package com.scopely.fontain.enums;

/**
 * Part of the With Buddies™ Platform
 * © 2013 Scopely, Inc.
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
