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

package com.scopely.fontain.utils;

import com.scopely.fontain.enums.Modifier;
import com.scopely.fontain.enums.Weight;
import com.scopely.fontain.enums.WeightBase;
import com.scopely.fontain.enums.Width;
import com.scopely.fontain.enums.WidthBase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that contains numerous static methods that facilitate parsing the weight, width and slope of a font from its name
 */
public class ParseUtils {

    /**
     *
     * Parses a name to find whether the font is italic or not
     *
     * @param font_name
     * @return true iff font is italic
     */
    public static boolean parseItalics(String font_name) {
        return font_name.toLowerCase().contains("italic") || font_name.toLowerCase().contains("oblique");
    }

    /**
     *
     * Parses a name to find the width of the font
     *
     * @param font_name
     * @return the width of the font
     */
    public static int parseWidth(String font_name) {
        for(WidthBase width : WidthBase.values()){
            if(font_name.toLowerCase().contains(width.name().toLowerCase())){
                for(Modifier modifier : Modifier.values()){
                    if(match(font_name, width.name(), modifier.name())){
                        return Width.get(width, modifier).value;
                    }
                }
                return Width.get(width, Modifier.NONE).value;
            }
        }
        return Width.NORMAL.value;
    }

    /**
     *
     * Parses a name to find the weight of the font. Gives priority to matches of any {@link com.scopely.fontain.enums.WeightBase}.
     * If no WeightBase match is found, it looks for a 100<= number <=900 and accepts that as the weight of the font.
     *
     * @param font_name
     * @return the weight of the font
     */
    public static int parseWeight(String font_name) {
        for(WeightBase weight : WeightBase.values()){
            if(font_name.toLowerCase().contains(weight.name().toLowerCase())){
                for(Modifier modifier : Modifier.values()){
                    if(match(font_name, weight.name(), modifier.name())){
                        return Weight.get(weight, modifier).value;
                    }
                }
                return Weight.get(weight, Modifier.NONE).value;
            }
        }

        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(font_name);
        while(matcher.find()){
            int match = Integer.parseInt(matcher.group());
            if(match >= 100 && match <= 900){
                return match;
            }
        }
        return Weight.NORMAL.value;
    }

    /**
     *
     * A regex search that searches for a match of the given base/modifier pair. Returns true iff both the base and the modifier are found, with the modifier preceding the base and separated by at most one character.
     *
     * Example:
     * Base: BOLD
     * Modifier: EXTRA
     *
     * Will return true for:
     * ExtraBold, Extra.Bold, Extra_Bold, EXTRABOLD
     * Will return false for:
     * BoldExtra, ExtraNarrowBold, Extra__Bold, UltraBold
     *
     * @param string
     * @param base
     * @param modifier
     * @return
     */
    public static boolean match(String string, String base, String modifier) {
        String regex = String.format("(?i)(%s.?)(?=%s)", modifier, base);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }
}
