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
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
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
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class MainViewController implements Initializable {

    private final ValidationSupport validationSupport = new ValidationSupport();
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

        locRateUomChoiceBox.getItems().setAll(TimeUnit.values());
        locRateUomChoiceBox.setValue(TimeUnit.MILLISECONDS);
        locRateUomChoiceBox.getSelectionModel().select(TimeUnit.MILLISECONDS);

        locRateValueComboBox.setItems(FXCollections.observableArrayList(1, 2, 5, 10, 20, 30, 40, 50, 100, 200));
        locRateValueComboBox.getSelectionModel().selectFirst();

        newFileRateUomChoiceBox.getItems().setAll(TimeUnit.values());
        newFileRateUomChoiceBox.setValue(TimeUnit.MINUTES);
        newFileRateUomChoiceBox.getSelectionModel().select(TimeUnit.MINUTES);

        newFileRateValueComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 5, 10, 20, 30, 40, 50, 100));
        newFileRateValueComboBox.getSelectionModel().select(1);

        latDeltaComboBox.setItems(FXCollections.observableArrayList(0.001, 0.01, 0.1, 1.0, 5.0, 10.0, 20.0, 30.0, 40.0, 60.0));
        latDeltaComboBox.getSelectionModel().select(1.0);

        lonDeltaComboBox.setItems(FXCollections.observableArrayList(0.001, 0.01, 0.1, 1.0, 5.0, 10.0, 20.0, 30.0, 40.0, 60.0));
        lonDeltaComboBox.getSelectionModel().select(1.0);

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
                earth.movePlacemark(28.0, -81.0);
            }
        });

        statusLabel.setText("Ready");
        registerValidators();
    }

    /**
     * Add validators to all input controls.
     */
    private void registerValidators() {
        final Validator intValidator = Validator.createPredicateValidator(new Predicate<Integer>() {
            @Override
            public boolean test(Integer t) {
                return t > 0;
            }
        }, "value must be an integer greater than 0");

        final Validator doubleValidator = Validator.createPredicateValidator(new Predicate<Double>() {
            @Override
            public boolean test(Double t) {
                return t >= 0.0;
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
}
