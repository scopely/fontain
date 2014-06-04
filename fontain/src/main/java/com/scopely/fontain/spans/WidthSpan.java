package com.scopely.fontain.spans;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import com.scopely.fontain.enums.Width;
import com.scopely.fontain.interfaces.Font;

import static com.scopely.fontain.Fontain.getFont;

/**
 * A span that sets the spanned text to the provided width while maintaining weight and slope
 */
public class WidthSpan extends MetricAffectingSpan {
    private Width width;

    public WidthSpan(Width width) {
        this.width = width;
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
        Typeface oldTypeFace = p.getTypeface();
        Font font = getFont(oldTypeFace);
        Typeface typeface = font.getFamily().getFont(font.getWeight(), width.value, font.getSlope()).getTypeFace();
        p.setTypeface(typeface);
    }
}
