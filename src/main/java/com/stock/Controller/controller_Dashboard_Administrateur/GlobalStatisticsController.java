package com.stock.Controller.controller_Dashboard_Administrateur;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.stock.dao.implementation.UtilisateurDAO;
import com.stock.dao.implementation.ProduitDAO;
import com.stock.model.produit.Produit;
import com.stock.dao.implementation.MouvementStockDAO;


public class GlobalStatisticsController {

    // Utilisateurs
    @FXML
    private PieChart pieUsersRoles;
    @FXML
    private PieChart pieUsersStatus;

    // Produits & stock
    @FXML
    private TableView<Produit> tableProduits;
    @FXML
    private TableColumn<Produit, String> colNomProduit;
    @FXML
    private TableColumn<Produit, String> colCategorie;
    @FXML
    private TableColumn<Produit, Integer> colStock;
    @FXML
    private TableColumn<Produit, Double> colValeur;

    @FXML
    private Label labelValeurStock;
    @FXML
    private BarChart<String, Number> barStockProduits;

    // CA et ventes
    @FXML
    private Label labelCA;
    @FXML
    private LineChart<String, Number> lineCA;
    @FXML
    private BarChart<String, Number> barTopVentes;

    // Commandes
    @FXML
    private LineChart<String, Number> lineCommandes;


    @FXML
    public void initialize() {
        loadUserRoles();
        loadUserActivity();

        loadProduitsTable();
        loadProduitsStockChart();
        loadStaticStockValue();

        loadStaticChiffreAffaires();
        loadStaticTopVentes();
        loadStaticCommandes();
    }


    private void loadUserRoles() {
        pieUsersRoles.getData().clear();

        try {
            UtilisateurDAO dao = new UtilisateurDAO();
            var data = dao.countByRole();

            int totalUsers = data.stream()
                    .mapToInt(o -> (int) o[1])
                    .sum();

            for (Object[] row : data) {
                String role = (String) row[0];
                int count = (int) row[1];

                double percent = (count * 100.0) / totalUsers;

                PieChart.Data slice = new PieChart.Data(
                        role + " (" + String.format("%.1f", percent) + "%)",
                        count
                );

                pieUsersRoles.getData().add(slice);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadUserActivity() {
        pieUsersStatus.getData().clear();

        try {
            UtilisateurDAO dao = new UtilisateurDAO();
            var data = dao.countByStatus();

            int total = data.stream()
                    .mapToInt(o -> (int) o[1])
                    .sum();

            for (Object[] row : data) {
                boolean actif = (boolean) row[0];
                int count = (int) row[1];

                double percent = (count * 100.0) / total;

                String label = actif ? "Actifs" : "Inactifs";

                pieUsersStatus.getData().add(
                        new PieChart.Data(
                                label + " (" + String.format("%.1f", percent) + "%)",
                                count
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadProduitsTable() {
        try {
            ProduitDAO dao = new ProduitDAO();
            ObservableList<Produit> produits =
                    FXCollections.observableArrayList(dao.readAll());

            colNomProduit.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(data.getValue().getDesignation()));
            colCategorie.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(data.getValue().getCategorie()));
            colStock.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuantiteStock()).asObject());
            colValeur.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleDoubleProperty(
                            data.getValue().getQuantiteStock() * data.getValue().getPrixVente()
                    ).asObject());

            tableProduits.setItems(produits);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadProduitsStockChart() {
        barStockProduits.getData().clear();

        try {
            ProduitDAO dao = new ProduitDAO();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Stock restant");

            for (Produit p : dao.readAll()) {
                series.getData().add(
                        new XYChart.Data<>(p.getDesignation(), p.getQuantiteStock())
                );
            }

            barStockProduits.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStaticStockValue() {
        try {
            ProduitDAO dao = new ProduitDAO();
            double total = 0;

            for (Produit p : dao.readAll()) {
                total += p.getQuantiteStock() * p.getPrixVente();
            }

            labelValeurStock.setText(
                    "Valeur totale du stock : " + String.format("%.2f", total) + " Dh"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadStaticChiffreAffaires() {
        lineCA.getData().clear();

        try {
            MouvementStockDAO dao = new MouvementStockDAO();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Chiffre d'affaires");

            double totalCA = 0;

            for (Object[] row : dao.chiffreAffairesParMois()) {
                String mois = (String) row[0];
                double montant = (double) row[1];

                totalCA += montant;
                series.getData().add(new XYChart.Data<>(mois, montant));
            }

            labelCA.setText("Chiffre d’affaires total : " + String.format("%.2f", totalCA) + " Dh");
            lineCA.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadStaticTopVentes() {
        barTopVentes.getData().clear();

        try {
            MouvementStockDAO dao = new MouvementStockDAO();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Top ventes");

            for (Object[] row : dao.topVentes()) {
                series.getData().add(
                        new XYChart.Data<>((String) row[0], (int) row[1])
                );
            }

            barTopVentes.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStaticCommandes() {
        lineCommandes.getData().clear();

        try {
            MouvementStockDAO dao = new MouvementStockDAO();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Commandes");

            // récupérer les données par mois
            for (Object[] row : dao.commandesParMois()) {
                String mois = (String) row[0];
                int total = (int) row[1];
                series.getData().add(new XYChart.Data<>(mois, total));
            }

            lineCommandes.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}