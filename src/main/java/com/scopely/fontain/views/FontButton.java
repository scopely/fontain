package com.scopely.fontain.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.scopely.fontain.Fontain;
import com.scopely.fontain.utils.FontViewUtils;

/**
 *
 * Behaves exactly like Button, except it provides extra XML attributes to set the CapsMode, FontFamily, Weight and Width (Slope is taken from Android's native Italic support in android.R.attr.textStyle)
 *
 */
public class FontButton extends Button {

    public FontButton(Context context) {
        super(context);
        FontViewUtils.initialize(this, context, null, Fontain.getFontManager());
    }

    public FontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        FontViewUtils.initialize(this, context, null, Fontain.getFontManager());
    }

    public FontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        FontViewUtils.initialize(this, context, null, Fontain.getFontManager());
    }
}
