package com.scopely.fontain;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scopely.fontain.impls.FontManagerImpl;
import com.scopely.fontain.interfaces.Font;
import com.scopely.fontain.interfaces.FontFamily;
import com.scopely.fontain.interfaces.FontManager;


/**
 *
 * Fontain is a static wrapper around an instance of FontManager.
 * Fontain must be initialized with a Context and a default font family name once before use, and can then be called from anywhere
 * The provided Font Views rely on Fontain, so it must be initialized before any layout using those views is inflated.
 * Your Application's OnCreate method is the recommended place to initialize Fontain.
 *
 */
public class Fontain {

    private static final String FONT_FOLDER = "fonts";
    public static final String SYSTEM_DEFAULT = "system_default";

    private static FontManager fontManager;

    public static FontManager getFontManager() {
        if(fontManager != null) {
            return fontManager;
        } else {
            throw new RuntimeException("Must initialize Fontain before accessing it");
        }
    }

    public static void init(Context context, String fontsFolder, String defaultFontName){
        fontManager = new FontManagerImpl(context, fontsFolder, defaultFontName);
    }

    public static void init(Context context, String defaultFontName) {
        init(context, FONT_FOLDER, defaultFontName);
    }

    public static void init(Context context, int defaultFontResId) {
        init(context, FONT_FOLDER, context.getString(defaultFontResId));
    }

    public static void fontify(View view, int weight, int width, boolean italic) {
        fontify(view, getFontManager().getDefaultFontFamily(), weight, width, italic);
    }

    public static void fontify(View view, FontFamily fontFamily, int weight, int width, boolean italic){
        Typeface typeface = fontFamily.getTypeFace(weight, width, italic);
        fontify(view, typeface);
    }

    public static void fontify(View view, Typeface typeface){
        if(view instanceof ViewGroup){
            for(int i = 0; i < ((ViewGroup) view).getChildCount(); i++){
                fontify(((ViewGroup) view).getChildAt(i), typeface);
            }
        } else if(view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);
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
