package org.teamninjaneer.simulator.locationsimulator.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import org.teamninjaneer.simulator.locationsimulator.model.TimeUnitOfMeasure;

public class MainSceneController implements Initializable {

    @FXML
    private ComboBox<Integer> locRateValueComboBox;

    @FXML
    private ChoiceBox<TimeUnitOfMeasure> locRateUomChoiceBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        locRateUomChoiceBox.getItems().setAll(TimeUnitOfMeasure.values());
        locRateUomChoiceBox.setValue(TimeUnitOfMeasure.millisecond);
        locRateUomChoiceBox.getSelectionModel().selectFirst();

        locRateValueComboBox.setItems(FXCollections.observableArrayList(1, 2, 5, 10, 20, 30, 40, 50, 100, 200));
        locRateValueComboBox.getSelectionModel().selectFirst();
    }
}
