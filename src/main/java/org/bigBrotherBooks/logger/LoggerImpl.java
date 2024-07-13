package org.bigBrotherBooks.logger;

import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggingEventBuilder;

import java.util.function.Supplier;

public class LoggerImpl implements Logger {

    private final org.slf4j.Logger logger;

    LoggerImpl(Class<?> clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public void logThis(LogType logType, String message) {
        logger.atLevel(logType.getLevel()).setMessage(message).log();
    }

    @Override
    public void logThis(LogType logType, Supplier<String> msgSupplier) {
        logger.atLevel(logType.getLevel()).setMessage(msgSupplier).log();
    }

    @Override
    public void logThis(LogType logType, String message, Object... args) {
        LoggingEventBuilder loggingEventBuilder = logger.atLevel(logType.getLevel()).setMessage(message);
        for (Object arg : args) {
            loggingEventBuilder = loggingEventBuilder.addArgument(arg);
        }
        loggingEventBuilder.log();
    }

    @Override
    public void logThis(LogType logType, Supplier<String> msgSupplier, Object... args) {
        LoggingEventBuilder loggingEventBuilder = logger.atLevel(logType.getLevel()).setMessage(msgSupplier);
        for (Object arg : args) {
            loggingEventBuilder = loggingEventBuilder.addArgument(arg);
        }
        loggingEventBuilder.log();
    }

    @Override
    public void logThis(LogType logType, String message, Supplier<Object>... argSuppliers) {
        LoggingEventBuilder loggingEventBuilder = logger.atLevel(logType.getLevel()).setMessage(message);
        for (Supplier<Object> argSupplier : argSuppliers) {
            loggingEventBuilder = loggingEventBuilder.addArgument(argSupplier);
        }
        loggingEventBuilder.log();
    }

    @Override
    public void logThis(LogType logType, Supplier<String> msgSupplier, Supplier<Object>... argSuppliers) {
        LoggingEventBuilder loggingEventBuilder = logger.atLevel(logType.getLevel()).setMessage(msgSupplier);
        for (Supplier<Object> argSupplier : argSuppliers) {
            loggingEventBuilder = loggingEventBuilder.addArgument(argSupplier);
        }
        loggingEventBuilder.log();
    }
}
