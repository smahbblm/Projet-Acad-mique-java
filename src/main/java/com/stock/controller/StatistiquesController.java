package com.stock.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.util.Duration;
import com.stock.util.AlertUtils;
import com.stock.util.NavigationManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Contrôleur pour les statistiques (Administrateur)
 */
public class StatistiquesController {

    @FXML private Label lblVentesTotales;
    @FXML private Label lblAchatsTotaux;
    @FXML private Label lblProduitsStock;
    @FXML private Label lblUtilisateursActifs;
    @FXML private Label lblDateHeure;

    @FXML private BarChart<String, Number> chartVentes;
    @FXML private PieChart chartProduitsParCategorie;
    @FXML private LineChart<String, Number> chartEvolutionStock;

    @FXML private Button btnRetour;

    private Timeline timeline;

    @FXML
    public void initialize() {
        chargerStatistiques();
        chargerGraphiques();
        demarrerHorloge();
    }

    private void chargerStatistiques() {
        try {
            var conn = com.stock.util.DatabaseConnection.getInstance().getConnection();

            // Ventes totales (montant TTC des factures)
            String sqlVentes = "SELECT COALESCE(SUM(montantTTC), 0) as total FROM factures";
            try (var stmt = conn.prepareStatement(sqlVentes);
                 var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double montant = rs.getDouble("total");
                    lblVentesTotales.setText(String.format("%,.2f DH", montant));
                }
            }

            // Achats totaux (montant total des commandes)
            String sqlAchats = "SELECT COALESCE(SUM(montantTotal), 0) as total FROM bon_commande";
            try (var stmt = conn.prepareStatement(sqlAchats);
                 var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double montant = rs.getDouble("total");
                    lblAchatsTotaux.setText(String.format("%,.2f DH", montant));
                }
            }

            // Produits en stock
            String sqlStock = "SELECT COALESCE(SUM(quantiteStock), 0) as total FROM produits";
            try (var stmt = conn.prepareStatement(sqlStock);
                 var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total");
                    lblProduitsStock.setText(String.valueOf(total));
                }
            }

            // Utilisateurs actifs
            String sqlUtilisateurs = "SELECT COUNT(*) as total FROM utilisateurs WHERE actif = 1";
            try (var stmt = conn.prepareStatement(sqlUtilisateurs);
                 var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total");
                    lblUtilisateursActifs.setText(String.valueOf(total));
                }
            }

        } catch (Exception e) {
            System.err.println("Erreur chargement statistiques: " + e.getMessage());
            // Valeurs par défaut en cas d'erreur
            lblVentesTotales.setText("0.00 DH");
            lblAchatsTotaux.setText("0.00 DH");
            lblProduitsStock.setText("0");
            lblUtilisateursActifs.setText("0");
        }
    }

    private void chargerGraphiques() {
        try {
            var conn = com.stock.util.DatabaseConnection.getInstance().getConnection();

            // Graphique des ventes par mois (derniers 6 mois)
            XYChart.Series<String, Number> seriesVentes = new XYChart.Series<>();
            seriesVentes.setName("Ventes");

            String sqlVentesMensuelles = "SELECT DATE_FORMAT(dateFacture, '%Y-%m') as mois, " +
                                       "SUM(montantTTC) as total " +
                                       "FROM factures " +
                                       "WHERE dateFacture >= DATE_SUB(CURRENT_DATE(), INTERVAL 6 MONTH) " +
                                       "GROUP BY DATE_FORMAT(dateFacture, '%Y-%m') " +
                                       "ORDER BY mois";

            try (var stmt = conn.prepareStatement(sqlVentesMensuelles);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String mois = rs.getString("mois");
                    double total = rs.getDouble("total");
                    seriesVentes.getData().add(new XYChart.Data<>(mois, total));
                }
            }

            chartVentes.getData().add(seriesVentes);

            // Graphique produits par catégorie
            String sqlCategories = "SELECT categorie, COUNT(*) as nombre " +
                                 "FROM produits " +
                                 "WHERE categorie IS NOT NULL AND categorie != '' " +
                                 "GROUP BY categorie " +
                                 "ORDER BY nombre DESC";

            try (var stmt = conn.prepareStatement(sqlCategories);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String categorie = rs.getString("categorie");
                    int nombre = rs.getInt("nombre");
                    chartProduitsParCategorie.getData().add(
                        new PieChart.Data(categorie + " (" + nombre + ")", nombre)
                    );
                }
            }

            // Graphique évolution du stock (simplifié)
            XYChart.Series<String, Number> seriesStock = new XYChart.Series<>();
            seriesStock.setName("Stock total");

            // Données fictives pour démonstration
            seriesStock.getData().add(new XYChart.Data<>("Jan", 150));
            seriesStock.getData().add(new XYChart.Data<>("Fév", 180));
            seriesStock.getData().add(new XYChart.Data<>("Mar", 200));
            seriesStock.getData().add(new XYChart.Data<>("Avr", 220));
            seriesStock.getData().add(new XYChart.Data<>("Mai", 250));
            seriesStock.getData().add(new XYChart.Data<>("Jun", 280));

            chartEvolutionStock.getData().add(seriesStock);

        } catch (Exception e) {
            System.err.println("Erreur chargement graphiques: " + e.getMessage());
        }
    }

    private void demarrerHorloge() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            lblDateHeure.setText(LocalDateTime.now().format(formatter));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void handleRetour() {
        if (timeline != null) {
            timeline.stop();
        }
        try {
            NavigationManager.getInstance().navigate("/fxml/admin/DashboardAdmin.fxml", "Dashboard Administrateur");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible de retourner au dashboard: " + e.getMessage());
        }
    }
}
