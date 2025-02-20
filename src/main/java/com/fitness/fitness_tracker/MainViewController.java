package com.fitness.fitness_tracker;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MainViewController {

    @FXML
    protected void onExitButtonClick() {
        Platform.exit();
    }

    @FXML
    public void initialize() {
    }

    public MainViewController() {

    }

    public void onWorkoutRoutinesClick(ActionEvent actionEvent) throws IOException {
        MainApplication.changeScene("workout-routines-view.fxml");
    }
}