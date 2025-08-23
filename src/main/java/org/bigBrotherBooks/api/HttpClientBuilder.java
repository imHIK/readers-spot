package org.bigBrotherBooks.api;

public class HttpClientBuilder implements ClientBuilder {

    private HttpClientConfig config;

    public HttpClientBuilder(HttpClientConfig config) {
        this.config = config;
    }

    public HttpClientBuilder() {
    }

    @Override
    public HttpClient build() {
        if (config == null) {
            config = new HttpClientConfig.Builder().build();
        }
        return new HttpClient(config);
    }
}
