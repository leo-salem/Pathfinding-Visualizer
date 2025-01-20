package com.example.javafxxx;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.Random;

public class BuildUI {
    public static final int CELL_SIZE = 15;
    public static final int ROWS = 30;
    public static final int COLS = 50;
    public int[][] grid = new int[ROWS][COLS];
    public int[][] weights = new int[ROWS][COLS];
    public int startX = -1, startY = -1, endX = -1, endY = -1;

    public Label pathStatus, pathCostLabel, startStatus, endStatus;
    public Button submitStartButton, submitEndButton, submitWallButton,stopWallButton, bfsButton, dfsButton, dijkstraButton;
    public Canvas canvas;
    public GraphicsContext gc;

    public BuildUI() {
        // Initialize weights
        Random rand = new Random();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                weights[i][j] = rand.nextInt(51); // Random weights (0 to 50)
            }
        }
    }

    public HBox initializeUI(Algorithms algorithms) {
        canvas = new Canvas(COLS * CELL_SIZE, ROWS * CELL_SIZE);
        gc = canvas.getGraphicsContext2D();
        drawGrid();
        drawWeights();

        // Labels
        pathStatus = new Label("Path status will appear here");
        pathCostLabel = new Label("Path cost: ");
        startStatus = new Label("Start cell status: ");
        endStatus = new Label("End cell status: ");
        styleLabels();

        // Buttons
        submitStartButton = new Button("Submit Start Cell");
        submitEndButton = new Button("Submit Destination Cell");
        submitWallButton = new Button("Submit Wall Cell");
        stopWallButton= new Button("Stop Wall Cell");
        bfsButton = new Button("Run BFS");
        dfsButton = new Button("Run DFS");
        dijkstraButton = new Button("Run Dijkstra");

        // Configure button actions
        configureActions(algorithms);

        // Layout
        VBox leftPanel = new VBox(10, pathStatus, pathCostLabel, startStatus, endStatus,
                submitStartButton, submitEndButton, submitWallButton,stopWallButton, bfsButton, dfsButton, dijkstraButton);
        leftPanel.setPadding(new Insets(10));

        return new HBox(leftPanel, canvas);
    }

    private void styleLabels() {
        pathStatus.setStyle("-fx-font-size: 16px; -fx-text-fill: brown;");
        pathCostLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: brown;");
        startStatus.setStyle("-fx-font-size: 14px;");
        endStatus.setStyle("-fx-font-size: 14px;");
    }

    private void configureActions(Algorithms algorithms) {
        submitStartButton.setOnAction(e -> {
            canvas.setOnMouseClicked(event -> {
                startX = (int) (event.getX() / CELL_SIZE);
                startY = (int) (event.getY() / CELL_SIZE);
                gc.setFill(Color.GREEN);
                gc.fillRect(startX * CELL_SIZE, startY * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                startStatus.setText("Start cell: (" + startX + ", " + startY + ")");
            });
        });

        submitEndButton.setOnAction(e -> {
            canvas.setOnMouseClicked(event -> {
                endX = (int) (event.getX() / CELL_SIZE);
                endY = (int) (event.getY() / CELL_SIZE);
                gc.setFill(Color.RED);
                gc.fillRect(endX * CELL_SIZE, endY * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                endStatus.setText("End cell: (" + endX + ", " + endY + ")");
            });
        });

        submitWallButton.setOnAction(e -> {
            // Set mouse click handler for the canvas
            canvas.setOnMouseClicked(event -> {
                int wallX = (int) (event.getX() / CELL_SIZE);
                int wallY = (int) (event.getY() / CELL_SIZE);

                // Check if the clicked cell is within bounds and not already a wall
                if (wallX >= 0 && wallX < COLS && wallY >= 0 && wallY < ROWS && grid[wallX][wallY] != 1) {
                    grid[wallX][wallY] = 1; // Mark the cell as a wall in the grid
                    gc.setFill(Color.BLACK); // Set the fill color to black for wall cells
                    gc.fillRect(wallX * CELL_SIZE, wallY * CELL_SIZE, CELL_SIZE, CELL_SIZE); // Draw the wall cell
                }
            });
        });

        stopWallButton.setOnAction(e -> {
            canvas.setOnMouseClicked(null); // Disable further marking of wall cells
        });


        bfsButton.setOnAction(e -> algorithms.runBFS(gc));
        dfsButton.setOnAction(e -> algorithms.runDFS(gc));
        dijkstraButton.setOnAction(e -> algorithms.runDijkstra(gc));
    }

    public void drawGrid() {
        gc.setStroke(Color.GRAY);
        for (int i = 0; i <= ROWS; i++) {
            gc.strokeLine(0, i * CELL_SIZE, COLS * CELL_SIZE, i * CELL_SIZE);
        }
        for (int j = 0; j <= COLS; j++) {
            gc.strokeLine(j * CELL_SIZE, 0, j * CELL_SIZE, ROWS * CELL_SIZE);
        }
    }

    public void drawWeights() {
        gc.setFill(Color.BLACK);
        gc.setFont(javafx.scene.text.Font.font(8));
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                gc.fillText(String.valueOf(weights[i][j]), j * CELL_SIZE + 3, i * CELL_SIZE + 12);
            }
        }
    }
}
