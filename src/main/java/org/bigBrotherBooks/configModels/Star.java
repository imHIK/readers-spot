package org.bigBrotherBooks.configModels;

public enum Star {
    ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

    private final int value;

    Star(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
