package com.scopely.fontain.spans;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import com.scopely.fontain.interfaces.Font;

/**
 * A span that sets the spanned text to the provided font
 */
public class FontSpan extends MetricAffectingSpan {
    private Typeface typeface;

    public FontSpan(Font font) {
        this.typeface = font.getTypeFace();
    }

    public FontSpan(Typeface typeface) {
        this.typeface = typeface;
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        apply(p);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        apply(tp);
    }

    private void apply(TextPaint p) {
        p.setTypeface(typeface);
    }
}
