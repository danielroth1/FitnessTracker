package com.fitness.fitness_tracker.views;

import com.fitness.fitness_tracker.models.WorkoutRoutine;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.cglib.core.Local;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public class WorkoutRoutinePopup {

    private Spinner<Integer> repeatSpinner;

    public enum Operation {
        CANCEL, SAVE_OR_CREATE, DELETE
    }

    private Stage stage;
    private Operation chosenOperation;
    private WorkoutRoutine workoutRoutine;
    private TextField textFieldLabel;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private Spinner<Integer> startTimeSpinner;
    private Spinner<Integer> endTimeSpinner;
    private boolean isCreateNew;

    public WorkoutRoutinePopup(WorkoutRoutine workoutRoutine, boolean isCreateNew) {
        this.workoutRoutine = workoutRoutine;
        this.isCreateNew = isCreateNew;
        chosenOperation = Operation.CANCEL;
        initializeStage();
    }

    public WorkoutRoutine getWorkoutRoutine() {
        return workoutRoutine;
    }

    public Operation getChosenOperation() {
        return chosenOperation;
    }

    private void initializeStage() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Workout routine");
        stage.initStyle(StageStyle.UTILITY);

        VBox root = new VBox();
//        VBox.setMargin(root, new Insets(50));
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        // name
        textFieldLabel = new TextField();
        textFieldLabel.setPromptText("Name");
        textFieldLabel.setText(isCreateNew ? "" : workoutRoutine.getName());
        root.getChildren().add(textFieldLabel);

        GridPane gridPane = new GridPane();
        gridPane.getColumnConstraints().add(new ColumnConstraints(40)); // 1st column
        ColumnConstraints columnConstraints = new ColumnConstraints(); // 2nd column
        columnConstraints.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().add(columnConstraints); // 3rd column
        columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().add(columnConstraints);
        gridPane.getRowConstraints().add(new RowConstraints());
        gridPane.getRowConstraints().add(new RowConstraints());
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        root.getChildren().add(gridPane);

        // start date
        LocalDateTime startDateTime = workoutRoutine == null ? LocalDateTime.now() : workoutRoutine.getStartDate();
        LocalDateTime endDateTime = workoutRoutine == null ? LocalDateTime.now() : workoutRoutine.getEndDate();
        Duration repeatDuration = workoutRoutine == null || workoutRoutine.getRepeatDuration() == null ? Duration.of(0, ChronoUnit.MILLIS) : workoutRoutine.getRepeatDuration();

        gridPane.add(new Label("Start"), 0, 0);
        startDatePicker = new DatePicker();
        startDatePicker.setValue(startDateTime.toLocalDate());
        gridPane.add(startDatePicker, 1, 0);
        startTimeSpinner = new Spinner<>(0, 24, startDateTime.toLocalTime().getHour());
        gridPane.add(startTimeSpinner, 2, 0);

        // end date
        gridPane.add(new Label("End"), 0, 1);
        endDatePicker = new DatePicker();
        endDatePicker.setValue(endDateTime.toLocalDate());
        gridPane.add(endDatePicker, 1, 1);
        endTimeSpinner = new Spinner<>(0, 24, endDateTime.toLocalTime().getHour());
        gridPane.add(endTimeSpinner, 2, 1);

        // repeat
        gridPane.add(new Label("Repeat"), 0, 2);
        repeatSpinner = new Spinner<Integer>(0, 999, (int)repeatDuration.toDays());
        gridPane.add(repeatSpinner, 1, 2);
        GridPane.setColumnSpan(repeatSpinner, 2);

        HBox lowerBar = new HBox();
        root.getChildren().add(lowerBar);
        // cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cancelClicked();
            }
        });
        cancelButton.setAlignment(Pos.CENTER);
        lowerBar.getChildren().add(cancelButton);

        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        lowerBar.getChildren().add(region1);

        // delete button (only visible in update mode because when freshly creating routine, nothing can be deleted)
        if (!isCreateNew) {
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    deleteClicked();
                }
            });
            deleteButton.setAlignment(Pos.CENTER);
            lowerBar.getChildren().add(deleteButton);

            Region region2 = new Region();
            HBox.setHgrow(region2, Priority.ALWAYS);
            lowerBar.getChildren().add(region2);
        }

        // save button
        Button saveCreateButton = new Button(isCreateNew ? "Create" : "Save");
        saveCreateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveOrCreateClicked();
            }
        });
        saveCreateButton.setAlignment(Pos.CENTER);
        lowerBar.getChildren().add(saveCreateButton);

        stage.setScene(new Scene(root));
    }

    public void showAndWait() {
        stage.showAndWait();
    }

    private void deleteClicked() {
        chosenOperation = Operation.DELETE;
        stage.close();
    }

    private void cancelClicked() {
        chosenOperation = Operation.CANCEL;
        stage.close();
    }

    private void saveOrCreateClicked() {
        saveOrCreateWorkoutRoutine();
        stage.close();
    }

    private void saveOrCreateWorkoutRoutine() {
        if (workoutRoutine == null) {
            workoutRoutine = new WorkoutRoutine();
        }
        workoutRoutine.setName(textFieldLabel.getText());
        workoutRoutine.setStartDate(LocalDateTime.of(startDatePicker.getValue(), LocalTime.of(startTimeSpinner.getValue(), 0)));
        workoutRoutine.setEndDate(LocalDateTime.of(endDatePicker.getValue(), LocalTime.of(endTimeSpinner.getValue(), 0)));
        workoutRoutine.setRepeatDuration(Duration.of(repeatSpinner.getValue(), ChronoUnit.DAYS));
        chosenOperation = Operation.SAVE_OR_CREATE;
    }

    public static WorkoutRoutinePopup showPopup(WorkoutRoutine workoutRoutine, boolean isCreateNew) {
        WorkoutRoutinePopup popup = new WorkoutRoutinePopup(workoutRoutine, isCreateNew);
        popup.showAndWait();
        return popup;
    }

}
