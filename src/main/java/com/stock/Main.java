package com.stock;


import javafx.application.Application;
import javafx.scene.Parent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Magasinier/DashboardMagasinier.fxml"));


        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Stock Manager");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
