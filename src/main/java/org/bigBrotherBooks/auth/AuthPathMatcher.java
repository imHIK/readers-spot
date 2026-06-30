package org.bigBrotherBooks.auth;

public final class AuthPathMatcher {

    private static final String LOGIN_PATH = "auth/login";
    private static final String REGISTER_PATH = "auth/register";
    private static final String REFRESH_PATH = "auth/refresh";

    private AuthPathMatcher() {
    }

    public static String normalizePath(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }
        return path.startsWith("/") ? path.substring(1) : path;
    }

    public static boolean isPublicAuthPath(String path) {
        String normalized = normalizePath(path);
        return LOGIN_PATH.equals(normalized)
                || REGISTER_PATH.equals(normalized)
                || REFRESH_PATH.equals(normalized);
    }
}
