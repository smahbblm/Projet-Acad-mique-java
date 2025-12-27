
package com.stock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public  class Main_smahan extends Application {
    public static void main(String[] args) {
        Application.launch(Main_smahan.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Gestion de Stock");

        // Créer une session utilisateur fictive pour le test
        com.stock.util.SessionManager sessionManager = com.stock.util.SessionManager.getInstance();
        com.stock.model.utilisateur.ResponsableVentes user = new com.stock.model.utilisateur.ResponsableVentes(
                "Boulmane", "Smahan", "smahan@example.com", "password123"
        );
        sessionManager.createSession(user, "RESPONSABLE_VENTES");
        /*// ========== DASHBOARD  latifa ==========
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Magasinier/DashboardMagasinier.fxml"));


        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Stock Manager");
        stage.show();
    }

}*/




    // ========== DASHBOARD  smahan ==========

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/dashboard/main-dashboard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/CSS/gestionchef-de-vents/css-siedbar.css").toExternalForm());
        stage.setScene(scene);


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
         /*
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
        /*
        //========section de consultation d'historique=========================================

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/consultation/HistoriqueVentes.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        */

        /*//==========================section de consultation d'historiqueVents=========================================
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/consultation/DisponibiliteProduits.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

         */

        stage.show();
    }
}
