package com.scopely.fontain.impls;

import android.graphics.Typeface;

import com.scopely.fontain.enums.Slope;
import com.scopely.fontain.enums.Weight;
import com.scopely.fontain.enums.Width;
import com.scopely.fontain.interfaces.Font;
import com.scopely.fontain.interfaces.FontFamily;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of {@link com.scopely.fontain.interfaces.FontFamily}
 */
public class FontFamilyImpl implements FontFamily {
    List<? extends Font> fonts;
    String name;

    public FontFamilyImpl(String name, List<? extends Font> fonts) {
        this.fonts = fonts;
        this.name = name;
    }

    public FontFamilyImpl(String name, Font... fonts) {
        this.fonts = Arrays.asList(fonts);
        this.name = name;
    }

    @Override
    public Typeface getTypeFace(int weight, int width, boolean italic) {
        return getFont(weight, width, italic).getTypeFace();
    }

    @Override
    public Typeface getTypeFace(Weight weight, Width width, Slope slope) {
        return getTypeFace(weight.value, width.value, slope.value);
    }

    @Override
    public Font getFont(int weight, int width, boolean italic) {
        Font bestMatch = null;
        int bestMatchScore = 0;
        for(Font font : fonts){
            int matchScore = matchFunction(weight, width, italic, font.getWeight(), font.getWidth(), font.getSlope());
            if (matchScore > bestMatchScore){
                bestMatchScore = matchScore;
                bestMatch = font;
            }
        }
        return bestMatch;
    }

    @Override
    public Font getFont(Weight weight, Width width, Slope slope) {
        return getFont(weight.value, width.value, slope.value);
    }

    @Override
    public List<? extends Font> getFonts() {
        return fonts;
    }

    public static int matchFunction(int targetWeight, int targetWidth, boolean targetSlope, int weight, int width, boolean slope){
        int weightDiff = targetWeight - weight;
        int widthDiff = (targetWidth - width) * 200;
        int slopeDiff = targetSlope == slope ? 0 : 800;

        return (int) (1500 - Math.sqrt(weightDiff*weightDiff + widthDiff*widthDiff + slopeDiff*slopeDiff));
    }
}
