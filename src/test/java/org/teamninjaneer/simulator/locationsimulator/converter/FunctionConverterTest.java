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

/**
 * Unit tests for Function Converter.
 *
 * @author Travis.Rennemann
 */
public class FunctionConverterTest {

    private static final String DT_FORMAT = "yyyy/MM/dd,HH:mm:s.SSSz";
    private static final Long EPOCH_TIME = 231757811001L;

    public FunctionConverterTest() {
    }

    /**
     * Test of convertDt method, of class FunctionConverter.
     */
    @Test
    public void testConvertDt() {
        Instant dateTime = Instant.ofEpochMilli(EPOCH_TIME);

        String expected = "1977/05/06,09:10:11.001UTC";
        String result = FunctionConverter.convertDt(DT_FORMAT, dateTime);

        assertEquals(expected, result);
    }

    /**
     * Test of convertRand method, of class FunctionConverter.
     */
    @Test
    public void testConvertRand() {

        String result = FunctionConverter.convertRand();
        assertNotNull(result);
        System.out.println(result);

        Integer intResult = Integer.valueOf(result);
        assertTrue(intResult > 0);
    }

    /**
     * Test of convertRand method, of class FunctionConverter.
     */
    @Test
    public void testConvertRand_seedAndUpperBound() {

        String result = FunctionConverter.convertRand(10, 100);
        assertNotNull(result);
        System.out.println(result);

        Integer intResult = Integer.valueOf(result);
        assertTrue(intResult > 0 && intResult < 100);
    }
    
    /**
     * Test of convertRand method, of class FunctionConverter.
     */
    @Test
    public void testConvertRand_upperBound() {

        String result = FunctionConverter.convertRand(null, 100);
        assertNotNull(result);
        System.out.println(result);

        Integer intResult = Integer.valueOf(result);
        assertTrue(intResult > 0 && intResult < 100);
    }

    /**
     * Test of convertRand method, of class FunctionConverter.
     */
    @Test
    public void testConvertRand_stringParam() {

        String result = FunctionConverter.convertRand("10, 15");
        assertNotNull(result);
        System.out.println(result);

        Integer intResult = Integer.valueOf(result);
        assertTrue(intResult > 0 && intResult < 15);
    }

}
