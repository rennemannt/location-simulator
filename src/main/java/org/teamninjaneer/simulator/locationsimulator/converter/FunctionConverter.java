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

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

/**
 * Methods for converting the functional definitions in the data row string
 * pattern.
 *
 * @author Travis Rennemann <rennemannt@gmail.com>
 */
public class FunctionConverter {

    private final static String UNSUPPORTED_FUNC = "The specified function is not supported!";
    private final static String INSUFFICIENT_PARAMS = "Not enough parameters supplied!";
    private final static String INVALID_PARAMS = "The supplied parameters are invalid for the requested function!";

    /**
     * Convert the Instant to the given date time pattern.
     *
     * @see
     * https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
     * @param dtPattern The date time pattern
     * @param dateTime The data time instant
     * @return
     */
    public static String convertDt(String dtPattern, Instant dateTime) throws UnsupportedOperationException {
        // the date time function requires two parameters
        if (dtPattern.isEmpty() || dateTime == null) {
            throw new UnsupportedOperationException(INVALID_PARAMS);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dtPattern)
                .withLocale(Locale.US)
                .withZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC));
        return formatter.format(dateTime);
    }

    /**
     * Generate a random positive integer based on the parameters.
     *
     * @param lowerBound The lower limit (exclusive) value for the random number
     * @param upperBound The upper limit (exclusive) value for the random number
     * @return
     */
    public static String convertRand(Integer lowerBound, Integer upperBound) {
        int intVal;
        Random rand;
        if (lowerBound != null && upperBound != null) {
            rand = new Random();
            intVal = rand.nextInt(upperBound - lowerBound) + lowerBound;
        } else if (upperBound != null) {
            rand = new Random();
            intVal = rand.nextInt(upperBound);
        } else {
            rand = new Random();
            intVal = rand.nextInt();
        }

        return String.valueOf(Math.abs(intVal));
    }

    /**
     * Generate a random positive integer based on the parameters.
     *
     * @return
     */
    public static String convertRand() {
        return convertRand(null, null);
    }

    /**
     * Generate a random positive integer based on the parameters.
     *
     * @param param The string representing the random function parameters
     * @return
     */
    public static String convertRand(String param) {
        if (param.trim().isEmpty()) {
            // get a random number because there are not params
            return convertRand();
        } else if (param.contains(",")) {
            // get a random number between a lower and upper bound
            String[] paramParts = param.split(",");
            if (paramParts.length == 2) {
                Integer seed = Integer.valueOf(paramParts[0].trim());
                Integer upperBound = Integer.valueOf(paramParts[1].trim());
                return convertRand(seed, upperBound);
            }
        } else {
            // get a random number less than the upper bound
            Integer upperBound = Integer.valueOf(param.trim());
            return convertRand(null, upperBound);
        }
        return null;
    }
}
