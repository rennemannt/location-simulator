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
package org.teamninjaneer.simulator.locationsimulator;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Methods for converting the functional definitions in the data row string
 * pattern.
 *
 * @author Travis Rennemann
 */
public class FunctionConverter {

    private final static String UNSUPPORTED_FUNC = "The specified function is not supported!";
    private final static String INSUFFICIENT_PARAMS = "Not enough parameters supplied!";
    private final static String INVALID_PARAMS = "The supplied parameters are invalid for the requested function!";

    // list of available functions to convert: dt = date time; rand = random with lower and upper params
    private static final String[] supportedFunctions = new String[]{"dt", "rand"};

    public static String convert(String func, Object... param) throws UnsupportedOperationException {
        String result = "";

        switch (func.toLowerCase()) {
            case "dt":
                // the date time function requires two parameters
                if (param.length < 2) {
                    throw new UnsupportedOperationException(INSUFFICIENT_PARAMS);
                }
                // verify the parameters are correctly typed for this function
                if (!param[0].getClass().isInstance(String.class)
                        || param[1].getClass().isInstance(Instant.class)) {
                    throw new UnsupportedOperationException(INVALID_PARAMS);
                }
                String dtPattern = (String) param[0];
                Instant dateTime = (Instant) param[1];
                result = convertDt(dtPattern, dateTime);
                break;
            case "rand":
                result = convertRand(param);
                break;
            default:
                throw new UnsupportedOperationException(UNSUPPORTED_FUNC);
        }

        return result;
    }

    private static String convertDt(String dtPattern, Instant dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dtPattern);
        return formatter.format(dateTime);
    }

    private static String convertRand(Object[] param) {
        String result = "";
        if (param.length == 2
                && param[0].getClass().isInstance(int.class)
                && param[1].getClass().isInstance(int.class)) {
            Random rand = new Random((int) param[0]);
            result = String.valueOf(rand.nextInt((int) param[1]));
        } else if (param.length == 1
                && param[0].getClass().isInstance(int.class)) {
            Random rand = new Random((int) param[0]);
            result = String.valueOf(rand.nextInt());
        } else {
            result = String.valueOf(Math.round(Math.random() * 100));
        }
        return result;
    }

    /**
     * Get the list of supported functions.
     *
     * @return
     */
    public static String[] getsupportedFunctions() {
        return supportedFunctions;
    }
}
