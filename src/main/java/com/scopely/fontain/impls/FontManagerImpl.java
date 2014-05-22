package com.scopely.fontain.impls;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import static com.scopely.fontain.Fontain.SYSTEM_DEFAULT;
import static com.scopely.fontain.utils.ParseUtils.parseItalics;
import static com.scopely.fontain.utils.ParseUtils.parseWeight;
import static com.scopely.fontain.utils.ParseUtils.parseWidth;

/**
 * Implementation of {@link com.scopely.fontain.interfaces.FontManager}
 * Walks through the provided fontFolder in the app's Assets folder and initializes the fonts and font families it finds within
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
        fontFamilyMap.put(SYSTEM_DEFAULT, initSystemDefaultFamily());

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

        FontFamily family = new FontFamilyImpl(SYSTEM_DEFAULT, fontList);

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

    private FontImpl initFont(String fontName, String familyName, AssetManager am) {
        Typeface typeface = Typeface.createFromAsset(am, String.format("%s/%s/%s", fontsFolder, familyName, fontName));
        int weight = parseWeight(fontName);
        int width = parseWidth(fontName);
        boolean italics = parseItalics(fontName);
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

    @Override
    public void applyFontToViewHierarchy(View view, Typeface typeface) {
        if(view instanceof ViewGroup){
            for(int i = 0; i < ((ViewGroup) view).getChildCount(); i++){
                applyFontToViewHierarchy(((ViewGroup) view).getChildAt(i), typeface);
            }
        } else if(view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);
        }
    }
}
