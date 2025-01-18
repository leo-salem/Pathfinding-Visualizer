package com.example.javafxxx;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
//gfsdgasg
public class pathfinding extends Application {

    private static final int CELL_SIZE = 15; // Smaller grid cells
    private static final int ROWS = 30;
    private static final int COLS = 50;
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private int[][] grid = new int[ROWS][COLS]; // 0 = empty, 1 = wall
    private int[][] weights = new int[ROWS][COLS]; // For Dijkstra's algorithm

    private int startX = -1, startY = -1, endX = -1, endY = -1;
    private Label pathStatus;
    private Label pathCostLabel;
    private Label startStatus;
    private Label endStatus;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(COLS * CELL_SIZE, ROWS * CELL_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawGrid(gc);

        // Initialize random weights for Dijkstra
        Random rand = new Random();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                weights[i][j] = rand.nextInt(51); // Random weights from 0 to 50
            }
        }
        drawWeights(gc);

        pathStatus = new Label("Path status will appear here");
        pathCostLabel = new Label("Path cost: ");
        startStatus = new Label("Start cell status: ");
        endStatus = new Label("End cell status: ");

        pathStatus.setStyle("-fx-font-size: 16px; -fx-text-fill: blue;");
        pathCostLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: blue;");
        startStatus.setStyle("-fx-font-size: 14px;");
        endStatus.setStyle("-fx-font-size: 14px;");

        // Buttons and Actions
        Button submitStartButton = new Button("Submit Start Cell");
        Button submitEndButton = new Button("Submit Destination Cell");
        Button submitWallButton = new Button("Submit Wall Cell");
        Button bfsButton = new Button("Run BFS");
        Button dfsButton = new Button("Run DFS");
        Button dijkstraButton = new Button("Run Dijkstra");

        submitStartButton.setOnAction(e -> {
            canvas.setOnMouseClicked(event -> {
                startX = (int) (event.getX() / CELL_SIZE);
                startY = (int) (event.getY() / CELL_SIZE);
                gc.setFill(Color.GREEN);
                gc.fillRect(startX * CELL_SIZE, startY * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                startStatus.setText("Start cell: (" + startX + ", " + startY + ") Weight: " + weights[startX][startY]);
            });
        });

        submitEndButton.setOnAction(e -> {
            canvas.setOnMouseClicked(event -> {
                endX = (int) (event.getX() / CELL_SIZE);
                endY = (int) (event.getY() / CELL_SIZE);
                gc.setFill(Color.RED);
                gc.fillRect(endX * CELL_SIZE, endY * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                endStatus.setText("End cell: (" + endX + ", " + endY + ") Weight: " + weights[endX][endY]);
            });
        });

        submitWallButton.setOnAction(e -> {
            canvas.setOnMouseClicked(event -> {
                int wallX = (int) (event.getX() / CELL_SIZE);
                int wallY = (int) (event.getY() / CELL_SIZE);
                grid[wallX][wallY] = 1;
                gc.setFill(Color.BLACK);
                gc.fillRect(wallX * CELL_SIZE, wallY * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            });
        });

        bfsButton.setOnAction(e -> {
            if (startX == -1 || startY == -1 || endX == -1 || endY == -1) {
                pathStatus.setText("Start or end cell not set");
            } else {
                runBFS(gc);
            }
        });

        dfsButton.setOnAction(e -> {
            if (startX == -1 || startY == -1 || endX == -1 || endY == -1) {
                pathStatus.setText("Start or end cell not set");
            } else {
                runDFS(gc);
            }
        });

        dijkstraButton.setOnAction(e -> {
            if (startX == -1 || startY == -1 || endX == -1 || endY == -1) {
                pathStatus.setText("Start or end cell not set");
            } else {
                runDijkstra(gc);
            }
        });

        // Left UI Panel
        VBox leftPanel = new VBox(10, pathStatus, pathCostLabel, startStatus, endStatus, submitStartButton, submitEndButton,
                submitWallButton, bfsButton, dfsButton, dijkstraButton);
        leftPanel.setPadding(new Insets(10));

        // Layout
        HBox layout = new HBox(leftPanel, canvas);
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pathfinding Visualizer");
        primaryStage.show();
    }

    private void drawGrid(GraphicsContext gc) {
        gc.clearRect(0, 0, COLS * CELL_SIZE, ROWS * CELL_SIZE);
        gc.setStroke(Color.GRAY);
        for (int i = 0; i <= ROWS; i++) {
            gc.strokeLine(0, i * CELL_SIZE, COLS * CELL_SIZE, i * CELL_SIZE);
        }
        for (int j = 0; j <= COLS; j++) {
            gc.strokeLine(j * CELL_SIZE, 0, j * CELL_SIZE, ROWS * CELL_SIZE);
        }
    }

    private void drawWeights(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setFont(javafx.scene.text.Font.font(8));
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                gc.fillText(String.valueOf(weights[i][j]), j * CELL_SIZE + 3, i * CELL_SIZE + 12);
            }
        }
    }

    private void highlightPath(GraphicsContext gc, List<int[]> path) {
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        for (int[] cell : path) {
            int x = cell[0];
            int y = cell[1];
            gc.strokeRect(y * CELL_SIZE, x * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    private void runBFS(GraphicsContext gc) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        boolean[][] visited = new boolean[ROWS][COLS];
        Map<int[], int[]> parentMap = new HashMap<>();
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0], y = current[1];

            if (x == endX && y == endY) {
                pathStatus.setText("Great job! Path is found.");
                List<int[]> path = reconstructPath(parentMap, new int[]{endX, endY});
                highlightPath(gc, path);
                return;
            }

            for (int[] dir : DIRECTIONS) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                if (newX >= 0 && newY >= 0 && newX < ROWS && newY < COLS && !visited[newX][newY] && grid[newX][newY] != 1) {
                    visited[newX][newY] = true;
                    parentMap.put(new int[]{newX, newY}, current);
                    queue.add(new int[]{newX, newY});
                }
            }
        }
        pathStatus.setText("Oh no! Path is not found.");
    }

    private List<int[]> reconstructPath(Map<int[], int[]> parentMap, int[] end) {
        List<int[]> path = new ArrayList<>();
        int[] current = end;
        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    private void runDFS(GraphicsContext gc) {
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startX, startY});
        boolean[][] visited = new boolean[ROWS][COLS];
        Map<int[], int[]> parentMap = new HashMap<>();
        visited[startX][startY] = true;

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int x = current[0], y = current[1];

            if (x == endX && y == endY) {
                pathStatus.setText("Great job! Path is found.");
                List<int[]> path = reconstructPath(parentMap, new int[]{endX, endY});
                highlightPath(gc, path);
                return;
            }

            for (int[] dir : DIRECTIONS) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                if (newX >= 0 && newY >= 0 && newX < ROWS && newY < COLS && !visited[newX][newY] && grid[newX][newY] != 1) {
                    visited[newX][newY] = true;
                    parentMap.put(new int[]{newX, newY}, current);
                    stack.push(new int[]{newX, newY});
                }
            }
        }

        pathStatus.setText("Oh no! Path is not found.");
    }

    private void runDijkstra(GraphicsContext gc) {
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
        queue.add(new int[]{startX, startY, 0});
        boolean[][] visited = new boolean[ROWS][COLS];
        int[][] cost = new int[ROWS][COLS];
        for (int[] row : cost) Arrays.fill(row, Integer.MAX_VALUE);
        cost[startX][startY] = 0;
        Map<int[], int[]> parentMap = new HashMap<>();

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0], y = current[1], currentCost = current[2];

            if (visited[x][y]) continue;
            visited[x][y] = true;

            if (x == endX && y == endY) {
                pathStatus.setText("Great job! Path is found.");
                pathCostLabel.setText("Path cost: " + currentCost);
                List<int[]> path = reconstructPath(parentMap, new int[]{endX, endY});
                highlightPath(gc, path);
                return;
            }

            for (int[] dir : DIRECTIONS) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                if (newX >= 0 && newY >= 0 && newX < ROWS && newY < COLS && grid[newX][newY] != 1) {
                    int newCost = currentCost + weights[newX][newY];
                    if (newCost < cost[newX][newY]) {
                        cost[newX][newY] = newCost;
                        queue.add(new int[]{newX, newY, newCost});
                        parentMap.put(new int[]{newX, newY}, current);
                    }
                }
            }
        }
        pathStatus.setText("Oh no! Path is not found.");
        pathCostLabel.setText("Path cost: !");
    }
}
