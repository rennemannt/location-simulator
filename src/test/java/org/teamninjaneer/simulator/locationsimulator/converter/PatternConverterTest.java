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
import org.junit.Test;
import static org.junit.Assert.*;
import org.teamninjaneer.simulator.locationsimulator.model.LocationDataRow;

/**
 * Unit tests for Pattern Converter.
 *
 * @author Travis.Rennemann
 */
public class PatternConverterTest {

    private static final String PATTERN_RAND_2PARAM = "dt(yyyy/MM/dd,HH:mm:s.SSS), $lat, $lon, rand(5,40), rand(4,16)\n";
    private static final String PATTERN_RAND_1PARAM = "dt(yyyy/MM/dd,HH:mm:s.SSS), $lat, $lon, rand(50), rand(100)\n";
    private static final String PATTERN_RAND_0PARAM = "dt(yyyy/MM/dd,HH:mm:s.SSS), $lat, $lon, rand(), rand()\n";

    public PatternConverterTest() {
    }

    /**
     * Test of convert method, of class PatternConverter.
     */
    @Test
    public void testConvert() {
        LocationDataRow dataRow = new LocationDataRow(Instant.now(), 28.5, 81.25, 1.0, 1.0, PATTERN_RAND_2PARAM);

        String result = PatternConverter.convert(dataRow);

        System.out.println(result);
        assertNotNull(result);
    }

    /**
     * Test of convert method, of class PatternConverter.
     */
    //@Test
    public void testConvertRand1Param() {
        LocationDataRow dataRow = new LocationDataRow(Instant.now(), 28.5, 81.25, 1.0, 1.0, PATTERN_RAND_1PARAM);

        String result = PatternConverter.convert(dataRow);

        System.out.println(result);
        assertNotNull(result);
    }
    
    /**
     * Test of convert method, of class PatternConverter.
     */
    @Test
    public void testConvertRand0Param() {
        LocationDataRow dataRow = new LocationDataRow(Instant.now(), 28.5, 81.25, 1.0, 1.0, PATTERN_RAND_0PARAM);

        String result = PatternConverter.convert(dataRow);

        System.out.println(result);
        assertNotNull(result);
    }
}
