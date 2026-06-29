package org.bigBrotherBooks.exception;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiErrorTest {

    @Test
    void constructorPopulatesFieldsAndTimestamp() {
        ApiError error = new ApiError(404, "Not Found", "missing");
        assertEquals(404, error.getStatus());
        assertEquals("Not Found", error.getError());
        assertEquals("missing", error.getMessage());
        assertTrue(error.getTimestamp() > 0);
        assertNull(error.getFieldErrors());
    }

    @Test
    void fieldErrorsAreOptional() {
        ApiError error = new ApiError(400, "Bad Request", "Validation failed");
        error.setFieldErrors(Map.of("name", "is required"));
        assertEquals("is required", error.getFieldErrors().get("name"));
    }
}
