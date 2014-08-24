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

package com.scopely.fontain.transformationmethods;

import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.view.View;

import static com.scopely.fontain.utils.FontViewUtils.capitalizeCharSequence;


public enum CapsTransformationMethod implements TransformationMethod {
    NONE,
    CHARACTER,
    WORD,
    SENTENCE,
    TITLE //Title is only available in English. In all other languages it will default to WORD
    ;

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return capitalizeCharSequence(source, ordinal());
    }

    @Override
    public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {
        //no op
    }
}
