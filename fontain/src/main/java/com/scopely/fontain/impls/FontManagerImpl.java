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

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.internal.util.Predicate;
import com.scopely.fontain.R;
import com.scopely.fontain.enums.Slope;
import com.scopely.fontain.enums.Weight;
import com.scopely.fontain.enums.Width;
import com.scopely.fontain.interfaces.Font;
import com.scopely.fontain.interfaces.FontFamily;
import com.scopely.fontain.interfaces.FontManager;
import com.scopely.fontain.utils.FontViewUtils;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    private final Map<String, FontFamily> fontFamilyMap = new HashMap<String, FontFamily>();
    private FontFamily defaultFontFamily;

    /**
     * This constructor will initialize a "dumb" version of FontManager that only has the system default fonts available.
     */
    public FontManagerImpl() {
        FontFamily systemDefaultFamily = initSystemDefaultFamily();
        fontFamilyMap.put(SYSTEM_DEFAULT, systemDefaultFamily);
        defaultFontFamily = systemDefaultFamily;

    }

    public FontManagerImpl(Context context, String fontsFolder, String defaultFontName) {
        this();
        init(context, fontsFolder, defaultFontName);
    }

    private void init(Context context, String fontsFolder, String defaultFontName){
        try {
            AssetManager am = context.getAssets();
            String[] fontFamilies = am.list(fontsFolder);
            for(String fontFamily : fontFamilies){
                if(am.list(String.format("%s/%s", fontsFolder, fontFamily)).length > 0){
                    fontFamilyMap.put(fontFamily, initFontFamily(fontsFolder, fontFamily, am));
                }
            }
        } catch (IOException e) {
            Log.e("Fontain", "IOException while initializing fonts from assets", e);
        }

        defaultFontFamily = getFontFamily(defaultFontName);
    }

    static FontFamily initSystemDefaultFamily() {
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

    private FontFamily initFontFamily(String fontsFolder, String fontFamily, AssetManager am) {
        try {
            List<FontImpl> fontList = new ArrayList<FontImpl>();
            String[] fonts = am.list(String.format("%s/%s", fontsFolder, fontFamily));
            for(String font : fonts){
                FontImpl fontObj = initFont(fontsFolder, fontFamily, font, am);
                if(fontObj != null){
                    fontList.add(fontObj);
                }
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

    @Nullable
    private FontImpl initFont(String fontsFolder, String familyName, String fontName, AssetManager am) {
        try {
            Typeface typeface = Typeface.createFromAsset(am, String.format("%s/%s/%s", fontsFolder, familyName, fontName));
            int weight = parseWeight(fontName);
            int width = parseWidth(fontName);
            boolean italics = parseItalics(fontName);
            return new FontImpl(typeface, weight, width, italics);
        } catch (Exception e) {
            Log.w("Fontain", String.format("Could not create typeface for %s/%s/%s", fontsFolder, familyName, fontName));
            return null;
        }
    }

    @Override
    public FontFamily getDefaultFontFamily() {
        return defaultFontFamily;
    }

    @Override
    public FontFamily getFontFamily(String fontFamilyName) {
        FontFamily family = fontFamilyMap.get(fontFamilyName);
        if(family == null) {
            Log.w("Fontain", String.format("Requested Font Family \"%s\" does not exist, using default Font Family", fontFamilyName));
            family = getDefaultFontFamily();
        }
        return family;
    }

    @Override
    public Collection<FontFamily> getAllFontFamilies() {
        return Collections.unmodifiableCollection(fontFamilyMap.values());
    }

    @Override
    public Font getFont(@Nullable Typeface typeface) {
        if(typeface == null) {
            return getDefaultFontFamily().getFont(Weight.NORMAL, Width.NORMAL, Slope.NORMAL);
        }
        for(Map.Entry<String, FontFamily> entry : fontFamilyMap.entrySet()) {
            for(Font font : entry.getValue().getFonts()) {
                if(typeface.equals(font.getTypeFace())) {
                    return font;
                }
            }
        }
        Log.w("Fontain", "Font could not be found for typeface, returning default font");
        Slope slope = typeface.isItalic() ? Slope.ITALIC : Slope.NORMAL;
        Weight weight = typeface.isBold() ? Weight.BOLD : Weight.NORMAL;
        return getDefaultFontFamily().getFont(weight, Width.NORMAL, slope);
    }

    @Override
    public FontFamily addFontFamilyFromFiles(String fontFamilyName, File... files) {
        FontFamily family = initFontFamily(fontFamilyName, files);
        fontFamilyMap.put(fontFamilyName, family);
        return family;
    }

    private FontFamily initFontFamily(String fontFamily, File... files) {
        List<FontImpl> fontList = new ArrayList<FontImpl>();
        for(File file : files){
            FontImpl fontObj = initFont(file, file.getName());
            if(fontObj != null){
                fontList.add(fontObj);
            }
        }
        FontFamily family = new FontFamilyImpl(fontFamily, fontList);
        for(FontImpl font : fontList) {
            font.setFamily(family);
        }
        return family;
    }

    private FontImpl initFont(File file, String fontName) {
        try {
            Typeface typeface = Typeface.createFromFile(file);
            int weight = parseWeight(fontName);
            int width = parseWidth(fontName);
            boolean italics = parseItalics(fontName);
            return new FontImpl(typeface, weight, width, italics);
        } catch (Exception e) {
            Log.w("Fontain", String.format("Could not create typeface for %s/%s", file, fontName));
            return null;
        }
    }

    @Override
    public void applyFontToViewHierarchy(View view, Font font, @Nullable Predicate<TextView> predicate) {
        if(view instanceof ViewGroup){
            for(int i = 0; i < ((ViewGroup) view).getChildCount(); i++){
                applyFontToViewHierarchy(((ViewGroup) view).getChildAt(i), font, predicate);
            }
        } else if(view instanceof TextView) {
            if(predicate == null || predicate.apply((TextView) view)) {
                FontViewUtils.ensureTags((TextView) view, this);
                ((TextView) view).setTypeface(font.getTypeFace());
            }
        }
    }

    @Override
    public void applyFontFamilyToViewHierarchy(View view, FontFamily family, @Nullable Predicate<TextView> predicate) {
        if(view instanceof ViewGroup){
            for(int i = 0; i < ((ViewGroup) view).getChildCount(); i++){
                applyFontFamilyToViewHierarchy(((ViewGroup) view).getChildAt(i), family, predicate);
            }
        } else if(view instanceof TextView) {
            TextView textView = (TextView) view;

            if(predicate == null || predicate.apply(textView)) {
                FontViewUtils.ensureTags(textView, this);

                Integer weight = (Integer) textView.getTag(R.id.fontain_tag_weight);
                Integer width = (Integer) textView.getTag(R.id.fontain_tag_width);
                Boolean slope = (Boolean) textView.getTag(R.id.fontain_tag_slope);

                Font newFont = family.getFont(weight, width, slope);
                textView.setTag(R.id.fontain_tag_font, newFont);
                textView.setTypeface(newFont.getTypeFace());
            }
        }
    }

    @Override
    public void applyTransformationToViewHierarchy(View view, TransformationMethod method, @Nullable Predicate<TextView> predicate) {
        if(view instanceof ViewGroup){
            for(int i = 0; i < ((ViewGroup) view).getChildCount(); i++){
                applyTransformationToViewHierarchy(((ViewGroup) view).getChildAt(i), method, predicate);
            }
        } else if(view instanceof TextView) {
            if(predicate == null || predicate.apply((TextView) view)){
                ((TextView) view).setTransformationMethod(method);
            }
        }
    }
}
