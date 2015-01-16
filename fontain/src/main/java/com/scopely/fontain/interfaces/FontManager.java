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

package com.scopely.fontain.interfaces;

import android.graphics.Typeface;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.TextView;

import com.android.internal.util.Predicate;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * A FontManager manages all the FontFamilies and associated Fonts available.
 * It provides a default FontFamily and provides methods for accessing any other FontFamily by name.
 * It also has a method to reverse lookup a Font from a Typeface.
 */
public interface FontManager {

    FontFamily getDefaultFontFamily();

    FontFamily getFontFamily(String fontFamilyName);

    Collection<FontFamily> getAllFontFamilies();

    Font getFont(Typeface typeface);

    void applyFontToViewHierarchy(View view, Font font, @Nullable Predicate<TextView> predicate);

    void applyFontFamilyToViewHierarchy(View view, FontFamily family, @Nullable Predicate<TextView> predicate);

    void applyTransformationToViewHierarchy(View view, TransformationMethod method, @Nullable Predicate<TextView> predicate);
}
