package com.scopely.fontain.enums;

/**
 * Standard font widths and their numerical values, on a 1-5 scale
 */
public enum Width {
    ULTRA_COMPRESSED(1),
    EXTRA_COMPRESSED(1),
    ULTRA_CONDENSED(1),
    EXTRA_CONDENSED(1),
    ULTRA_NARROW(1),
    EXTRA_NARROW(1),
    COMPRESSED(2),
    CONDENSED(2),
    NARROW(2),
    NORMAL(3),
    WIDE(4),
    EXTENDED(4),
    EXPANDED(4),
    EXTRA_WIDE(5),
    ULTRA_WIDE(5),
    EXTRA_EXTENDED(5),
    ULTRA_EXTENDED(5),
    EXTRA_EXPANDED(5),
    ULTRA_EXPANDED(5)
    ;

    public int value;

    Width(int value) {
        this.value = value;
    }

    public static Width get(WidthBase base, Modifier modifier) {
        try {
            return valueOf(String.format("%s_%s", modifier, base));
        } catch (IllegalArgumentException e) {
            return valueOf(base.name());
        }
    }
}
