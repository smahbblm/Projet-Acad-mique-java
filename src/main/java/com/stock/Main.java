
package com.stock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.Objects;

//avand de crée  la classe qui va hérité de la classe Application on dois crée les  fichiers fxml
public  class Main extends Application  {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        /* scene 1
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/seidbar.fxml"));
        //charger le contenu du fichier dashboard.fxml
        Parent root = loader.load();
        Scene scene = new Scene(root);
        // 2. AJOUTER CETTE LIGNE pour lier le fichier CSS à la scène
        scene.getStylesheets().add(getClass().getResource("/css/gestionchef-de-vents/css-siedbar.css").toExternalForm());

        //les infos sur la fenetre
        stage.setTitle("Tableau de bord des vents");
        stage.setScene(scene);
        stage.show();

         //test du  fichier : dashboard-content.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/Dashboard-content.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        //je dois normalment ajouter la scene crée au stage
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/gestionchef-de-vents/css-dashboard-content.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/main-dashboard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/gestionchef-de-vents/css-dashboard-content.css")).toExternalForm());
        //LIGNE À AJOUTER (pour la sidebar)
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/gestionchef-de-vents/css-siedbar.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/gestionchef-de-vents/navbar.css")).toExternalForm());
        stage.setScene(scene);

       /*
        FXMLLoader adress_monfichier_fxml =  new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/Clients.fxml"));
        Parent  root2 = adress_monfichier_fxml.load();
        Scene  scene_clients =  new Scene(root2);
        scene_clients.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/gestionchef-de-vents/style-navbar-clients.css")).toExternalForm());
        stage.setScene(scene_clients);
         */
        //scene de  Bons de Livraison
        FXMLLoader adress_monfichier_fxml =  new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/BonsLivraison.fxml"));
        Parent  root2 = adress_monfichier_fxml.load();
        Scene  scene_clients =  new Scene(root2);
        scene_clients.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/gestionchef-de-vents/bons-livraison.css")).toExternalForm());
        stage.setScene(scene_clients);
        stage.show();

    }
}
