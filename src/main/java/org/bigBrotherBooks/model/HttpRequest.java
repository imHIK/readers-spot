package org.bigBrotherBooks.model;

import java.util.Map;

public class HttpRequest extends Request {

    private HttpMethod method;
    private String baseUrl;
    private String path;
    private Map<String, Object> pathParams;
    private Map<String, Object> queryParams;
    private Map<String, String> headers;
    private String body;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = HttpMethod.valueOf(method);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getPathParams() {
        return pathParams;
    }

    public void setPathParams(Map<String, Object> pathParams) {
        this.pathParams = pathParams;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, Object> queryParams) {
        this.queryParams = queryParams;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body.toString();
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", baseUrl='" + baseUrl + '\'' +
                ", path='" + path + '\'' +
                ", pathParams=" + pathParams +
                ", queryParams=" + queryParams +
                ", headers=" + headers +
                ", body=" + body +
                '}';
    }
}
