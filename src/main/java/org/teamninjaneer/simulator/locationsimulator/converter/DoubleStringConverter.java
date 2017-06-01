/*
 * The MIT License
 *
 * Copyright 2017 Team Ninjaneer.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.teamninjaneer.simulator.locationsimulator.converter;

import javafx.util.StringConverter;

/**
 * Handle the conversion of string to double values.
 *
 * @author Travis.Rennemann
 */
public class DoubleStringConverter extends StringConverter<Number> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double fromString(String value) {

        // If the specified value is null or zero-length, return null
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return null;
        }

        double doubleVal;
        try {
            doubleVal = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            // can't parse the string because it's not a number
            return null;
        }
        return doubleVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(Number value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }
        return String.valueOf(value);
    }
}
