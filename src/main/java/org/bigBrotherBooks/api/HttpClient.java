package org.bigBrotherBooks.api;


import org.bigBrotherBooks.infra.utils.HttpUtils;
import org.bigBrotherBooks.infra.utils.JsonUtils;
import org.bigBrotherBooks.logger.LogType;
import org.bigBrotherBooks.logger.Logger;
import org.bigBrotherBooks.logger.LoggerFactory;
import org.bigBrotherBooks.model.HttpRequest;
import org.bigBrotherBooks.model.HttpResponse;
import org.bigBrotherBooks.model.Request;
import org.bigBrotherBooks.model.Response;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;


public class HttpClient implements Client {

    private final java.net.http.HttpClient httpClient;
    private final HttpClientConfig config;
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);

    public HttpClient(HttpClientConfig config) {
        this.config = config;
        this.httpClient = java.net.http.HttpClient.newBuilder()
                .connectTimeout(config.getConnectTimeout())
                .build();
    }

    @Override
    public <T> Response<T> send(Request request) {
        LOGGER.log(LogType.ERROR, "Sending request: {}", () -> JsonUtils.toJson(request));
        Exception lastException = null;

        for (int attempt = 0; attempt <= config.getMaxRetries(); attempt++) {
            try {
                java.net.http.HttpRequest httpRequest = buildHttpRequest(request);
                java.net.http.HttpResponse<String> httpResponse =
                        httpClient.send(httpRequest, java.net.http.HttpResponse.BodyHandlers.ofString());

                //noinspection unchecked
                return mapResponse(httpResponse, (Class<T>) Object.class);

            } catch (IOException | InterruptedException e) {
                lastException = e;
                if (attempt < config.getMaxRetries()) {
                    waitBeforeRetry(attempt);
                }
            }
        }

        throw new RuntimeException("Request failed after retries", lastException);
    }

    public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, Class<T> responseType) {
        java.net.http.HttpRequest nativeRequest = buildHttpRequest(request);

        return httpClient.sendAsync(nativeRequest, java.net.http.HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> mapResponse(response, responseType))
                .exceptionally(throwable -> {
                    throw new RuntimeException("Async request failed", throwable);
                });
    }

    private java.net.http.HttpRequest buildHttpRequest(Request request) {
        HttpRequest httpRequest = (HttpRequest) request;
        java.net.http.HttpRequest.Builder builder = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create(HttpUtils.buildUrl(httpRequest.getBaseUrl(), httpRequest.getPath(), httpRequest.getQueryParams())))
                .timeout(config.getReadTimeout());
        HttpUtils.addHeaders(builder, config.getDefaultHeaders());
        HttpUtils.addHeaders(builder, httpRequest.getHeaders());
        HttpUtils.addBody(builder, httpRequest.getMethod(), httpRequest.getBody());

        if (config.getUserAgent() != null && !config.getUserAgent().isEmpty()) {
            builder.header("User-Agent", config.getUserAgent());
        }

        return builder.build();
    }

    private <T> HttpResponse<T> mapResponse(java.net.http.HttpResponse<String> response, Class<T> type) {
        HttpResponse<T> httpResponse = new HttpResponse<>();
        httpResponse.setStatus(response.statusCode());
        httpResponse.setHeaders(response.headers().map());

        String body = response.body();
        if (body != null && !body.isEmpty()) {
            T result = JsonUtils.fromJson(body, type);
            httpResponse.setResponse(result);
        }

        return httpResponse;
    }

    private void waitBeforeRetry(int attempt) {
        try {
            Thread.sleep(config.getRetryDelay().toMillis() * (long) Math.pow(config.getBackoffFactor(), attempt));
        } catch (InterruptedException exception) {
            // Ignore
        }
    }
}

