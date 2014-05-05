package com.scopely.fontain.impls;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.scopely.fontain.Fontain;
import com.scopely.fontain.enums.Modifier;
import com.scopely.fontain.enums.Slope;
import com.scopely.fontain.enums.Weight;
import com.scopely.fontain.enums.WeightBase;
import com.scopely.fontain.enums.Width;
import com.scopely.fontain.enums.WidthBase;
import com.scopely.fontain.interfaces.Font;
import com.scopely.fontain.interfaces.FontFamily;
import com.scopely.fontain.interfaces.FontManager;
import com.scopely.fontain.utils.FontViewUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Part of the With Buddies™ Platform
 * © 2013 Scopely, Inc.
 */
public class FontManagerImpl implements FontManager {
    private final String fontsFolder;
    private final String defaultFontName;

    private final Map<String, FontFamily> fontFamilyMap = new HashMap<String, FontFamily>();
    private FontFamily defaultFontFamily;

    public FontManagerImpl(Context context, String fontsFolder, String defaultFontName) {
        this.fontsFolder = fontsFolder;
        this.defaultFontName = defaultFontName;
        init(context);
    }

    private void init(Context context){
        AssetManager am = context.getAssets();
        fontFamilyMap.put(Fontain.SYSTEM_DEFAULT, initSystemDefaultFamily());

        try {
            String[] fontFamilies = am.list(fontsFolder);
            for(String fontFamily : fontFamilies){
                if(am.list(String.format("%s/%s", fontsFolder, fontFamily)).length > 0){
                    fontFamilyMap.put(fontFamily, initFontFamily(fontFamily, am));
                }
            }
            defaultFontFamily = fontFamilyMap.get(defaultFontName);
        } catch (IOException e) {
            //no op
        }

    }

    private FontFamily initSystemDefaultFamily() {
        List<Font> fontList = new ArrayList<Font>();
        int[] styles = new int[]{Typeface.NORMAL, Typeface.BOLD, Typeface.BOLD_ITALIC, Typeface.ITALIC};
        for(int style : styles) {
            Width width = Width.NORMAL;
            Weight weight = FontViewUtils.isBold(style) ? Weight.BOLD : Weight.NORMAL;
            Slope slope = FontViewUtils.isItalic(style) ? Slope.ITALIC : Slope.NORMAL;
            fontList.add(new FontImpl(Typeface.defaultFromStyle(style), weight.value, width.value, slope.value));
        }

        FontFamily family = new FontFamilyImpl(Fontain.SYSTEM_DEFAULT, fontList);

        for(Font font : fontList) {
            ((FontImpl) font).setFamily(family);
        }

        return family;
    }

    private FontFamily initFontFamily(String fontFamily, AssetManager am) {
        try {
            List<FontImpl> fontList = new ArrayList<FontImpl>();
            String[] fonts = am.list(String.format("%s/%s", fontsFolder, fontFamily));
            for(String font : fonts){
                fontList.add(initFont(font, fontFamily, am));
            }
            FontFamily family = new FontFamilyImpl(fontFamily, fontList);
            for(FontImpl font : fontList) {
                font.setFamily(family);
            }
            return family;
        } catch (IOException e) {
            return null;
        }
    }

    private FontImpl initFont(String font_name, String family_name, AssetManager am) {
        Typeface typeface = Typeface.createFromAsset(am, String.format("%s/%s/%s", fontsFolder, family_name, font_name));
        int weight = parseWeight(font_name);
        int width = parseWidth(font_name);
        boolean italics = parseItalics(font_name);
        return new FontImpl(typeface, weight, width, italics);
    }

    private static boolean parseItalics(String font_name) {
        return font_name.toLowerCase().contains("italic");
    }

    private static int parseWidth(String font_name) {
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

    private static int parseWeight(String font_name) {
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

    private static boolean match(String string, String base, String modifier) {
        Pattern pattern = Pattern.compile(String.format("(%s.?)(?=%s)", modifier, base));
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    @Override
    public FontFamily getDefaultFontFamily() {
        return defaultFontFamily;
    }

    @Override
    public FontFamily getFontFamily(String fontFamilyName) {
        FontFamily family = fontFamilyMap.get(fontFamilyName);
        if(family == null) {
            family = getDefaultFontFamily();
        }
        return family;
    }

    @Override
    public Font getFont(Typeface typeface) {
        for(Map.Entry<String, FontFamily> entry : fontFamilyMap.entrySet()) {
            for(Font font : entry.getValue().getFonts()) {
                if(typeface.equals(font.getTypeFace())) {
                    return font;
                }
            }
        }
        Slope slope = typeface.isItalic() ? Slope.ITALIC : Slope.NORMAL;
        Weight weight = typeface.isBold() ? Weight.BOLD : Weight.NORMAL;
        return getDefaultFontFamily().getFont(weight, Width.NORMAL, slope);
    }
}
