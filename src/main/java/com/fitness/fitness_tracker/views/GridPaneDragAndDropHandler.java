package com.fitness.fitness_tracker.views;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class GridPaneDragAndDropHandler {

    private GridPane gridPane;
    private int rowStart;
    private int columnStart;
    private boolean dragging;
    private List<Node> selectedNodes;

    public GridPaneDragAndDropHandler(GridPane gridPane) {
        this.gridPane = gridPane;
        this.dragging = false;
        this.selectedNodes = new ArrayList<>();
        AddEventHandlers();
    }

    private void dragAndDropStarted(int r, int c) {
        rowStart = r;
        columnStart = c;
        this.dragging = true;
        clearSelectedNodes();
        updateSelectedNodes(r, c);

    }

    private void dragging(int r, int c) {
        if (dragging) {
            updateSelectedNodes(r, c);
        }
    }

    private void dragAndDropFinished(int r, int c) {
        this.dragging = false;
        clearSelectedNodes();
    }

    private void updateSelectedNodes(int row, int column) {
        Node child = getChild(row, column);
        clearSelectedNodes();
        if (child != null) {
            int rStart = rowStart;
            int cStart = columnStart;
            int rEnd = row;
            int cEnd = column;
            if (rStart > rEnd || (rStart == rEnd && cStart > cEnd)) {
                rStart = row;
                cStart = column;
                rEnd = rowStart;
                cEnd = columnStart;
            }

            int rCurrent = rStart;
            int cCurrent = cStart;
            while (true) {
                addSelectedNode(rCurrent, cCurrent);
                if (rCurrent == this.gridPane.getRowCount() &&
                    cCurrent == this.gridPane.getColumnCount() ||
                    rCurrent == rEnd &&
                    cCurrent == cEnd) {
                    break;
                }
                else if (cCurrent == this.gridPane.getColumnCount()) {
                    rCurrent++;
                    cCurrent = 0;
                }
                else {
                    cCurrent++;
                }

            }
//            boolean done = false;
//            for (int r = 0; r < this.gridPane.getRowCount(); r++) {
//                for (int c = 0; c < this.gridPane.getColumnCount(); c++) {
//                    if (r < rowStart || (r == rowStart && c < columnStart))
//                        continue;
//                    addSelectedNode(r, c);
//                    if (c == column && r == row) {
//                        done = true;
//                        break;
//                    }
//                }
//                if (done) {
//                    break;
//                }
//            }
//            boolean success = this.selectedNodes.add(child);
//            if (success) {
//                child.setStyle("-fx-background-color: blue; -fx-border-color: black");
//            }
        }
    }

    private void addSelectedNode(int row, int column) {
        Node child = getChild(row, column);
        if (child != null) {
            boolean success = this.selectedNodes.add(child);
            if (success) {
                child.setStyle("-fx-background-color: lightblue; -fx-border-color: gray");
            }
        }
    }

    private void clearSelectedNodes() {
        for (Node node : this.selectedNodes) {
            node.setStyle("-fx-background-color: white; -fx-border-color: gray");
        }
        this.selectedNodes.clear();
    }

    private Node getChild(int r, int c) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == c && GridPane.getRowIndex(node) == r) {
                return node;
            }
        }
        return null;
    }

    private void AddEventHandlers() {
        GridPane grid = this.gridPane;
        grid.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                int c = (int)Math.floor(grid.getColumnCount() * e.getX() / grid.getWidth());
                int r = (int)Math.floor(grid.getRowCount() * e.getY() / grid.getHeight());
                dragAndDropStarted(r, c);
                System.out.printf("Mouse pressed cell [%d, %d]%n", c, r);
            }
        });
        grid.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                int c = (int)Math.floor(grid.getColumnCount() * e.getX() / grid.getWidth());
                int r = (int)Math.floor(grid.getRowCount() * e.getY() / grid.getHeight());
                dragging(r, c);
            }
        });
        grid.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                int c = (int)Math.floor(grid.getColumnCount() * e.getX() / grid.getWidth());
                int r = (int)Math.floor(grid.getRowCount() * e.getY() / grid.getHeight());
                dragAndDropFinished(r, c);
                System.out.printf("Mouse released cell [%d, %d]%n", c, r);
            }
        });
    }
}
