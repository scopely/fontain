/*
 * Copyright (c) 2014 Scopely, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scopely.fontain.impls;

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
    private static final int PERFECT_MATCH_SCORE = 1500;
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
    public Font getFont(int weight, int width, boolean italic) {
        Font bestMatch = null;
        int bestMatchScore = 0;
        for(Font font : fonts){
            int matchScore = matchFunction(weight, width, italic, font.getWeight(), font.getWidth(), font.getSlope());
            if(matchScore == PERFECT_MATCH_SCORE) {
                return font;
            }
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

        return (int) (PERFECT_MATCH_SCORE - Math.sqrt(weightDiff*weightDiff + widthDiff*widthDiff + slopeDiff*slopeDiff));
    }

    @Override
    public String getName() {
        return name;
    }
}
