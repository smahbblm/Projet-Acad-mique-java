package com.stock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainAppOumnia extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Dashboard actuel
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Dashboard_Administrateur/dashboard.fxml"));
            primaryStage.setTitle("Dashboard Administrateur");
            primaryStage.setScene(new Scene(root, 1200, 800));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
