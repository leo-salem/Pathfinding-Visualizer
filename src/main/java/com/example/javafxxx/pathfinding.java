package com.example.javafxxx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class pathfinding extends Application {

    @Override
    public void start(Stage primaryStage) {
        BuildUI buildUI = new BuildUI(); // Instantiate the BuildUI class
        Algorithms algorithms = new Algorithms(buildUI); // Pass UI instance to Algorithms class

        // Build the main layout (leftPanel and Canvas)
        HBox layout = buildUI.initializeUI(algorithms);

        // Configure the primary stage
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pathfinding Visualizer");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
