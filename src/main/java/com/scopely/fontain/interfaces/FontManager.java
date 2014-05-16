package com.scopely.fontain.interfaces;

import android.graphics.Typeface;

/**
 * A FontManager manages all the FontFamilies and associated Fonts available.
 * It provides a default FontFamily and provides methods for accessing any other FontFamily by name.
 * It also has a method to reverse lookup a Font from a Typeface.
 */
public interface FontManager {

    FontFamily getDefaultFontFamily();

    FontFamily getFontFamily(String fontFamilyName);

    Font getFont(Typeface typeface);
}
