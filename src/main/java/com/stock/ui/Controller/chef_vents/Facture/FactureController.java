package com.stock.ui.Controller.chef_vents.Facture;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.List;
import com.stock.Service.FactureServce;
import com.stock.api.ApisFactures;

public class FactureController {

    // les cartes seront affiché dynamiquement et chargement des elements qui existenent en fxml .
    @FXML private Button btnNouvelleFacture;
    @FXML private FlowPane facturesContainer;

    @FXML
    public void initialize() {
        // Cette méthode est appelée automatiquement après le chargement du FXML
        System.out.println("FactureController initialisé !");
        
        // Étape suivante : charger les factures
        loadFacturesFromDatabase();
        System.out.println("la fin d'initialisation ");
    }

    // Méthode pour charger les factures depuis le DAO
    private void loadFacturesFromDatabase() {
        System.out.println("Chargement des factures depuis la base de données");
        FactureServce factsert = new FactureServce();
        //l'appele  de la méthode de la classe service pour récupérer les données
        List<Object> listefactures = factsert.allfacture();
        System.out.println("liste des factures récupérées : " + listefactures.toString());

        // 2. Vider le conteneur avant d'ajouter les nouvelles cartes
        facturesContainer.getChildren().clear();
        
        // 3. Créer une carte pour chaque facture
        //avand la connexion avec le backend j'ai aucun cartes
        for (Object facture : listefactures) {
            System.out.println("création de facture: " + facture);
            VBox carte = createFactureCard(facture);
            facturesContainer.getChildren().add(carte);
        }
    }
    
    // Méthode pour créer une carte de facture
    private VBox createFactureCard(Object facture) {
        System.out.println("je suis dans la méthode qui crée lesrts en sebasant sur les données que j'ai le db");
        VBox carte = new VBox();
        // Définir la taille de la carte
        carte.setPrefSize(280, 200);
        // Définir le style de la carte
        carte.setStyle("-fx-background-color: white; -fx-spacing: 10;");
        
        // Créer les éléments avec les vraies données
        Text titre = new Text("Facture ");
        Text data = new  Text(facture.toString());
        carte.getChildren().addAll(titre, data);
        
        // Appliquer le style bleu à tous les textes
        for (var node : carte.getChildren()) {
            if (node instanceof Text) {
                ((Text) node).setStyle("-fx-fill: blue;");
            }
        }
        return carte;
    }

}


