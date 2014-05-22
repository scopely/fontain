package com.scopely.fontain.views;

import android.content.Context;
import android.inputmethodservice.ExtractEditText;
import android.util.AttributeSet;

import org.jetbrains.annotations.NotNull;

import static com.scopely.fontain.Fontain.getFontManager;
import static com.scopely.fontain.utils.FontViewUtils.initialize;
import static com.scopely.fontain.utils.FontViewUtils.typefaceFromTextAppearance;

/**
 *
 * Behaves exactly like ExtractEditText, except it provides extra XML attributes to set the CapsMode, FontFamily, Weight and Width (Slope is taken from Android's native Italic support in android.R.attr.textStyle)
 *
 */
public class FontExtractEditText extends ExtractEditText {

    public FontExtractEditText(Context context) {
        super(context);
        initialize(this, context, null, getFontManager());
    }

    public FontExtractEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(this, context, attrs, getFontManager());
    }

    public FontExtractEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(this, context, attrs, getFontManager());
    }

    @Override
    public void setTextAppearance(@NotNull Context context, int resid) {
        super.setTextAppearance(context, resid);
        setTypeface(typefaceFromTextAppearance(context, resid, getFontManager()));
    }
}
