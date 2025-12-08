package com.stock.Controller.Dashboard_Administrateur;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GlobalStatisticsController {

    // Utilisateurs
    @FXML private PieChart pieUsersRoles;
    @FXML private PieChart pieUsersStatus;

    // Produits & stock
    @FXML private TableView<Product> tableProduits;
    @FXML private TableColumn<Product, String> colNomProduit;
    @FXML private TableColumn<Product, String> colCategorie;
    @FXML private TableColumn<Product, Integer> colStock;
    @FXML private TableColumn<Product, Double> colValeur;

    @FXML private Label labelValeurStock;
    @FXML private BarChart<String, Number> barStockProduits;

    // CA et ventes
    @FXML private Label labelCA;
    @FXML private LineChart<String, Number> lineCA;
    @FXML private BarChart<String, Number> barTopVentes;

    // Commandes
    @FXML private LineChart<String, Number> lineCommandes;

    /* ================================
              INITIALISATION
       ================================ */

    @FXML
    public void initialize() {
        loadUserRoles();
        loadUserActivity();

        loadProductsTable();
        loadProductsStockChart();
        loadStaticStockValue();

        loadStaticChiffreAffaires();
        loadStaticTopVentes();
        loadStaticCommandes();
    }

    /* ================================
           STATISTIQUES UTILISATEURS
       ================================ */

    private void loadUserRoles() {
        pieUsersRoles.getData().addAll(
                new PieChart.Data("Administrateurs", 2),
                new PieChart.Data("Resp. Approvisionnement", 4),
                new PieChart.Data("Resp. Ventes", 3),
                new PieChart.Data("Magasiniers", 6)
        );
    }

    private void loadUserActivity() {
        pieUsersStatus.getData().addAll(
                new PieChart.Data("Actifs", 12),
                new PieChart.Data("Inactifs", 3)
        );
    }

    /* ================================
               PRODUITS / STOCK
       ================================ */

    private void loadProductsTable() {

        // Colonnes mappées aux données
        colNomProduit.setCellValueFactory(data -> data.getValue().nomProperty());
        colCategorie.setCellValueFactory(data -> data.getValue().categorieProperty());
        colStock.setCellValueFactory(data -> data.getValue().stockProperty().asObject());
        colValeur.setCellValueFactory(data -> data.getValue().valeurProperty().asObject());

        // Liste statique
        ObservableList<Product> produits = FXCollections.observableArrayList(
                new Product("Produit A", "Électronique", 15, 30.0),
                new Product("Produit B", "Bureau", 8, 20.0),
                new Product("Produit C", "Alimentaire", 25, 5.0),
                new Product("Produit D", "Électronique", 10, 120.0),
                new Product("Produit E", "Hygiène", 50, 3.0)
        );

        tableProduits.setItems(produits);
    }

    private void loadProductsStockChart() {
        XYChart.Series<String, Number> s = new XYChart.Series<>();
        s.setName("Stock restant");

        s.getData().add(new XYChart.Data<>("Produit A", 15));
        s.getData().add(new XYChart.Data<>("Produit B", 8));
        s.getData().add(new XYChart.Data<>("Produit C", 25));
        s.getData().add(new XYChart.Data<>("Produit D", 10));
        s.getData().add(new XYChart.Data<>("Produit E", 50));

        barStockProduits.getData().add(s);
    }

    private void loadStaticStockValue() {
        // TOTAL 100% FIXE
        double total = (15 * 30) + (8 * 20) + (25 * 5) + (10 * 120) + (50 * 3);
        labelValeurStock.setText("Valeur totale du stock : " + total + " €");
    }

    /* ================================
                 CHIFFRE D'AFFAIRES
       ================================ */

    private void loadStaticChiffreAffaires() {

        // Valeur totalement statique
        labelCA.setText("Chiffre d’affaires total : 12 500 €");

        XYChart.Series<String, Number> s = new XYChart.Series<>();
        s.setName("Évolution CA");

        s.getData().add(new XYChart.Data<>("Jan", 2000));
        s.getData().add(new XYChart.Data<>("Fév", 1500));
        s.getData().add(new XYChart.Data<>("Mar", 3200));
        s.getData().add(new XYChart.Data<>("Avr", 2800));
        s.getData().add(new XYChart.Data<>("Mai", 3000));

        lineCA.getData().add(s);
    }

    /* ================================
           PRODUITS LES + VENDUS
       ================================ */

    private void loadStaticTopVentes() {
        XYChart.Series<String, Number> s = new XYChart.Series<>();
        s.setName("Top 5 ventes");

        s.getData().add(new XYChart.Data<>("Produit E", 300));
        s.getData().add(new XYChart.Data<>("Produit C", 150));
        s.getData().add(new XYChart.Data<>("Produit A", 120));
        s.getData().add(new XYChart.Data<>("Produit B", 90));
        s.getData().add(new XYChart.Data<>("Produit D", 40));

        barTopVentes.getData().add(s);
    }

    /* ================================
               COMMANDES / JOUR
       ================================ */

    private void loadStaticCommandes() {
        XYChart.Series<String, Number> s = new XYChart.Series<>();
        s.setName("Commandes / jour");

        s.getData().add(new XYChart.Data<>("01/01", 10));
        s.getData().add(new XYChart.Data<>("02/01", 18));
        s.getData().add(new XYChart.Data<>("03/01", 5));
        s.getData().add(new XYChart.Data<>("04/01", 20));
        s.getData().add(new XYChart.Data<>("05/01", 15));

        lineCommandes.getData().add(s);
    }

    /* ================================
                  CLASS PRODUIT
       ================================ */

    public static class Product {

        private final javafx.beans.property.SimpleStringProperty nom;
        private final javafx.beans.property.SimpleStringProperty categorie;
        private final javafx.beans.property.SimpleIntegerProperty stock;
        private final javafx.beans.property.SimpleDoubleProperty valeur;

        public Product(String nom, String categorie, int stock, double prix) {
            this.nom = new javafx.beans.property.SimpleStringProperty(nom);
            this.categorie = new javafx.beans.property.SimpleStringProperty(categorie);
            this.stock = new javafx.beans.property.SimpleIntegerProperty(stock);
            this.valeur = new javafx.beans.property.SimpleDoubleProperty(prix * stock);
        }

        public String getNom() { return nom.get(); }
        public javafx.beans.property.StringProperty nomProperty() { return nom; }

        public String getCategorie() { return categorie.get(); }
        public javafx.beans.property.StringProperty categorieProperty() { return categorie; }

        public int getStock() { return stock.get(); }
        public javafx.beans.property.IntegerProperty stockProperty() { return stock; }

        public double getValeur() { return valeur.get(); }
        public javafx.beans.property.DoubleProperty valeurProperty() { return valeur; }
    }
}
