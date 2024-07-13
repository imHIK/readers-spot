package org.bigBrotherBooks.logger;

import java.util.function.Supplier;

public interface Logger {

    void logThis(LogType logType, String message);

    void logThis(LogType logType, Supplier<String> msgSupplier);

    void logThis(LogType logType, String message, Object... args);

    void logThis(LogType logType, Supplier<String> msgSupplier, Object... args);

    void logThis(LogType logType, String message, Supplier<Object>... argSuppliers);

    void logThis(LogType logType, Supplier<String> msgSupplier, Supplier<Object>... argSuppliers);
}
