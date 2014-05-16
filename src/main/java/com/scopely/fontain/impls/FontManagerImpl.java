package com.scopely.fontain.impls;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.scopely.fontain.Fontain;
import com.scopely.fontain.enums.Slope;
import com.scopely.fontain.enums.Weight;
import com.scopely.fontain.enums.Width;
import com.scopely.fontain.interfaces.Font;
import com.scopely.fontain.interfaces.FontFamily;
import com.scopely.fontain.interfaces.FontManager;
import com.scopely.fontain.utils.FontViewUtils;
import com.scopely.fontain.utils.ParseUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        int weight = ParseUtils.parseWeight(font_name);
        int width = ParseUtils.parseWidth(font_name);
        boolean italics = ParseUtils.parseItalics(font_name);
        return new FontImpl(typeface, weight, width, italics);
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
