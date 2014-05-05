package com.scopely.fontain.impls;

import android.graphics.Typeface;

import com.scopely.fontain.interfaces.Font;
import com.scopely.fontain.interfaces.FontFamily;

/**
 * Part of the With Buddies™ Platform
 * © 2013 Scopely, Inc.
 */
public class FontImpl implements Font {
    public static String TAG = FontImpl.class.getCanonicalName();
    private Typeface typeface;
    private boolean slope;
    private int weight;
    private int width;
    private FontFamily family;

    public FontImpl(Typeface typeface, int weight, int width, boolean italics) {
        this.family = family;
        this.typeface = typeface;
        this.weight = weight;
        this.width = width;
        this.slope = italics;
    }

    @Override
    public Typeface getTypeFace() {
        return typeface;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public boolean getSlope() {
        return slope;
    }

    @Override
    public FontFamily getFamily() {
        return family;
    }

    public Font setFamily(FontFamily family) {
        this.family = family;
        return this;
    }
}
