package com.scopely.fontain.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

import static com.scopely.fontain.Fontain.getFontManager;
import static com.scopely.fontain.utils.FontViewUtils.initialize;

/**
 *
 * Behaves exactly like ToggleButton, except it provides extra XML attributes to set the CapsMode, FontFamily, Weight and Width (Slope is taken from Android's native Italic support in android.R.attr.textStyle)
 *
 */
public class FontToggleButton extends ToggleButton {

    public FontToggleButton(Context context) {
        super(context);
        initialize(this, context, null, getFontManager());
    }

    public FontToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(this, context, attrs, getFontManager());
    }

    public FontToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(this, context, attrs, getFontManager());
    }
}
