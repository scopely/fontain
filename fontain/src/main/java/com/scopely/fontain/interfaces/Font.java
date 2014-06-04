package com.scopely.fontain.interfaces;

import android.graphics.Typeface;

/**
 * A Font is an object that provides the actual typeface used to display text, as well as the weight, width and slope of that typeface.
 * It also provides a reference to the FontFamily it belongs to.
 */
public interface Font {
    public Typeface getTypeFace();
    public int getWeight();
    public int getWidth();
    public boolean getSlope();
    public FontFamily getFamily();
}
