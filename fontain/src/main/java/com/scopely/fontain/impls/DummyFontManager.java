package com.scopely.fontain.impls;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scopely.fontain.R;
import com.scopely.fontain.enums.Slope;
import com.scopely.fontain.enums.Weight;
import com.scopely.fontain.enums.Width;
import com.scopely.fontain.interfaces.Font;
import com.scopely.fontain.interfaces.FontFamily;
import com.scopely.fontain.interfaces.FontManager;
import com.scopely.fontain.utils.FontViewUtils;

import static com.scopely.fontain.impls.FontManagerImpl.*;

/**
 * A "dumb" implementation of {@link com.scopely.fontain.interfaces.FontManager} that only provides the system default fonts.
 * Used as a fallback when a real FontManager hasn't been initialized.
 */
public class DummyFontManager implements FontManager {

    FontFamily systemDefault;

    public DummyFontManager() {
        systemDefault = initSystemDefaultFamily();
    }

    @Override
    public FontFamily getDefaultFontFamily() {
        return systemDefault;
    }

    @Override
    public FontFamily getFontFamily(String fontFamilyName) {
        return systemDefault;
    }

    @Override
    public Font getFont(Typeface typeface) {
        for(Font font : systemDefault.getFonts()) {
            if (font.getTypeFace().equals(typeface)) {
                return font;
            }
        }
        return systemDefault.getFont(Weight.NORMAL, Width.NORMAL, Slope.NORMAL);
    }

    @Override
    public void applyFontToViewHierarchy(View view, Font font) {
        if(view instanceof ViewGroup){
            for(int i = 0; i < ((ViewGroup) view).getChildCount(); i++){
                applyFontToViewHierarchy(((ViewGroup) view).getChildAt(i), font);
            }
        } else if(view instanceof TextView) {
            FontViewUtils.ensureTags((TextView) view, this);
            ((TextView) view).setTypeface(font.getTypeFace());
        }
    }

    @Override
    public void applyFontFamilyToViewHierarchy(View view, FontFamily family) {
        if(view instanceof ViewGroup){
            for(int i = 0; i < ((ViewGroup) view).getChildCount(); i++){
                applyFontFamilyToViewHierarchy(((ViewGroup) view).getChildAt(i), family);
            }
        } else if(view instanceof TextView) {
            TextView textView = (TextView) view;

            FontViewUtils.ensureTags(textView, this);

            Integer weight = (Integer) textView.getTag(R.id.fontain_tag_weight);
            Integer width = (Integer) textView.getTag(R.id.fontain_tag_width);
            Boolean slope = (Boolean) textView.getTag(R.id.fontain_tag_slope);

            Font newFont = family.getFont(weight, width, slope);
            textView.setTag(R.id.fontain_tag_font, newFont);
            textView.setTypeface(newFont.getTypeFace());
        }
    }
}
