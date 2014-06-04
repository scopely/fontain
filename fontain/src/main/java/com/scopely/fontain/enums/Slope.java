package com.scopely.fontain.enums;

/**
 * Italic or not.
 */
public enum Slope {
    NORMAL(false),
    ITALIC(true)
    ;

    public boolean value;

    Slope(boolean value) {
        this.value = value;
    }
}
