package org.bigBrotherBooks.logger;

import java.util.function.Supplier;

public interface Logger {

    void log(LogType logType, String message);

    void log(LogType logType, Supplier<String> msgSupplier);

    void log(LogType logType, String message, Object... args);

    void log(LogType logType, Supplier<String> msgSupplier, Object... args);

    void log(LogType logType, String message, Supplier<Object>... argSuppliers);

    void log(LogType logType, Supplier<String> msgSupplier, Supplier<Object>... argSuppliers);
}
