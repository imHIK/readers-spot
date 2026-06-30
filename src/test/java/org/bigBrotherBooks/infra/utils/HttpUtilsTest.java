package org.bigBrotherBooks.infra.utils;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpUtilsTest {

    @Test
    void buildUrl_joinsBaseAndPath() {
        assertEquals("https://api.test/books",
                HttpUtils.buildUrl("https://api.test", "books", null));
    }

    @Test
    void buildUrl_normalisesTrailingAndLeadingSlashes() {
        assertEquals("https://api.test/books",
                HttpUtils.buildUrl("https://api.test/", "/books", null));
    }

    @Test
    void buildUrl_appendsSingleQueryParam() {
        String url = HttpUtils.buildUrl("https://api.test", "/v", Map.<String, Object>of("q", "isbn:123"));
        assertEquals("https://api.test/v?q=isbn:123", url);
    }
}
