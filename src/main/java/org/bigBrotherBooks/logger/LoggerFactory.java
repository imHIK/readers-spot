package org.bigBrotherBooks.logger;

public class LoggerFactory {

    public static Logger getLogger(Class<?> clazz) {
        return new LoggerImpl(clazz);
    }
}
