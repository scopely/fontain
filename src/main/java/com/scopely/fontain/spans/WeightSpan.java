package com.scopely.fontain.spans;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import com.scopely.fontain.Fontain;
import com.scopely.fontain.enums.Weight;
import com.scopely.fontain.interfaces.Font;

import static com.scopely.fontain.Fontain.getFont;

/**
 * A span that sets the spanned text to the provided weight while maintaining slope and width
 */
public class WeightSpan extends MetricAffectingSpan {
    private Weight weight;

    public WeightSpan(Weight weight) {
       this.weight = weight;
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
        Typeface typeface = font.getFamily().getTypeFace(weight.value, font.getWidth(), font.getSlope());
        p.setTypeface(typeface);
    }
}
