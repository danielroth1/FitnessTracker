package com.fitness.fitness_tracker;

import com.fitness.fitness_tracker.models.WorkoutRoutine;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

/**
 * Used for visualization of single list elements. To see it in use, remove the managed="false" in ListView in workout-routines-view.fxml.
 */
public class WorkoutRoutineCell extends ListCell<WorkoutRoutine> {

    private VBox parent;
    private Label nameLabel;
    private Label timeLabel;

    public WorkoutRoutineCell() {
        nameLabel = new Label();
        timeLabel = new Label();
        parent = new VBox(nameLabel, timeLabel);
    }

    @Override
    protected void updateItem(WorkoutRoutine item, boolean empty) {

        if (empty) {
            nameLabel.setText("");
            timeLabel.setText("");
        }
        else {
            nameLabel.setText(item.getName());
//            timeLabel.setText(Integer.toString(item.getTime()));
        }
        setGraphic(parent);
        super.updateItem(item, empty);
    }
}
