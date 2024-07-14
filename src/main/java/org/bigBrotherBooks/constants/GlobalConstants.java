package org.bigBrotherBooks.constants;

import java.util.concurrent.TimeUnit;

public class GlobalConstants {
    public static final String JWT_ISSUER = "readers-spot-app";
    public static final long JWT_VALID_DURATION = TimeUnit.DAYS.toSeconds(30);
    public static final long RENT_REQUEST_LOCK_TIME = TimeUnit.MINUTES.toMillis(30);
    public static final long RENT_DEADLINE = TimeUnit.DAYS.toMillis(15);
    public static final String BOOKS_API_BASE_URL = "https://books.googleapis.com/books/v1";
}
