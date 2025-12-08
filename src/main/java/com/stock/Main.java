
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
        
        // ========== DASHBOARD ==========
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/dashboard/main-dashboard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/CSS/gestionchef-de-vents/css-siedbar.css").toExternalForm());
        stage.setScene(scene);

        */
        // ========== CLIENTS ==========
       /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/clients/Clients.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/CSS/gestionchef-de-vents/style-navbar-clients.css").toExternalForm());
        stage.setScene(scene);
        */
        // ========== FORMULAIRE AJOUT CLIENT ==========
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/clients/formr_ajout_client.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        // ========== FACTURES ==========
         /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/factures/Facture.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        */
        // ========== AJOUT FACTURE ==========
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/factures/ajout_facture.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        */

        // ========== BONS DE LIVRAISON (ACTIF) ==========
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/Bons_liv/BonsLivraison.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/CSS/gestionchef-de-vents/bons-livraison.css").toExternalForm());
        stage.setScene(scene);

        // ========== NOUVEAU BON ==========
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/Bons_liv/NouveauBon.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/CSS/gestionchef-de-vents/bons-livraison.css").toExternalForm());
        stage.setScene(scene);
        */
        stage.show();
    }
}
