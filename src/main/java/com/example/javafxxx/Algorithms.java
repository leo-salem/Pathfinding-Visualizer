package com.example.javafxxx;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;

public class Algorithms {
    private final BuildUI ui;

    public Algorithms(BuildUI ui) {
        this.ui = ui;
    }

    /**
     * Breadth-First Search (BFS) Algorithm with Visualization
     */
    public void runBFS(GraphicsContext gc) {
        if (!validateStartAndEnd()) return;

        Task<Void> bfsTask = new Task<>() {
            @Override
            protected Void call() throws InterruptedException {
                boolean[][] visited = new boolean[BuildUI.ROWS][BuildUI.COLS];
                Queue<int[]> queue = new LinkedList<>();
                queue.add(new int[]{ui.startX, ui.startY});
                visited[ui.startY][ui.startX] = true;

                int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
                boolean pathFound = false;

                while (!queue.isEmpty() && !pathFound) {
                    int[] current = queue.poll();
                    int x = current[0];
                    int y = current[1];

                    // Visualize current cell
                    Platform.runLater(() -> {
                        gc.setFill(Color.LIGHTBLUE);
                        gc.fillRect(x * BuildUI.CELL_SIZE, y * BuildUI.CELL_SIZE, BuildUI.CELL_SIZE, BuildUI.CELL_SIZE);
                    });
                    Thread.sleep(100); // Delay for visualization

                    // Check if we've reached the end
                    if (x == ui.endX && y == ui.endY) {
                        pathFound = true;
                        break;
                    }

                    // Explore neighbors
                    for (int[] direction : directions) {
                        int nx = x + direction[0];
                        int ny = y + direction[1];

                        if (isValidCell(nx, ny, visited)) {
                            visited[ny][nx] = true;
                            queue.add(new int[]{nx, ny});
                        }
                    }
                }

                if (pathFound) {
                    Platform.runLater(() -> ui.pathStatus.setText("Path Found (BFS)!"));
                } else {
                    Platform.runLater(() -> ui.pathStatus.setText("No Path Found (BFS)"));
                }
                return null;
            }
        };

        new Thread(bfsTask).start();
    }

    /**
     * Reset Visualization
     */
    public void resetVisualization(GraphicsContext gc) {
        Platform.runLater(() -> {
            gc.clearRect(0, 0, BuildUI.COLS * BuildUI.CELL_SIZE, BuildUI.ROWS * BuildUI.CELL_SIZE);
            ui.drawGrid();
            ui.drawWeights();
        });
    }

    /**
     * Utility Methods
     */
    private boolean validateStartAndEnd() {
        if (ui.startX == -1 || ui.startY == -1) {
            ui.pathStatus.setText("Start cell not set.");
            return false;
        }
        if (ui.endX == -1 || ui.endY == -1) {
            ui.pathStatus.setText("End cell not set.");
            return false;
        }
        return true;
    }

    private boolean isValidCell(int x, int y, boolean[][] visited) {
        return x >= 0 && x < BuildUI.COLS && y >= 0 && y < BuildUI.ROWS &&
                ui.grid[y][x] != 1 && !visited[y][x];
    }

    /**
     * Show Optimal Path After Visualization
     */
    public void showOptimalPath(GraphicsContext gc, List<int[]> path) {
        Task<Void> pathTask = new Task<>() {
            @Override
            protected Void call() throws InterruptedException {
                for (int[] cell : path) {
                    int x = cell[0];
                    int y = cell[1];

                    // Visualize the optimal path cell
                    Platform.runLater(() -> {
                        gc.setFill(Color.YELLOW);
                        gc.fillRect(x * BuildUI.CELL_SIZE, y * BuildUI.CELL_SIZE, BuildUI.CELL_SIZE, BuildUI.CELL_SIZE);
                    });
                    Thread.sleep(100); // Delay for visualization
                }
                return null;
            }
        };
        new Thread(pathTask).start();
    }

    /**
     * Run BFS with Step-by-Step Visualization and Path Display
     */
    public void runBFSWithPath(GraphicsContext gc) {
        if (!validateStartAndEnd()) return;

        Task<Void> bfsTask = new Task<>() {
            @Override
            protected Void call() throws InterruptedException {
                boolean[][] visited = new boolean[BuildUI.ROWS][BuildUI.COLS];
                Queue<int[]> queue = new LinkedList<>();
                Map<int[], int[]> parentMap = new HashMap<>();
                queue.add(new int[]{ui.startX, ui.startY});
                visited[ui.startY][ui.startX] = true;

                int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
                boolean pathFound = false;

                while (!queue.isEmpty() && !pathFound) {
                    int[] current = queue.poll();
                    int x = current[0];
                    int y = current[1];

                    // Visualize current cell
                    Platform.runLater(() -> {
                        gc.setFill(Color.LIGHTBLUE);
                        gc.fillRect(x * BuildUI.CELL_SIZE, y * BuildUI.CELL_SIZE, BuildUI.CELL_SIZE, BuildUI.CELL_SIZE);
                    });
                    Thread.sleep(100); // Delay for visualization

                    // Check if we've reached the end
                    if (x == ui.endX && y == ui.endY) {
                        pathFound = true;
                        break;
                    }

                    // Explore neighbors
                    for (int[] direction : directions) {
                        int nx = x + direction[0];
                        int ny = y + direction[1];

                        if (isValidCell(nx, ny, visited)) {
                            visited[ny][nx] = true;
                            queue.add(new int[]{nx, ny});
                            parentMap.put(new int[]{nx, ny}, current);
                        }
                    }
                }

                if (pathFound) {
                    Platform.runLater(() -> ui.pathStatus.setText("Path Found (BFS)!"));

                    // Reconstruct the path
                    List<int[]> path = new ArrayList<>();
                    int[] current = new int[]{ui.endX, ui.endY};
                    while (current != null) {
                        path.add(0, current);
                        current = parentMap.get(current);
                    }

                    // Reset visualization to initial state
                    resetVisualization(gc);

                    // Visualize the optimal path
                    showOptimalPath(gc, path);
                } else {
                    Platform.runLater(() -> ui.pathStatus.setText("No Path Found (BFS)"));
                }
                return null;
            }
        };

        new Thread(bfsTask).start();
    }

    /**
     * Display Start, End Status and Path Cost
     */
    public void updatePathCostDisplay(List<int[]> path) {
        Platform.runLater(() -> {
            ui.startStatus.setText("Start Cell: (" + ui.startX + ", " + ui.startY + ")");
            ui.endStatus.setText("End Cell: (" + ui.endX + ", " + ui.endY + ")");
            ui.pathCostLabel.setText("Path Cost: " + calculatePathCost(path));
        });
    }

    private int calculatePathCost(List<int[]> path) {
        int cost = 0;
        for (int[] cell : path) {
            cost += ui.weights[cell[1]][cell[0]];
        }
        return cost;
    }

    /**
     * Depth-First Search (DFS) Algorithm with Visualization
     */
    public void runDFS(GraphicsContext gc) {
        if (!validateStartAndEnd()) return;

        Task<Void> dfsTask = new Task<>() {
            @Override
            protected Void call() throws InterruptedException {
                boolean[][] visited = new boolean[BuildUI.ROWS][BuildUI.COLS];
                List<int[]> path = new ArrayList<>();
                boolean pathFound = dfs(ui.startX, ui.startY, visited, path, gc);

                if (pathFound) {
                    Platform.runLater(() -> ui.pathStatus.setText("Path Found (DFS)!"));

                    // Reset visualization to initial state
                    resetVisualization(gc);

                    // Visualize the optimal path
                    showOptimalPath(gc, path);
                } else {
                    Platform.runLater(() -> ui.pathStatus.setText("No Path Found (DFS)"));
                }
                return null;
            }
        };

        new Thread(dfsTask).start();
    }

    /**
     * Recursive DFS Helper
     */
    private boolean dfs(int x, int y, boolean[][] visited, List<int[]> path, GraphicsContext gc) throws InterruptedException {
        if (x == ui.endX && y == ui.endY) {
            path.add(new int[]{x, y});
            return true;
        }

        visited[y][x] = true;

        // Visualize the current cell
        Platform.runLater(() -> {
            gc.setFill(Color.LIGHTBLUE);
            gc.fillRect(x * BuildUI.CELL_SIZE, y * BuildUI.CELL_SIZE, BuildUI.CELL_SIZE, BuildUI.CELL_SIZE);
        });
        Thread.sleep(100); // Delay for visualization

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        for (int[] direction : directions) {
            int nx = x + direction[0];
            int ny = y + direction[1];

            if (isValidCell(nx, ny, visited)) {
                if (dfs(nx, ny, visited, path, gc)) {
                    path.add(0, new int[]{x, y});
                    return true;
                }
            }
        }

        return false;
    }
    public void runDijkstra(GraphicsContext gc) {
        if (!validateStartAndEnd()) return;

        Task<Void> dijkstraTask = new Task<>() {
            @Override
            protected Void call() throws InterruptedException {
                int[][] dist = new int[BuildUI.ROWS][BuildUI.COLS];
                for (int i = 0; i < BuildUI.ROWS; i++) {
                    Arrays.fill(dist[i], Integer.MAX_VALUE);
                }
                dist[ui.startY][ui.startX] = 0;

                PriorityQueue<cellDijkstra> pq = new PriorityQueue<>(Comparator.comparingInt(c -> c.cost));
                pq.add(new cellDijkstra(ui.startX, ui.startY, 0));

                Map<cell, cell> parentMap = new HashMap<>();
                boolean[][] visited = new boolean[BuildUI.ROWS][BuildUI.COLS];

                int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
                boolean pathFound = false;

                while (!pq.isEmpty()) {
                    cellDijkstra current = pq.poll();
                    int x = current.x;
                    int y = current.y;
                    int cost = current.cost;

                    // If the current cell is already visited, skip it
                    if (visited[y][x]) continue;
                    visited[y][x] = true;

                    // Visualize the current cell
                    Platform.runLater(() -> {
                        gc.setFill(Color.LIGHTBLUE);
                        gc.fillRect(x * BuildUI.CELL_SIZE, y * BuildUI.CELL_SIZE, BuildUI.CELL_SIZE, BuildUI.CELL_SIZE);
                    });
                    Thread.sleep(50); // Delay for visualization

                    // If we've reached the end, stop the algorithm
                    if (x == ui.endX && y == ui.endY) {
                        pathFound = true;
                        break;
                    }

                    // Explore neighbors
                    for (int[] direction : directions) {
                        int nx = x + direction[0];
                        int ny = y + direction[1];

                        if (isValidCell(nx, ny, visited)) {
                            int newCost = cost + ui.weights[ny][nx];
                            if (newCost < dist[ny][nx]) {
                                dist[ny][nx] = newCost;
                                pq.add(new cellDijkstra(nx, ny, newCost));
                                parentMap.put(new cell(nx, ny), new cell(x, y));
                            }
                        }
                    }
                }

                if (pathFound) {
                    Platform.runLater(() -> ui.pathStatus.setText("Path Found (Dijkstra)!"));

                    // Reconstruct the path
                    List<int[]> path = new ArrayList<>();
                    cell current = new cell(ui.endX, ui.endY);
                    while (current != null) {
                        path.add(0, new int[]{current.x, current.y});
                        current = parentMap.get(current);
                    }

                    // Reset visualization to initial state
                    resetVisualization(gc);

                    // Visualize the optimal path
                    showOptimalPath(gc, path);
                } else {
                    Platform.runLater(() -> ui.pathStatus.setText("No Path Found (Dijkstra)"));
                }

                return null;
            }
        };

        new Thread(dijkstraTask).start();
    }
}


