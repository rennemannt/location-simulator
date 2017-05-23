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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.teamninjaneer.simulator.locationsimulator.SupportedFunction;
import org.teamninjaneer.simulator.locationsimulator.model.LocationDataRow;

/**
 * Methods for converting the string pattern into location data rows.
 *
 * @author Travis.Rennemann
 */
public class PatternConverter {

    private static final String INSUFFICIENT_PARAMS = "LocationDataRow and its pattern property cannot be null!";
    private static final Pattern FUNC_NAME_PATTERN = Pattern.compile("(\\w+)");
    private static final Pattern RAND_PATTERN = Pattern.compile("(\\brand\\(\\b[^)]+\\))");
    private static final Pattern RAND_PARAM_PATTERN = Pattern.compile("\\brand\\(\\b([^)]+)\\)");
    private static final Pattern DT_PATTERN = Pattern.compile("(\\bdt\\(\\b[^)]+\\))");
    private static final Pattern DT_PARAM_PATTERN = Pattern.compile("\\bdt\\(\\b([^)]+)\\)");
    private static final Map<Pattern, Pattern> REGISTERED_FUNCS = new HashMap<>();

    static {
        // functions must be registered such that the outter function pattern is the Key and the function parameters pattern is the Value
        REGISTERED_FUNCS.put(RAND_PATTERN, RAND_PARAM_PATTERN);
        REGISTERED_FUNCS.put(DT_PATTERN, DT_PARAM_PATTERN);
    }

    /**
     * Convert the location data row into a string where variables and functions
     * are replaced with actual values.
     *
     * @param dataRow The data row to convert into a string
     * @return
     */
    public static String convert(LocationDataRow dataRow) {
        if (dataRow == null || dataRow.getPattern().isEmpty()) {
            throw new UnsupportedOperationException(INSUFFICIENT_PARAMS);
        }
        String converted = dataRow.getPattern();
        converted = converted.replace("$lat", String.valueOf(dataRow.getLat()));
        converted = converted.replace("$lon", String.valueOf(dataRow.getLon()));
        converted = replaceFunctions(converted, dataRow);

        return converted;
    }

    private static String replaceFunctions(String dataRowPattern, LocationDataRow dataRow) {
        String result = dataRowPattern;

        for (Map.Entry<Pattern, Pattern> entry : REGISTERED_FUNCS.entrySet()) {
            Pattern outter = entry.getKey();
            Pattern inner = entry.getValue();

            Matcher matcher = outter.matcher(dataRowPattern);
            boolean foundMatches = matcher.find();
            if (foundMatches && matcher.groupCount() > 0) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    String func = matcher.group(i);
                    Matcher paramMatcher = inner.matcher(func);
                    boolean foundParams = paramMatcher.find();
                    String params = "";
                    if (foundParams) {
                        params = paramMatcher.group(1);
                    }
                    Matcher funcNameMatcher = FUNC_NAME_PATTERN.matcher(func);
                    boolean foundFuncName = funcNameMatcher.find();
                    SupportedFunction sFunc;
                    String funcResult;
                    if (foundFuncName && funcNameMatcher.groupCount() > 0) {
                        sFunc = SupportedFunction.valueOf(funcNameMatcher.group(1).toUpperCase());
                    } else {
                        //TODO: throw error if we can't determine which function to run
                        continue;
                    }
                    switch (sFunc) {
                        case DT:
                            funcResult = FunctionConverter.convertDt(params, dataRow.getDt());
                            break;
                        case RAND:
                            funcResult = FunctionConverter.convertRand(params);
                            break;
                        default:
                            funcResult = "ERROR!";
                    }
                    result = result.replace(func, funcResult);
                }
            }
        }

        return result;
    }
}