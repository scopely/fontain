/*
 * Copyright (c) 2014 Scopely, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scopely.fontain.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import static com.scopely.fontain.Fontain.getFontManager;
import static com.scopely.fontain.utils.FontViewUtils.initialize;

/**
 *
 * Behaves exactly like CompoundButton, except it provides extra XML attributes to set the CapsMode, FontFamily, Weight and Width (Slope is taken from Android's native Italic support in android.R.attr.textStyle)
 *
 */
public class FontCompoundButton extends CompoundButton {

    public FontCompoundButton(Context context) {
        super(context);
        initialize(this, context, null, getFontManager());
    }

    public FontCompoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(this, context, attrs, getFontManager());
    }

    public FontCompoundButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(this, context, attrs, getFontManager());
    }
}
