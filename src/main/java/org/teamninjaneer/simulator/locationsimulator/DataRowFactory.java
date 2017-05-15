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

/**
 * Methods for creating location data rows.
 *
 * @author Travis Rennemann <rennemannt@gmail.com>
 */
public class DataRowFactory {

    // latitude
    private double lat;

    // longitude
    private double lon;

    // latitudinal delta
    private double latDelta;

    // longitudinal delta
    private double lonDelta;

    // row pattern
    private String pattern;

    /**
     * Construct.
     */
    public DataRowFactory() {
        this.lat = 0;
        this.lon = 0;
        this.latDelta = 1;
        this.lonDelta = 1;
        this.pattern = null;
    }

    /**
     * Construct the Data Row Factory.
     *
     * @param lat The latitude to start at
     * @param lon The longitude to start at
     * @param latDelta The amount of latitudinal change for each iteration
     * @param lonDelta The amount of longitudinal change for each iteration
     * @param pattern The string pattern for each data row
     */
    public DataRowFactory(double lat, double lon, double latDelta, double lonDelta, String pattern) {
        this.lat = lat;
        this.lon = lon;
        this.latDelta = latDelta;
        this.lonDelta = lonDelta;
        this.pattern = pattern;
    }

    /**
     * Create the next data row.
     *
     * @return
     */
    public String next() {
        String row = "";

        return row;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLatDelta() {
        return latDelta;
    }

    public void setLatDelta(double latDelta) {
        this.latDelta = latDelta;
    }

    public double getLonDelta() {
        return lonDelta;
    }

    public void setLonDelta(double lonDelta) {
        this.lonDelta = lonDelta;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
