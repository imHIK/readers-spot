package org.bigBrotherBooks.api;

public class HttpClientBuilder implements ClientBuilder {

    private HttpClientConfig config;

    HttpClientBuilder(HttpClientConfig config) {
        this.config = config;
    }

    @Override
    public HttpClient build() {
        if (config == null) {
            config = new HttpClientConfig.Builder().build();
        }
        return new HttpClient(config);
    }
}
