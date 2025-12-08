package com.stock;

import javafx.application.Application;
import javafx.stage.Stage;
import com.stock.util.NavigationManager;

/**
 * Classe principale de l'application
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        NavigationManager manager = NavigationManager.getInstance();
        manager.setStage(primaryStage);

        primaryStage.setTitle("Système de Gestion de Stock");
        primaryStage.setWidth(1024);
        primaryStage.setHeight(768);

        manager.navigate("/fxml/Login.fxml", "Connexion");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

