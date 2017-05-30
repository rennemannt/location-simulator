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
package org.teamninjaneer.simulator.locationsimulator.views;

import static javafx.animation.Animation.INDEFINITE;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * The Earth map view controller.
 *
 * @author Travis Rennemann <rennemannt@gmail.com>
 */
public class EarthViewController {

    private final StackPane earthStackPane;
    private final Circle placemark = new Circle();
    private final Timeline timeline = new Timeline();
    private final static Paint CIRCLE_COLOR = Paint.valueOf(Color.RED.toString());

    /**
     * Construct controller.
     *
     * @param earthStackPane The stack pane container for the earth map
     */
    public EarthViewController(StackPane earthStackPane) {
        placemark.setRadius(10);
        placemark.setFill(CIRCLE_COLOR);
        earthStackPane.getChildren().add(placemark);
        this.earthStackPane = earthStackPane;

        // set a blinking animation
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, // set start position at 0
                        new KeyValue(placemark.fillProperty(), Color.TRANSPARENT)
                ),
                new KeyFrame(new Duration(1000), // set end position at 4s
                        new KeyValue(placemark.fillProperty(), CIRCLE_COLOR)
                )
        );
        timeline.setAutoReverse(true);
        timeline.cycleCountProperty().setValue(INDEFINITE);
    }

    /**
     * Move the placemark to a specific location on the map.
     *
     * @param lat The latitude
     * @param lon The longitude
     */
    public void movePlacemark(double lat, double lon) {
        final double halfHeight = earthStackPane.getHeight() / 2;
        final double halfWidth = earthStackPane.getWidth() / 2;

        // translate the lat lon coordinate to the stackpane's coordinate system
        double latGridCoord = ((lat / 90.0) * halfHeight) * -1;
        double lonGridCoord = ((lon / 180.0) * halfWidth);

        placemark.setTranslateY(latGridCoord);
        placemark.setTranslateX(lonGridCoord);

        // play animation
        timeline.play();
    }
}
