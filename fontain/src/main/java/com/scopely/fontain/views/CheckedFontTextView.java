package com.scopely.fontain.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

import static com.scopely.fontain.Fontain.getFontManager;
import static com.scopely.fontain.utils.FontViewUtils.initialize;

/**
 *
 * Behaves exactly like CheckedTextView, except it provides extra XML attributes to set the CapsMode, FontFamily, Weight and Width (Slope is taken from Android's native Italic support in android.R.attr.textStyle)
 *
 */
public class CheckedFontTextView extends CheckedTextView {

    public CheckedFontTextView(Context context) {
        super(context);
        initialize(this, context, null, getFontManager());
    }

    public CheckedFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(this, context, attrs, getFontManager());
    }

    public CheckedFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(this, context, attrs, getFontManager());
    }
}
