package org.teamninjaneer.simulator.locationsimulator.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.teamninjaneer.simulator.locationsimulator.model.TimeUnitOfMeasure;

public class MainSceneController implements Initializable {

    @FXML
    private ComboBox<Integer> locRateValueComboBox;

    @FXML
    private ChoiceBox<TimeUnitOfMeasure> locRateUomChoiceBox;

    @FXML
    private ComboBox<Integer> newFileRateValueComboBox;

    @FXML
    private ChoiceBox<TimeUnitOfMeasure> newFileRateUomChoiceBox;

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
            

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        locRateUomChoiceBox.getItems().setAll(TimeUnitOfMeasure.values());
        locRateUomChoiceBox.setValue(TimeUnitOfMeasure.millisecond);
        locRateUomChoiceBox.getSelectionModel().selectFirst();

        locRateValueComboBox.setItems(FXCollections.observableArrayList(1, 2, 5, 10, 20, 30, 40, 50, 100, 200));
        locRateValueComboBox.getSelectionModel().selectFirst();

        newFileRateUomChoiceBox.getItems().setAll(TimeUnitOfMeasure.values());
        newFileRateUomChoiceBox.setValue(TimeUnitOfMeasure.millisecond);
        newFileRateUomChoiceBox.getSelectionModel().select(TimeUnitOfMeasure.minute);

        newFileRateValueComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 5, 10, 20, 30, 40, 50, 100));
        newFileRateValueComboBox.getSelectionModel().select(1);

        latDeltaComboBox.setItems(FXCollections.observableArrayList(0.001, 0.01, 0.1, 1.0, 5.0, 10.0, 20.0, 30.0, 40.0, 60.0));
        latDeltaComboBox.getSelectionModel().select(1.0);

        lonDeltaComboBox.setItems(FXCollections.observableArrayList(0.001, 0.01, 0.1, 1.0, 5.0, 10.0, 20.0, 30.0, 40.0, 60.0));
        lonDeltaComboBox.getSelectionModel().select(1.0);
    }
}
