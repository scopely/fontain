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

import com.scopely.fontain.enums.Slope;
import com.scopely.fontain.enums.Weight;
import com.scopely.fontain.enums.Width;

import java.util.List;

/**
 * A FontFamily is a collection of like Fonts that vary in weight, width and/or slope.
 * It provides methods to find the closest match it has to a given combination of weight, width and slope.
 */
public interface FontFamily {
    public Font getFont(int weight, int width, boolean italic);
    public Font getFont(Weight weight, Width width, Slope slope);
    public List<? extends Font> getFonts();
}
