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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.teamninjaneer.simulator.locationsimulator.converter.PatternConverter;
import org.teamninjaneer.simulator.locationsimulator.model.LocationDataRow;

/**
 * Export file with the given data rows.
 *
 * @author Travis Rennemann <rennemannt@gmail.com>
 */
public class FileExporter {

    private static final Logger LOGGER = Logger.getGlobal();
    private final LocationDataRow locDataRow;
    private final String exportPath;
    private final String fileExt;
    private final Duration newLocRate;
    private final Duration newFileRate;
    private final Timer timer = new Timer();
    private final ObjectProperty<Instant> dtProperty = new SimpleObjectProperty<>();
    private final SimpleDoubleProperty latProperty = new SimpleDoubleProperty(0.0);
    private final SimpleDoubleProperty lonProperty = new SimpleDoubleProperty(0.0);
    private final SimpleStringProperty statusProperty = new SimpleStringProperty("Ready");

    /**
     * Construct file exporter.
     *
     * @param locDataRow The location data row
     * @param exportPath The export file path
     * @param fileExt The file extension for new exported files
     * @param newLocRate The time between consecutive location data rows
     * @param newFileRate The time between new file exports
     */
    public FileExporter(LocationDataRow locDataRow,
            String fileExt,
            String exportPath,
            Duration newLocRate,
            Duration newFileRate) {
        this.locDataRow = locDataRow;
        this.exportPath = exportPath;
        this.fileExt = fileExt;
        this.newLocRate = newLocRate;
        this.newFileRate = newFileRate;
    }

    /**
     * Start the timer process.
     */
    public void start() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    export();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Failed to export file!", e);
                }
            }
        }, 0, newFileRate.toMillis());
    }

    /**
     * Stop the timer process.
     */
    public void stop() {
        timer.cancel();
    }

    /**
     * Export the data rows to a text file.
     *
     * @throws IOException
     */
    public void export() throws IOException {
        final String dataRows = nextLocDataRows();
        String fileName = Instant.now().toString().replace(":", "") + fileExt;
        Files.write(Paths.get(exportPath + "/" + fileName), dataRows.getBytes());
    }

    /**
     * Create the next set of location data rows.
     *
     * @return
     */
    private String nextLocDataRows() {
        StringBuilder dataRows = new StringBuilder();
        long rowCount = Math.floorMod(newFileRate.toMillis(), newLocRate.toMillis());
        for (int i = 0; i < rowCount; i++) {
            // increment values
            locDataRow.setDt(locDataRow.getDt().plusMillis(newLocRate.toMillis()));
            locDataRow.setLat(locDataRow.getLat() + locDataRow.getLatDelta());
            locDataRow.setLon(locDataRow.getLon() + locDataRow.getLonDelta());
            // create a single data row
            String dataRow = PatternConverter.convert(locDataRow);
            dataRows.append(dataRow);
        }
        return dataRows.toString();
    }

    public ObjectProperty<Instant> getDtProperty() {
        return dtProperty;
    }

    public SimpleDoubleProperty getLatProperty() {
        return latProperty;
    }

    public SimpleDoubleProperty getLonProperty() {
        return lonProperty;
    }

    public SimpleStringProperty getStatusProperty() {
        return statusProperty;
    }

}
