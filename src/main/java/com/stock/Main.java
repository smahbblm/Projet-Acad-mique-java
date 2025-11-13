package com.stock;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;



public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/Magasinier/DashboardMagasinier.fxml"));



        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard_chef_vets/DashboardChef_vents.fxml"));
        //charger le contenu du fichier dashboard.fxml
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Stock Manager");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
