package org.bigBrotherBooks.infra.utils;

import org.bigBrotherBooks.model.HttpMethod;

import java.net.http.HttpRequest;
import java.util.Map;

public class HttpUtils {

    public static String buildUrl(String baseUrl, String path, Map<String, Object> queryParams) {
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        String url = baseUrl + path;
        if (!CollectionUtils.isEmpty(queryParams)) {
            StringBuilder queryString = new StringBuilder("?");
            queryParams.forEach((key, value) -> {
                if (queryString.length() > 1) {
                    queryString.append("&");
                }
                queryString.append(key).append("=").append(value);
            });
            url += queryString;
        }
        return url;
    }

    public static HttpRequest.Builder addHeaders(HttpRequest.Builder builder, Map<String, String> headers) {
        if (headers != null) {
            headers.forEach(builder::header);
        }
        return builder;
    }

    public static HttpRequest.Builder addBody(HttpRequest.Builder builder, HttpMethod method, String body) {
        switch (method) {
            case POST, PUT, PATCH -> {
                if (body != null) {
                    builder.method(method.name(), HttpRequest.BodyPublishers.ofString(body));
                } else {
                    builder.method(method.name(), HttpRequest.BodyPublishers.noBody());
                }
            }
            case GET, DELETE, HEAD -> builder.method(method.name(), HttpRequest.BodyPublishers.noBody());
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
        return builder;
    }
}
