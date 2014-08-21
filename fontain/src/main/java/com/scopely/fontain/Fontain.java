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
        init(context, FONT_FOLDER, SYSTEM_DEFAULT);
    }

    public static void init(Context context, String defaultFontName) {
        init(context, FONT_FOLDER, defaultFontName);
    }

    public static void init(Context context, String fontsFolder, int defaultFontResId) {
        init(context,fontsFolder, context.getString(defaultFontResId));
    }

    public static void init(Context context, int defaultFontResId) {
        init(context, FONT_FOLDER, context.getString(defaultFontResId));
    }

    public static void applyFontToViewHierarchy(View view, int weight, int width, boolean italic) {
        applyFontToViewHierarchy(view, getFontManager().getDefaultFontFamily(), weight, width, italic);
    }

    public static void applyFontToViewHierarchy(View view, Weight weight, Width width, Slope italic){
        applyFontToViewHierarchy(view, getFontManager().getDefaultFontFamily(), weight, width, italic);
    }

    public static void applyFontToViewHierarchy(View view, FontFamily fontFamily, Weight weight, Width width, Slope italic){
        applyFontToViewHierarchy(view, fontFamily, weight.value, width.value, italic.value);
    }

    public static void applyFontToViewHierarchy(View view, FontFamily fontFamily, int weight, int width, boolean italic){
        Font font = fontFamily.getFont(weight, width, italic);
        applyFontToViewHierarchy(view, font);
    }

    /**
     *
     * Walks the given view hierarchy and applies the given font to every TextView therein
     *
     * @param view the View hierarchy to walk
     * @param font the font to apply
     */
    public static void applyFontToViewHierarchy(View view, Font font){
        getFontManager().applyFontToViewHierarchy(view, font);
    }

    /**
     *
     * Walks the given view hiearchy and applies the font within the provided font family that best matches each TextView therein.
     * Acts similarly to {@link applyFontToViewHierarchy}, but maintains weight, width and slope for each TextView it encounters.
     *
     * @param view the View hierarchy to walk
     * @param family the font family to apply
     */
    public static void applyFontFamilyToViewHierarchy(View view, FontFamily family){
        getFontManager().applyFontFamilyToViewHierarchy(view, family);
    }

    public static void applyTransformationToViewHierarchy(View view, TransformationMethod method) {
        applyTransformationToViewHierarchy(view, method, null);
    }

    public static void applyTransformationToViewHierarchy(View view, TransformationMethod method, @Nullable Predicate<TextView> predicate) {
        if(view instanceof ViewGroup){
            for(int i = 0; i < ((ViewGroup) view).getChildCount(); i++){
                applyTransformationToViewHierarchy(((ViewGroup) view).getChildAt(i), method);
            }
        } else if(view instanceof TextView) {
            if(predicate == null || predicate.apply((TextView) view)){
                ((TextView) view).setTransformationMethod(method);
            }
        }
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
