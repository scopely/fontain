package com.scopely.fontain.spans;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import com.scopely.fontain.Fontain;
import com.scopely.fontain.enums.Width;
import com.scopely.fontain.interfaces.Font;

/**
 * Part of the With Buddies™ Platform
 * © 2013 Scopely, Inc.
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
        Font font = Fontain.getFont(oldTypeFace);
        Typeface typeface = font.getFamily().getTypeFace(font.getWeight(), width.value, font.getSlope());
        p.setTypeface(typeface);
    }
}
