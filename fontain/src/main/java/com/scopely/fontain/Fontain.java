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

package com.scopely.fontain;

import android.content.Context;
import android.graphics.Typeface;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.internal.util.Predicate;
import com.scopely.fontain.enums.Slope;
import com.scopely.fontain.enums.Weight;
import com.scopely.fontain.enums.Width;
import com.scopely.fontain.impls.FontManagerImpl;
import com.scopely.fontain.interfaces.Font;
import com.scopely.fontain.interfaces.FontFamily;
import com.scopely.fontain.interfaces.FontManager;

import org.jetbrains.annotations.Nullable;


/**
 *
 * Fontain is a static wrapper around an instance of FontManager.
 * Fontain must be initialized with a Context and a default font family name once before use, and can then be called from anywhere.
 * The provided Font Views rely on Fontain, so it must be initialized before any layout using those views is inflated.
 * Your Application's OnCreate method is the recommended place to initialize Fontain.
 *
 */
public class Fontain {

    private static final String FONT_FOLDER = "fonts";
    public static final String SYSTEM_DEFAULT = "system_default";

    private static FontManager fontManager;
    private static boolean initialized;

    public static FontManager getFontManager() {
        if(fontManager == null) {
            fontManager = new FontManagerImpl();
        }
        if(!initialized) {
            Log.e("Fontain", "Fontain was accessed without being initialized. Falling back to system-font-only implementation.");
        }
        return fontManager;
    }

    public static void init(Context context, String fontsFolder, String defaultFontName){
        fontManager = new FontManagerImpl(context, fontsFolder, defaultFontName);
        initialized = true;
    }

    public static void init(Context context) {
        init(context, SYSTEM_DEFAULT);
    }

    public static void init(Context context, String defaultFontName) {
        init(context, FONT_FOLDER, defaultFontName);
    }

    public static void applyFontToViewHierarchy(View root, int weight, int width, boolean italic) {
        applyFontToViewHierarchy(root, weight, width, italic, null);
    }

    public static void applyFontToViewHierarchy(View root, Weight weight, Width width, Slope italic){
        applyFontToViewHierarchy(root, getFontManager().getDefaultFontFamily(), weight, width, italic, null);
    }

    public static void applyFontToViewHierarchy(View root, FontFamily fontFamily, Weight weight, Width width, Slope italic){
        applyFontToViewHierarchy(root, fontFamily, weight.value, width.value, italic.value, null);
    }

    public static void applyFontToViewHierarchy(View root, FontFamily fontFamily, int weight, int width, boolean italic){
        applyFontToViewHierarchy(root, fontFamily, weight, width, italic, null);
    }

    public static void applyFontToViewHierarchy(View root, Font font){
        applyFontToViewHierarchy(root, font, null);
    }

    public static void applyFontToViewHierarchy(View root, int weight, int width, boolean italic, @Nullable Predicate<TextView> predicate) {
        applyFontToViewHierarchy(root, getFontManager().getDefaultFontFamily(), weight, width, italic, predicate);
    }

    public static void applyFontToViewHierarchy(View root, Weight weight, Width width, Slope italic, @Nullable Predicate<TextView> predicate){
        applyFontToViewHierarchy(root, getFontManager().getDefaultFontFamily(), weight, width, italic, predicate);
    }

    public static void applyFontToViewHierarchy(View root, FontFamily fontFamily, Weight weight, Width width, Slope italic, @Nullable Predicate<TextView> predicate){
        applyFontToViewHierarchy(root, fontFamily, weight.value, width.value, italic.value, predicate);
    }

    public static void applyFontToViewHierarchy(View root, FontFamily fontFamily, int weight, int width, boolean italic, @Nullable Predicate<TextView> predicate){
        Font font = fontFamily.getFont(weight, width, italic);
        applyFontToViewHierarchy(root, font, predicate);
    }

    /**
     *
     * Walks the given view hierarchy and applies the given font to every TextView therein
     *
     * @param root the View hierarchy to walk
     * @param font the font to apply
     * @param predicate an optional predicate to check each TextView within the hierarchy against.
     *                  Returning true will apply the font to a given TextView.
     *                  Returning false will skip a given TextView and leave it as is.
     */
    public static void applyFontToViewHierarchy(View root, Font font, @Nullable Predicate<TextView> predicate){
        getFontManager().applyFontToViewHierarchy(root, font, null);
    }

    /**
     *
     * Walks the given view hiearchy and applies the font within the provided font family that best matches each TextView therein.
     * Acts similarly to {@link applyFontToViewHierarchy}, but maintains weight, width and slope for each TextView it encounters.
     *
     * @param root the View hierarchy to walk
     * @param family the font family to apply
     * @param predicate an optional predicate to check each TextView within the hierarchy against.
     *                  Returning true will apply the font family to a given TextView.
     *                  Returning false will skip a given TextView and leave it as is.
     */
    public static void applyFontFamilyToViewHierarchy(View root, FontFamily family, @Nullable Predicate<TextView> predicate){
        getFontManager().applyFontFamilyToViewHierarchy(root, family, predicate);
    }

    public static void applyFontFamilyToViewHierarchy(View root, FontFamily family) {
        applyFontFamilyToViewHierarchy(root, family, null);
    }

    public static void applyTransformationToViewHierarchy(View view, TransformationMethod method) {
        applyTransformationToViewHierarchy(view, method, null);
    }

    /**
     * Walks the given view hiearchy and applies the provided TransformationMethod.
     * Acts similarly to {@link applyFontToViewHierarchy}, but only applies to TransformationMethod.
     *
     * @param root the View hierarchy to walk
     * @param method the TransformationMethod to apply
     * @param predicate an optional predicate to check each TextView within the hierarchy against.
     *                  Returning true will apply the TransformationMethod to a given TextView.
     *                  Returning false will skip a given TextView and leave it as is.
     */
    public static void applyTransformationToViewHierarchy(View root, TransformationMethod method, @Nullable Predicate<TextView> predicate) {
        getFontManager().applyTransformationToViewHierarchy(root, method, predicate);
    }

    public static FontFamily getDefaultFontFamily(){
        return getFontManager().getDefaultFontFamily();
    }

    public static FontFamily getFontFamily(String fontFamilyName){
        return getFontManager().getFontFamily(fontFamilyName);
    }

    public static Font getFont(Typeface typeface) {
        return getFontManager().getFont(typeface);
    }
}
