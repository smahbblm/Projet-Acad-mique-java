
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
        //  vue d'ensemble

        // sidbardashoard et le conetenu  de chaque section
        //trouver l'emplacement de  fichier xmls
        stage.setTitle("Gestion de chef de vents");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/dashboard/seidbar.fxml"));
        //charger le contenu du fichier dashboard.fxml
        Parent root = loader.load();
        Scene scene_sidbar = new Scene(root);
        //JOUTER CETTE LIGNE pour lier le fichier CSS à la scène
        scene_sidbar.getStylesheets().add(getClass().getResource("/css/gestionchef-de-vents/css-siedbar.css").toExternalForm());
        stage.setScene(scene_sidbar);
        // fichier xml de contenu de dashboard
        FXMLLoader cont = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/dashboard/Dashboard-content.fxml"));
        Parent root1 = cont.load();
        Scene scene_content = new Scene(root1);
        scene_content.getStylesheets().add(getClass().getResource("/css/gestionchef-de-vents/css-dashboard-content.css").toExternalForm());
        stage.setScene(scene_content);
        // la scene qui contient de sidbar et le contenu de chaque section
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/dashboard/main-dashboard.fxml"));
        Parent root2 = loader1.load();
        Scene scene = new Scene(root2);
        stage.setScene(scene);


        // Gestion
        // scene de contenu  gestion de clients
        FXMLLoader path_fenetre_clients = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/clients/Clients.fxml"));
        Parent  lirefeneFxml_clients = path_fenetre_clients.load();
        Scene s_client= new Scene(lirefeneFxml_clients);
        s_client.getStylesheets().add(getClass().getResource("/css/gestionchef-de-vents/navbar_fenetre_clients.css").toExternalForm());
        //System.out.println("je suis au moment de configuration de scene");
        stage.setScene(s_client);
        stage.show();

        /* la scene de gestions de factures
        System.out.println("je suis au moment de configuration de scene");
        FXMLLoader adress = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/factures/Facture.fxml"));
        Parent lireFXML = adress.load();
        Scene scene_facture = new Scene(lireFXML);
        stage.setScene(scene_facture) ;
         stage.show();


        scene.getStylesheets().add(getClass().getResource("/css/gestionchef-de-vents/css-dashboard-content.css").toExternalForm());
        //LIGNE À AJOUTER (pour la sidebar)
        scene.getStylesheets().add(getClass().getResource("/css/gestionchef-de-vents/css-siedbar.css").toExternalForm());

        stage.setScene(scene);


        /*
        FXMLLoader adress_monfichier_fxml =  new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/Clients.fxml"));
        Parent  root2 = adress_monfichier_fxml.load();
        Scene  scene_clients =  new Scene(root2);
        scene_clients.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/gestionchef-de-vents/style-navbar-clients.css")).toExternalForm());
        stage.setScene(scene_clients);

        //scene de formulaire d'ajout  de bon par le chef de vent
        FXMLLoader adress_monfichier_fxml = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/NouveauBon.fxml"));
        Parent root2 = adress_monfichier_fxml.load();
        Scene scene_bons = new Scene(root2);
        stage.setScene(scene_bons);
        //scene de  Bons de Livraison
        FXMLLoader adres_monfichier_fxml =  new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/BonsLivraison.fxml"));
        Parent  root = adres_monfichier_fxml.load();
        Scene  scene_clients =  new Scene(root);
        scene_clients.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/gestionchef-de-vents/bons-livraison.css")).toExternalForm());
        stage.setScene(scene_clients);*/
        //stage.show();

    }
}

