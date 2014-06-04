package com.scopely.fontain.interfaces;

import android.graphics.Typeface;

import com.scopely.fontain.enums.Slope;
import com.scopely.fontain.enums.Weight;
import com.scopely.fontain.enums.Width;

import java.util.List;

/**
 * A FontFamily is a collection of like Fonts that vary in weight, width and/or slope.
 * It provides methods to find the closest match it has to a given combination of weight, width and slope.
 */
public interface FontFamily {
    public Font getFont(int weight, int width, boolean italic);
    public Font getFont(Weight weight, Width width, Slope slope);
    public List<? extends Font> getFonts();
}
