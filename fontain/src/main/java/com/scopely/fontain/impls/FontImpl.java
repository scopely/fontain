package com.scopely.fontain.impls;

import android.graphics.Typeface;

import com.scopely.fontain.interfaces.Font;
import com.scopely.fontain.interfaces.FontFamily;

/**
 * An implementation of {@link Font} that holds the typeface and weight, width and slope parameters of a font, as well as a reference to the family it belongs to
 */
public class FontImpl implements Font {
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
