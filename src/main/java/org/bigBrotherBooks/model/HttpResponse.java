package org.bigBrotherBooks.model;

import java.util.List;
import java.util.Map;

public class HttpResponse<T> extends Response<T> {

    private Map<String, List<String>> headers;

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }
}
