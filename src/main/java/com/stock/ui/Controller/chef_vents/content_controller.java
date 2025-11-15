package com.stock.ui.Controller.chef_vents;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class content_controller implements Initializable {
      // je dois obligatoirement définire les composantes  que je dois définir leur logique


      @FXML private Button  aujourdhuit;
      @FXML private Button  semaine;
      @FXML private Button  mois ;
      @FXML private Button annee;
    @FXML private Button nouvelleVente, voirFactures;
    @FXML private Label facturesJour, chiffreAffaires, clientsActifs;

      //maintenant je dois  définir la méthode
      // Méthode appelée au démarrage
      @Override
      public void initialize(URL location, ResourceBundle resources) {
          // Initialiser les données au démarrage
          chargerStatistiques();
      }

    @FXML
    private void nouvelleVente() {
        System.out.println("Nouvelle vente cliquée");
    }

    @FXML
    private void voirFactures() {
        System.out.println("Voir factures cliqué");
    }



    private void chargerStatistiques() {
        // Données simples pour test
        facturesJour.setText("5");
        chiffreAffaires.setText("2,500 €");
        clientsActifs.setText("12");
    }
    // c'est utilisé pour faire colorié le boton  clicqué en blue
      @FXML
      private void selectPeriod(ActionEvent event) {
          // Logique pour changer la couleur
          resetAllButtons();
          Button clickedButton = (Button) event.getSource();
          clickedButton.setStyle("-fx-background-color: blue; -fx-text-fill: white; ");
          // Charger les statistiques pour la période sélectionnée
          chargerStatistiques();
      }
    private void resetAllButtons() {
        aujourdhuit.setStyle("-fx-background-color: black;");
        semaine.setStyle("-fx-background-color: black;");
        mois.setStyle("-fx-background-color: black;");
        annee.setStyle("-fx-background-color: black;");
    }

}
