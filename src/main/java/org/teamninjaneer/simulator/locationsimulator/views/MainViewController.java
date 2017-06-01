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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.DepthTest;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.util.StringConverter;
import javafx.util.converter.FormatStringConverter;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.teamninjaneer.simulator.locationsimulator.FileExporter;
import org.teamninjaneer.simulator.locationsimulator.converter.DoubleStringConverter;
import org.teamninjaneer.simulator.locationsimulator.converter.FunctionConverter;
import org.teamninjaneer.simulator.locationsimulator.converter.TemporalUnitConverter;
import org.teamninjaneer.simulator.locationsimulator.model.LocationDataRow;

public class MainViewController implements Initializable {

    private static final Logger LOGGER = Logger.getGlobal();
    private final static DateTimeFormatter ISO_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final ValidationSupport validationSupport = new ValidationSupport();
    private final LocationDataRow locDataRow = new LocationDataRow();
    private final SimpleStringProperty status = new SimpleStringProperty("Ready");
    private final ObjectProperty<Instant> dtProperty = new SimpleObjectProperty<>();
    private final SimpleDoubleProperty latProperty = new SimpleDoubleProperty(0.0);
    private final SimpleDoubleProperty lonProperty = new SimpleDoubleProperty(0.0);
    private FileExporter exporter = null;
    private EarthViewController earth;

    @FXML
    private DatePicker eventDatePicker;

    @FXML
    private TextField eventTimeTextField;

    @FXML
    private TextField latTextField;

    @FXML
    private TextField lonTextField;

    @FXML
    private ComboBox<Integer> locRateValueComboBox;

    @FXML
    private ChoiceBox<TimeUnit> locRateUomChoiceBox;

    @FXML
    private ComboBox<Integer> newFileRateValueComboBox;

    @FXML
    private ChoiceBox<TimeUnit> newFileRateUomChoiceBox;

    @FXML
    private ComboBox<Double> latDeltaComboBox;

    @FXML
    private ComboBox<Double> lonDeltaComboBox;

    @FXML
    private TextField dataRowFormatTextField;

    @FXML
    private TextField exportDirectoryTextField;

    @FXML
    private TextField fileExtensionTextField;

    @FXML
    private Button exportButton;

    @FXML
    private Button runButton;

    @FXML
    private StackPane earthStackPane;

    @FXML
    private Button browseButton;

    @FXML
    private Label statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        earth = new EarthViewController(earthStackPane);

        eventDatePicker.setValue(LocalDate.now());
        eventDatePicker.setDepthTest(DepthTest.ENABLE);

        dtProperty.addListener(new ChangeListener<Instant>() {
            @Override
            public void changed(ObservableValue<? extends Instant> observable, Instant oldValue, Instant newValue) {
                locDataRow.setDt(newValue);
                // update the form fields for date and time
                eventDatePicker.setValue(LocalDateTime.ofInstant(newValue, ZoneOffset.UTC).toLocalDate());
                eventTimeTextField.setText(LocalDateTime.ofInstant(newValue, ZoneOffset.UTC).toLocalTime().format(ISO_TIME));
            }
        });

        locRateUomChoiceBox.getItems().setAll(TimeUnit.values());
        locRateUomChoiceBox.setValue(TimeUnit.MILLISECONDS);
        locRateUomChoiceBox.getSelectionModel().select(TimeUnit.MILLISECONDS);

        locRateValueComboBox.setItems(FXCollections.<Integer>observableArrayList(1, 2, 5, 10, 20, 30, 40, 50, 100, 200));
        locRateValueComboBox.getSelectionModel().selectFirst();
        locRateValueComboBox.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return String.valueOf(object);
            }

            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.parseInt(string);
                } catch (NumberFormatException e) {
                    // can't parse the string because it's not a number
                    return null;
                }
            }
        });

        newFileRateUomChoiceBox.getItems().setAll(TimeUnit.values());
        newFileRateUomChoiceBox.setValue(TimeUnit.MINUTES);
        newFileRateUomChoiceBox.getSelectionModel().select(TimeUnit.MINUTES);

        newFileRateValueComboBox.setItems(FXCollections.<Integer>observableArrayList(1, 2, 3, 5, 10, 20, 30, 40, 50, 100));
        newFileRateValueComboBox.getSelectionModel().select(1);
        newFileRateValueComboBox.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return String.valueOf(object);
            }

            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.parseInt(string);
                } catch (NumberFormatException e) {
                    // can't parse the string because it's not a number
                    return null;
                }
            }
        });

        latTextField.textProperty().bindBidirectional(latProperty, new DoubleStringConverter());
        lonTextField.textProperty().bindBidirectional(lonProperty, new DoubleStringConverter());

        latProperty.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                earth.movePlacemark((double) newValue, lonProperty.get());
            }
        });

        lonProperty.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                earth.movePlacemark(latProperty.get(), (double) newValue);
            }
        });

        latDeltaComboBox.setItems(FXCollections.<Double>observableArrayList(0.001, 0.01, 0.1, 1.0, 5.0, 10.0, 20.0, 30.0, 40.0, 60.0));
        latDeltaComboBox.getSelectionModel().select(1.0);
        latDeltaComboBox.setConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return String.valueOf(object);
            }

            @Override
            public Double fromString(String string) {
                try {
                    return Double.parseDouble(string);
                } catch (NumberFormatException e) {
                    // can't parse the string because it's not a number
                    return null;
                }
            }
        });

        lonDeltaComboBox.setItems(FXCollections.<Double>observableArrayList(0.001, 0.01, 0.1, 1.0, 5.0, 10.0, 20.0, 30.0, 40.0, 60.0));
        lonDeltaComboBox.getSelectionModel().select(1.0);
        lonDeltaComboBox.setConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return String.valueOf(object);
            }

            @Override
            public Double fromString(String string) {
                try {
                    return Double.parseDouble(string);
                } catch (NumberFormatException e) {
                    // can't parse the string because it's not a number
                    return null;
                }
            }
        });

        browseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory = directoryChooser.showDialog(browseButton.getScene().getWindow());

                if (selectedDirectory == null) {
                    exportDirectoryTextField.setText("no directory selected");
                } else {
                    exportDirectoryTextField.setText(selectedDirectory.getAbsolutePath());
                }
            }
        });

        exportButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateLocDataRow();

                if (exporter == null) {
                    Duration newLocRate = Duration.of(locRateValueComboBox.getValue(),
                            TemporalUnitConverter.convert(locRateUomChoiceBox.getValue()));
                    Duration newFileRate = Duration.of(newFileRateValueComboBox.getValue(),
                            TemporalUnitConverter.convert(newFileRateUomChoiceBox.getValue()));
                    exporter = new FileExporter(locDataRow,
                            fileExtensionTextField.getText(),
                            exportDirectoryTextField.getText(),
                            newLocRate,
                            newFileRate);
                    exporter.getStatusProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            status.setValue(newValue);
                        }

                    });
                    exporter.getLatProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                            latProperty.set((double) newValue);
                        }
                    });
                    exporter.getLonProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                            lonProperty.set((double) newValue);
                        }
                    });
                    exporter.getDtProperty().addListener(new ChangeListener<Instant>() {
                        @Override
                        public void changed(ObservableValue<? extends Instant> observable, Instant oldValue, Instant newValue) {
                            dtProperty.set(newValue);
                        }
                    });
                }
                try {
                    exporter.export();
                } catch (IOException e) {
                    status.set("Failed to export file!");
                    LOGGER.log(Level.SEVERE, "Failed to export file", e);
                }
            }
        });

        runButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateLocDataRow();
            }
        });

        statusLabel.textProperty().bind(status);
        registerValidators();
    }

    /**
     * Add validators to all input controls.
     */
    private void registerValidators() {
        final Validator intValidator = Validator.createPredicateValidator(new Predicate<Object>() {
            @Override
            public boolean test(Object value) {
                if (value == null) {
                    return false;
                }
                String strVal;
                int intVal;
                if (value instanceof String) {
                    strVal = (String) value;
                    strVal = strVal.trim();
                    if (strVal.length() < 1) {
                        return false;
                    }

                    try {
                        intVal = Integer.parseInt(strVal);
                    } catch (NumberFormatException e) {
                        // can't parse the string because it's not a number
                        return false;
                    }
                } else if (value instanceof Integer) {
                    intVal = (int) value;
                } else {
                    return false;
                }

                if (intVal >= 0) {
                    return true;
                } else {
                    return false;
                }
            }

        }, "value must be an integer greater than 0");
        final Validator doubleValidator = Validator.createPredicateValidator(new Predicate<Object>() {
            @Override
            public boolean test(Object value) {
                // If the specified value is null or zero-length, return null
                if (value == null) {
                    return false;
                }
                String strVal;
                double doubleVal;
                if (value instanceof String) {
                    strVal = (String) value;
                    strVal = strVal.trim();
                    if (strVal.length() < 1) {
                        return false;
                    }

                    try {
                        doubleVal = Double.parseDouble(strVal);
                    } catch (NumberFormatException e) {
                        // can't parse the string because it's not a number
                        return false;
                    }
                } else if (value instanceof Double) {
                    doubleVal = (double) value;
                } else {
                    return false;
                }

                if (doubleVal >= 0) {
                    return true;
                } else {
                    return false;
                }
            }

        }, "value must be a numeric value greater than or equal to 0");

        validationSupport.registerValidator(eventDatePicker, true, Validator.createEmptyValidator("a valid date must be provided"));
        validationSupport.registerValidator(eventTimeTextField, true, Validator.createRegexValidator("time must follow the format HH:mm:s", "\\d+:\\d+:\\d+", Severity.ERROR));
        validationSupport.registerValidator(locRateValueComboBox, true, intValidator);
        validationSupport.registerValidator(newFileRateValueComboBox, true, intValidator);
        validationSupport.registerValidator(latDeltaComboBox, true, doubleValidator);
        validationSupport.registerValidator(lonDeltaComboBox, true, doubleValidator);
        validationSupport.registerValidator(latTextField, true, Validator.createRegexValidator(
                "latitude must be given in decimal degrees format between 90 and -90",
                "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$",
                Severity.ERROR));
        validationSupport.registerValidator(lonTextField, true, Validator.createRegexValidator(
                "longitude must be given in decimal degrees format between 180 and -180",
                "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$",
                Severity.ERROR));
        validationSupport.registerValidator(dataRowFormatTextField, true, Validator.createEmptyValidator("data row format must be provided"));
        validationSupport.registerValidator(exportDirectoryTextField, true, Validator.createEmptyValidator("an export directory must be given"));

    }

    /**
     * Update location data row based on form inputs.
     */
    private void updateLocDataRow() {
        if (validationSupport.getValidationResult().getErrors().toArray().length > 0) {
            status.setValue(validationSupport.getValidationResult().getMessages().toString());
            return;
        }
        // update the date time
        String dateString = eventDatePicker.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE);
        dtProperty.set(Instant.parse(dateString + "T" + eventTimeTextField.getText() + "Z"));

        // update lat and lon
        locDataRow.setLat(latProperty.get());
        locDataRow.setLon(lonProperty.get());

        // update the lat and lon deltas
        double latDelta = latDeltaComboBox.getValue();
        locDataRow.setLatDelta(latDelta);
        locDataRow.setLonDelta(lonDeltaComboBox.getValue());

        locDataRow.setPattern(dataRowFormatTextField.getText());
    }
}
