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

package com.scopely.fontain.impls;

import android.graphics.Typeface;

import com.scopely.fontain.interfaces.Font;
import com.scopely.fontain.interfaces.FontFamily;

/**
 * An implementation of {@link Font} that holds the typeface and weight, width and slope parameters of a font, as well as a reference to the family it belongs to
 */
public class FontImpl implements Font {
    private Typeface typeface;
    private boolean slope;
    private int weight;
    private int width;
    private FontFamily family;

    public FontImpl(Typeface typeface, int weight, int width, boolean italics) {
        this.family = family;
        this.typeface = typeface;
        this.weight = weight;
        this.width = width;
        this.slope = italics;
    }

    @Override
    public Typeface getTypeFace() {
        return typeface;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public boolean getSlope() {
        return slope;
    }

    @Override
    public FontFamily getFamily() {
        return family;
    }

    public Font setFamily(FontFamily family) {
        this.family = family;
        return this;
    }
}
