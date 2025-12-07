
package com.stock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

//avand de crée  la classe qui va hérité de la classe Application on dois crée les  fichiers fxml
public  class Main extends Application  {
    public static void main(String[] args) {
        Application.launch(Main.class,args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Gestion de Stock");
        // Gestion de clients
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/Bons_liv/NouveauBon.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/CSS/gestionchef-de-vents/bons-livraison.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}

