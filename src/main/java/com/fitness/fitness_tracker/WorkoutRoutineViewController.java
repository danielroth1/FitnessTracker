package com.fitness.fitness_tracker;

import com.fitness.fitness_tracker.models.WorkoutRoutine;
import com.fitness.fitness_tracker.services.WorkoutRoutineService;
import com.fitness.fitness_tracker.views.GridPaneDragAndDropHandler;
import com.fitness.fitness_tracker.views.WorkoutRoutinePopup;
import jakarta.persistence.TypedQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.OverrunStyle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class WorkoutRoutineViewController {

    private final WorkoutRoutineService workoutRoutineService;

    public ListView<WorkoutRoutine> listView;
    public ObservableList<WorkoutRoutine> workoutRoutines;
    public GridPane gridView;
    public Label currentMonthLabel;

    private LocalDate selectedMonth;

    private int numRows = 0;
    private int numColumns = 0;
    private List<LocalDate> gridDates = new ArrayList<>();

    public WorkoutRoutineViewController() {
        workoutRoutineService = MainApplication.APPLICATION_CONTEXT.getBean(WorkoutRoutineService.class);
    }

    @FXML
    public void initialize() {
        selectedMonth = LocalDate.now().withDayOfMonth(1);
        workoutRoutines = FXCollections.observableArrayList();
        listView.setCellFactory(lv -> new WorkoutRoutineCell());
        listView.setItems(workoutRoutines);
        update();
    }

    private void update() {
        if (workoutRoutineService == null)
            return;
        workoutRoutines.clear();
        workoutRoutines.addAll(this.workoutRoutineService.getWorkoutRoutines());

        initializeCalendar();
    }

    private void initializeCalendar() {

//        GridPaneDragAndDropHandler dragAndDropHandler = new GridPaneDragAndDropHandler(this.gridView);

        List<WorkoutRoutine> workoutRoutines = workoutRoutineService.getWorkoutRoutines();

        updateCalendar();
    }

    private LocalDate getGridDate(int r, int c) {
        return gridDates.get(c + r * numColumns);
    }

    private void updateCalendar() {
        currentMonthLabel.setText(selectedMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));

        // go back to last monday
        LocalDate dateCurrent = selectedMonth;
        while (dateCurrent.getDayOfWeek().getValue() != 1) {
            dateCurrent = dateCurrent.minusDays(1);
        }

        LocalDate dateEnd = selectedMonth.plusMonths(1);
        while (dateEnd.getDayOfWeek().getValue() != 1) {
            dateEnd = dateEnd.plusDays(1);
        }

        gridView.getChildren().clear();
        int counter = 0;
        numRows = 0;
        numColumns = 7;
        gridDates = new ArrayList<>();
        LocalDate currentDay = LocalDate.now();
        while (!dateCurrent.isEqual(dateEnd)) {
            int row = (int)Math.floor(counter / (double)numColumns);
            int column = counter % numColumns; // dateCurrent.getDayOfWeek().getValue();
            Pane pane = AddGridElement(dateCurrent, row, column);
            String backgroundColor = "white";
            String borderColor = "gray";
            int borderWidth = 1;
            if (dateCurrent.isEqual(currentDay)) {
                borderColor = "red";
                borderWidth = 4;
            }
            if (dateCurrent.getMonth() != selectedMonth.getMonth()) {
                backgroundColor = "#c3c4c7";
            }
            String style = "-fx-background-color: %s".formatted(backgroundColor);
            if (!borderColor.equals("none")) {
                style += "; -fx-border-color: " + borderColor + "; -fx-border-width: " + borderWidth;
            }
            pane.setStyle(style);

            gridDates.add(dateCurrent);
            dateCurrent = dateCurrent.plusDays(1);
            counter++;
            numRows = Math.max(numRows, row);
        }
        numRows++;

        // rows and columns
        gridView.getRowConstraints().clear();
        for (int r = 0; r < numRows; r++) {
            RowConstraints rowConstraints = new RowConstraints(-1, -1, -1, Priority.ALWAYS, VPos.CENTER, true);
            rowConstraints.setPercentHeight(100.0 / numRows);
            gridView.getRowConstraints().add(rowConstraints);
        }
        gridView.getColumnConstraints().clear();
        for (int c = 0; c < numColumns; c++) {
            ColumnConstraints columnConstraints = new ColumnConstraints(-1, -1, -1, Priority.ALWAYS, HPos.CENTER, true);
            columnConstraints.setPercentWidth(100.0 / numColumns);
            gridView.getColumnConstraints().add(columnConstraints);
        }
    }

    private Pane AddGridElement(LocalDate date, int row, int column) {

        // root
        HBox rootH = new HBox();
        VBox rootV = new VBox(rootH);
        HBox.setHgrow(rootV, Priority.ALWAYS);
        VBox root = new VBox(rootV);
        VBox.setVgrow(root, Priority.ALWAYS);
        root.setStyle("-fx-background-color: white; -fx-border-color: gray");
        gridView.add(root, column, row);

        // title
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd EEE MMM");
        String title = date.format(formatter);
        Label titleLabel = new Label(title);
        VBox.setMargin(titleLabel, new Insets(5, 5, 5, 5));
        VBox titleControl = new VBox();
        titleControl.getChildren().add(titleLabel);
        titleControl.setAlignment(Pos.TOP_CENTER);
        root.getChildren().add(titleControl);

        // routines
        VBox routinesBox = new VBox();
        root.getChildren().add(routinesBox);
        VBox.setVgrow(routinesBox, Priority.ALWAYS);
        List<Color> listOfColors = Arrays.asList(
            Color.RED.desaturate(),
            Color.GREEN.desaturate(),
            Color.BLUE.desaturate(),
            Color.MAGENTA.desaturate(),
            Color.CYAN.desaturate(),
            Color.YELLOW.desaturate(),
            Color.ORANGE.desaturate(),
            Color.PINK.desaturate());

        int counter = 0;
        List<WorkoutRoutine> routines = workoutRoutineService.getWorkoutRoutines();
        routines.sort(new Comparator<WorkoutRoutine>() {
            @Override
            public int compare(WorkoutRoutine o1, WorkoutRoutine o2) {
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });
        for (WorkoutRoutine workoutRoutine : routines) {
            LocalDateTime t1 = date.atStartOfDay();
            LocalDateTime t2 = date.plusDays(1).atStartOfDay();

            LocalDateTime r1 = workoutRoutine.getStartDate();
            LocalDateTime r2 = workoutRoutine.getEndDate();

            long rd = workoutRoutine.getRepeatDuration().toMillis();
            boolean isInside;
            if (rd != 0) {
                isInside = false;
                while(r1.compareTo(date.plusDays(1).atStartOfDay()) <= 0 && !isInside) {
                    isInside = r1.compareTo(t2) < 0 && r2.compareTo(t1) >= 0;
                    r1 = r1.plus(workoutRoutine.getRepeatDuration());
                    r2 = r2.plus(workoutRoutine.getRepeatDuration());
                }
                r1 = r1.minus(workoutRoutine.getRepeatDuration());
                r2 = r2.minus(workoutRoutine.getRepeatDuration());
            }
            else {
                isInside = r1.compareTo(t2) < 0 && r2.compareTo(t1) >= 0;
            }

            if (isInside) {
                VBox routineBox = new VBox();
                String style = "-fx-background-color: #%s".formatted(listOfColors.get(counter % listOfColors.size()).toString().toLowerCase().substring(2));
                routineBox.setStyle(style);

                String routineContent = null;
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                if (r1.toLocalDate().compareTo(date) == 0 && r2.toLocalDate().compareTo(date) == 0) {
                    // date == start date == end date
                    routineContent = r1.format(timeFormatter) + " - " + r2.format(timeFormatter);
                }
                else if (r1.toLocalDate().compareTo(date) == 0) {
                    // date == start date
                    routineContent = "Start: " + r1.format(timeFormatter);
                }
                else if (r2.toLocalDate().compareTo(date) == 0) {
                    // date == end date
                    routineContent = "End: " + r2.format(timeFormatter);
                }

                String routineTitle = (workoutRoutine.getName() == null ? "" : workoutRoutine.getName()) + (routineContent == null ? "" : "\n(" + routineContent + ")");
                Label routineTitleLabel = new Label(routineTitle);
                routineTitleLabel.setAlignment(Pos.TOP_CENTER);
                routineTitleLabel.setTextFill(Color.WHITE);
                routineTitleLabel.setTextOverrun(OverrunStyle.CLIP);
                routineTitleLabel.setTextAlignment(TextAlignment.CENTER);
                routineBox.getChildren().add(routineTitleLabel);
                routineBox.setAlignment(Pos.TOP_CENTER);

                // h box
                HBox routineHBox = new HBox();
                routineHBox.getChildren().add(routineBox);
                HBox.setHgrow(routineBox, Priority.ALWAYS);
                VBox.setMargin(routineHBox, new Insets(2));

                routineHBox.setOnMouseReleased(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        showWorkoutRoutinePopup(workoutRoutine, false);
                        event.consume(); // stop event from propagating to root.setOnMouseReleased
                    }
                });

                routinesBox.getChildren().add(routineHBox);
//                VBox.setVgrow(routineHBox, Priority.ALWAYS); // Uncomment if boxes should fill each day.
            }
            counter++;
        }

        // click on empty space
        root.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                WorkoutRoutine workoutRoutine = new WorkoutRoutine();
                workoutRoutine.setStartDate(date.atStartOfDay());
                workoutRoutine.setEndDate(date.atStartOfDay());
                showWorkoutRoutinePopup(workoutRoutine, true);
            }
        });

        return root;
    }

    public void OnBackButtonClick(ActionEvent actionEvent) throws IOException {
        MainApplication.changeScene("main-view.fxml");
    }

    private void showWorkoutRoutinePopup(WorkoutRoutine workoutRoutine, boolean isCreateNew) {
        WorkoutRoutinePopup popup = WorkoutRoutinePopup.showPopup(workoutRoutine, isCreateNew);
        workoutRoutine = popup.getWorkoutRoutine();
        switch (popup.getChosenOperation()) {
            case CANCEL -> { // do nothing
                break;
            }
            case DELETE -> {
                workoutRoutineService.removeWorkoutRoutine(workoutRoutine);
                update();
                break;
            }
            case SAVE_OR_CREATE -> {
                workoutRoutineService.addOrUpdateWorkoutRoutine(workoutRoutine);
                update();
                break;
            }
        }
    }

    public void onPreviousMonthButtonClicked(ActionEvent actionEvent) {
        selectedMonth = selectedMonth.minusMonths(1);
        updateCalendar();
    }

    public void onNextMonthButtonClicked(ActionEvent actionEvent) {
        selectedMonth = selectedMonth.plusMonths(1);
        updateCalendar();
    }

    /**
     * THIS METHOD IS NOT USED:
     * Add one mouse listener to the global pane. Not used as it is better to add a mouse listener to each grid element
     * on its own.
     */
    private void setMouseListeners(Pane pane) {
        pane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                Node source = (Node)e.getSource() ;
                Integer colIndex = GridPane.getColumnIndex(source);
                Integer rowIndex = GridPane.getRowIndex(source);
                System.out.printf("Mouse entered cell [%d, %d]%n", colIndex, rowIndex);
            }
        });
        pane.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                Node source = (Node)e.getSource() ;
                Integer colIndex = GridPane.getColumnIndex(source);
                Integer rowIndex = GridPane.getRowIndex(source);
                System.out.printf("Mouse release cell [%d, %d]%n", colIndex, rowIndex);

                WorkoutRoutine workoutRoutine = new WorkoutRoutine();
                LocalDate date = getGridDate(rowIndex, colIndex);
                workoutRoutine.setStartDate(date.atStartOfDay());
                workoutRoutine.setEndDate(date.atStartOfDay());
                showWorkoutRoutinePopup(workoutRoutine, true);
            }
        });
    }
}
