package com.stock.ui.Controller.chef_vents;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.chart.PieChart;

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
      @FXML private PieChart diagrammeCategories;
      @FXML private TableView<ProduitStock> tableStock;
      @FXML private TableColumn<ProduitStock, String> colProduit;
      @FXML private TableColumn<ProduitStock, Integer> colQuantite;
      @FXML private TableColumn<ProduitStock, String> colStatut;


      // Méthode appelée au démarrage
      @Override
      public void initialize(URL location, ResourceBundle resources) {
          // Initialiser les données au démarrage
          //chargerStatistiques();
          chargerDonneesFrontend(); // Commenté temporairement
      }
    @FXML
    private void nouvelleVente() {
        System.out.println("Nouvelle vente cliquée");
    }

    @FXML
    private void voirFactures() {
        System.out.println("Voir factures cliqué");
    }

    // cette méthode à pour but de modifier les valeurs des cartes  mais ça sera faites au moment ou la base est crée
    /*private void chargerStatistiques() {
        // Données simples pour test
        facturesJour.setText("0");
        chiffreAffaires.setText("2,500 €");
        clientsActifs.setText("12");
    }*/
    // c'est utilisé pour faire colorié le boton  clicqué en blue
      @FXML
      private void selectPeriod(ActionEvent event) {
          // lorsque je clic  un botton  touts les autres sont colorié par le blanc  et pres je procède le  traitement du button cliqué
          resetAllButtons();
          Button clickedButton = (Button) event.getSource();
          System.out.println("button cliquée  :"+clickedButton.getId());
          clickedButton.setStyle("-fx-background-color: blue; -fx-text-fill: white; ");
          // Charger les statistiques pour la période sélectionnée
          //chargerStatistiques();
      }
    private void resetAllButtons() {
        aujourdhuit.setStyle("-fx-background-color: white;");
        semaine.setStyle("-fx-background-color: white;");
        mois.setStyle("-fx-background-color: white;");
        annee.setStyle("-fx-background-color: white;");
    }
    
    private void chargerDonneesFrontend() {
        // Vérifier si les composants existent avant de les utiliser
        if (diagrammeCategories != null) {
            // Données de test pour le diagramme
            PieChart.Data slice1 = new PieChart.Data("Électronique", 40);
            PieChart.Data slice2 = new PieChart.Data("Vêtements", 30);
            PieChart.Data slice3 = new PieChart.Data("Alimentaire", 20);
            PieChart.Data slice4 = new PieChart.Data("Autres", 10);
            diagrammeCategories.getData().addAll(slice1, slice2, slice3, slice4);
        }
        
        if (tableStock != null && colProduit != null) {
            // Configuration du tableau
            colProduit.setCellValueFactory(new PropertyValueFactory<>("produit"));
            colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
            colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
            
            // Données de test pour le tableau
            ObservableList<ProduitStock> data = FXCollections.observableArrayList(
                new ProduitStock("Ordinateur", 15, "En stock"),
                new ProduitStock("Téléphone", 8, "Stock faible"),
                new ProduitStock("Tablette", 25, "En stock"),
                new ProduitStock("Casque", 3, "Rupture"),
                new ProduitStock("Souris", 50, "En stock")
            );
            tableStock.setItems(data);
        }
    }
    
    // Classe pour les données du tableau
    public static class ProduitStock {
        private String produit;
        private Integer quantite;
        private String statut;
        
        public ProduitStock(String produit, Integer quantite, String statut) {
            this.produit = produit;
            this.quantite = quantite;
            this.statut = statut;
        }
        
        public String getProduit() { return produit; }
        public Integer getQuantite() { return quantite; }
        public String getStatut() { return statut; }
    }

}
