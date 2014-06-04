package com.scopely.fontain.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.scopely.fontain.R;
import com.scopely.fontain.enums.Slope;
import com.scopely.fontain.enums.Weight;
import com.scopely.fontain.enums.Width;
import com.scopely.fontain.interfaces.FontFamily;
import com.scopely.fontain.interfaces.FontManager;
import com.scopely.fontain.transformationmethods.CapsTransformationMethod;

import org.jetbrains.annotations.Nullable;

/**
 *
 * A class that contains numerous static methods that facilitate the functions of any Font Views
 *
 */
public class FontViewUtils {

    public static final int CAPS_MODE_CHARACTERS = 1;
    public static final int CAPS_MODE_WORDS = 2;
    public static final int CAPS_MODE_SENTENCES = 3;
    public static final int CAPS_MODE_NONE = 0;

    /**
     *
     * Called from the constructor of {@param view}, sets up the typeface and caps mode of the view based on its XML attributes
     * @param view The View being initialized
     * @param context The context provided to the view's constructor
     * @param attributeSet the attribute set provided to the view's constructor, if present
     * @param fontManager the FontManager to pull fonts from. Usually Fontain.getFontManager()
     */
    public static void initialize(TextView view, Context context, @Nullable AttributeSet attributeSet, FontManager fontManager){
        if (view.isInEditMode()) {
            return;
        }
        Typeface typeface = FontViewUtils.typefaceFromAttributeSet(context, attributeSet, fontManager);
        CapsTransformationMethod method = FontViewUtils.capsModeFromAttributeSet(context, attributeSet);
        view.setTransformationMethod(method);
        view.setTypeface(typeface);
    }

    /**
     *
     * Parses a provided AttributeSet and returns a typeface based on the found attributes.
     * Any attributes that are not set in the AttributeSet are assumed to be their default values
     * font_weight => NORMAL
     * font_width => NORMAL
     * font_slope => NORMAL
     * font_family => Whatever default {@param fontManager} has set
     *
     * @param context
     * @param attributeSet
     * @return
     */
    public static Typeface typefaceFromAttributeSet(Context context, @Nullable AttributeSet attributeSet, FontManager fontManager) {
        if (attributeSet != null) {
            TypedArray fontArray = context.obtainStyledAttributes(attributeSet, R.styleable.FontTextView);
            int[] attributesTextStyle = {android.R.attr.textStyle};
            TypedArray textStyleArray = context.obtainStyledAttributes(attributeSet, attributesTextStyle);
            return typefaceFromTypedArrays(fontManager, fontArray, textStyleArray);
        } else {
            int fontWeight = 0;
            int fontWidth = 0;
            int textStyle = Typeface.NORMAL;
            FontFamily fontFamily = fontManager.getDefaultFontFamily();
            return typefaceForTextStyle(fontFamily, fontWeight, fontWidth, textStyle);
        }
    }

    public static Typeface typefaceFromTextAppearance(Context context, int textAppearanceResId, FontManager fontManager) {
        TypedArray fontArray = context.obtainStyledAttributes(textAppearanceResId, R.styleable.FontTextView);
        int[] attributesTextStyle = {android.R.attr.textStyle};
        TypedArray textStyleArray = context.obtainStyledAttributes(textAppearanceResId, attributesTextStyle);
        return typefaceFromTypedArrays(fontManager, fontArray, textStyleArray);
    }

    private static Typeface typefaceFromTypedArrays(FontManager fontManager, TypedArray fontArray, TypedArray textStyleArray) {
        String fontFamilyName = fontArray.getString(R.styleable.FontButton_font_family);
        int fontWeight = fontArray.getInt(R.styleable.FontTextView_font_weight, 0);
        int fontWidth = fontArray.getInt(R.styleable.FontTextView_font_width, 0);
        fontArray.recycle();
        int textStyle = textStyleArray.getInt(0, Typeface.NORMAL);
        textStyleArray.recycle();

        FontFamily fontFamily = fontManager.getFontFamily(fontFamilyName);
        return typefaceForTextStyle(fontFamily, fontWeight, fontWidth, textStyle);
    }


    /**
     *
     * Parses a provided AttributeSet and returns an appropriate TransformationMethod to achieve the specified CapsMode
     *
     * @param context
     * @param attributeSet
     * @return
     */
    public static CapsTransformationMethod capsModeFromAttributeSet(Context context, AttributeSet attributeSet){
        int capsMode = 0;
        if (attributeSet != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.FontTextView);
            capsMode = typedArray.getInt(R.styleable.FontTextView_caps_mode, 0);
            typedArray.recycle();
        }
        return CapsTransformationMethod.values()[capsMode];
    }

    /**
     *
     * Finds the appropriate font within a provided FontFamily, given parameters for weight, width and style.
     * Always takes its slope value from the provided style, and takes its weight from the provided style if no specific weight is given
     *
     * @param family The FontFamily to pull the typeface from
     * @param fontWeight The weight of the font to pull, 0 if not provided
     * @param fontWidth The width of the font to pull, 0 if not provided
     * @param textStyle The style flags (As specified in Typeface) Android natively supports for TextViews
     * @return
     */
    public static Typeface typefaceForTextStyle(FontFamily family, int fontWeight, int fontWidth, int textStyle) {
        int weight;
        int width;
        boolean slope;

        weight = fontWeight != 0 ? fontWeight : isBold(textStyle) ? Weight.BOLD.value : Weight.NORMAL.value;
        width = fontWidth != 0 ? fontWidth : Width.NORMAL.value;
        slope = isItalic(textStyle) ? Slope.ITALIC.value : Slope.NORMAL.value;

        return family.getFont(weight, width, slope).getTypeFace();
    }

    /**
     *
     * Returns a CharSequence that capitalizes letters in the source CharSequence, based on the capsMode provided
     *
     * @param text
     * @param capsMode
     * @return
     */
    public static CharSequence capitalizeCharSequence(CharSequence text, int capsMode) {
        if(capsMode == CAPS_MODE_NONE || text == null || text.length() == 0){
            return text;
        }
        int reqModes = 0;
        switch (capsMode){
            case CAPS_MODE_CHARACTERS:
                reqModes = TextUtils.CAP_MODE_CHARACTERS;
                break;
            case CAPS_MODE_WORDS:
                reqModes = TextUtils.CAP_MODE_WORDS;
                break;
            case CAPS_MODE_SENTENCES:
                reqModes = TextUtils.CAP_MODE_SENTENCES;
                break;
        }
        char[] transformed = new char[text.length()];
        for(int offset = 0; offset < text.length(); offset++){
            if((reqModes & TextUtils.getCapsMode(text, offset, reqModes)) == reqModes
                    || (offset == 0 && (reqModes & TextUtils.CAP_MODE_SENTENCES) == TextUtils.CAP_MODE_SENTENCES )){ // This is required because getCapsMode returns incorrectly on the first character with CAP_MODE_SENTENCES
                transformed[offset] = Character.toUpperCase(text.charAt(offset));
            } else {
                transformed[offset] = text.charAt(offset);
            }
        }
        String s = new String(transformed);
        if (text instanceof Spanned) {
            SpannableString sp = new SpannableString(s);
            TextUtils.copySpansFrom((Spanned) text,
                    0, text.length(), null, sp, 0);
            return sp;
        } else {
            return s;
        }
    }

    public static boolean isBold(int textStyle) {
        return (textStyle & Typeface.BOLD) != 0;
    }

    public static boolean isItalic(int textStyle) {
        return (textStyle & Typeface.ITALIC) != 0;
    }
}
