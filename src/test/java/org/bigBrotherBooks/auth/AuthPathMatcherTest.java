package org.bigBrotherBooks.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthPathMatcherTest {

    @Test
    void isPublicAuthPath_matchesOnlyExactAuthRoutes() {
        assertTrue(AuthPathMatcher.isPublicAuthPath("auth/login"));
        assertTrue(AuthPathMatcher.isPublicAuthPath("/auth/register"));
        assertTrue(AuthPathMatcher.isPublicAuthPath("auth/refresh"));
        assertFalse(AuthPathMatcher.isPublicAuthPath("api/auth/login/exploit"));
        assertFalse(AuthPathMatcher.isPublicAuthPath("user/auth/login"));
    }
}
