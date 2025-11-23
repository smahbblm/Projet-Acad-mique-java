package com.stock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/dashboardResApprov.fxml"));
            primaryStage.setTitle("Dashboard Responsable Appro");
            primaryStage.setScene(new Scene(root, 1200, 800));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace(); // copie le stacktrace dans la console IntelliJ si erreur
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}