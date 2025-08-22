package org.bigBrotherBooks.api;

import java.time.Duration;
import java.util.Map;

public class HttpClientConfig {
    private Duration connectTimeout = Duration.ofSeconds(10);
    private Duration readTimeout = Duration.ofSeconds(30);
    private int maxRetries = 3;
    private Duration retryDelay = Duration.ofSeconds(1);
    private double backoffFactor = 2.0;
    private Map<String, String> defaultHeaders;
    private String userAgent = "ReaderProject-HttpClient/1.0";

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public Duration getRetryDelay() {
        return retryDelay;
    }

    public double getBackoffFactor() {
        return backoffFactor;
    }

    public Map<String, String> getDefaultHeaders() {
        return defaultHeaders;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public static class Builder {
        private final HttpClientConfig config = new HttpClientConfig();

        public Builder connectTimeout(Duration timeout) {
            config.connectTimeout = timeout;
            return this;
        }

        public Builder readTimeout(Duration timeout) {
            config.readTimeout = timeout;
            return this;
        }

        public Builder maxRetries(int retries) {
            config.maxRetries = retries;
            return this;
        }

        public Builder retryDelay(Duration delay) {
            config.retryDelay = delay;
            return this;
        }

        public Builder backoffFactor(double factor) {
            config.backoffFactor = factor;
            return this;
        }

        public Builder defaultHeaders(Map<String, String> headers) {
            config.defaultHeaders = headers;
            return this;
        }

        public Builder userAgent(String userAgent) {
            config.userAgent = userAgent;
            return this;
        }

        public HttpClientConfig build() {
            return config;
        }
    }
}
