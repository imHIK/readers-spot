package org.bigBrotherBooks.logger;

import org.slf4j.event.Level;

public enum LogType {
    ERROR(Level.ERROR),
    INFO(Level.INFO),
    DEBUG(Level.DEBUG),
    TRACE(Level.TRACE),
    WARN(Level.WARN);

    private final Level level;

    LogType(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }
}
