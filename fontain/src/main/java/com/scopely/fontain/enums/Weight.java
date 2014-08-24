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

package com.scopely.fontain.enums;

/**
 * Standard font weights and their numerical values, on the standard 100-900 scale
 */
public enum Weight {
    HAIRLINE(100),
    THIN(100),
    ULTRA_LIGHT(100),
    EXTRA_LIGHT(100),
    LIGHT(200),
    BOOK(300),
    NORMAL(400),
    MEDIUM(500),
    DEMI_BOLD(600),
    SEMI_BOLD(600),
    BOLD(700),
    EXTRA_BOLD(800),
    HEAVY(800),
    BLACK(800),
    ULTRA_HEAVY(900),
    EXTRA_BLACK(900),
    ULTRA_BLACK(900),
    ULTRA_BOLD(900),
    FAT(900),
    POSTER(900);

    public int value;

    Weight(int weight) {
        this.value = weight;
    }

    public static Weight get(WeightBase base, Modifier modifier) {
        try {
            return valueOf(String.format("%s_%s", modifier, base));
        } catch (IllegalArgumentException e) {
            return valueOf(base.name());
        }
    }
}
