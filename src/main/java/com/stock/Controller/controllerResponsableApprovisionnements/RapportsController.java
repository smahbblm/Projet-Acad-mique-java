package com.stock.Controller.controllerResponsableApprovisionnements;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class RapportsController {

    @FXML private Button btnDashboard;
    @FXML private Label lblProduits;
    @FXML private Label lblFournisseurs;
    @FXML private Label lblCommandes;
    @FXML private Label lblStock;
    @FXML private ComboBox<String> reportTypeCombo;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Button btnGenerateReport;
    @FXML private Button btnDownloadPDF;
    @FXML private Label lblTotalCommandes;
    @FXML private Label lblMontantTotal;
    @FXML private Label lblProduitsActifs;
    @FXML private Label lblNbFournisseurs;

    @FXML
    public void initialize() {
        System.out.println("Page Rapports chargée !");
        
        setupNavigation();
        if (reportTypeCombo != null) {
            reportTypeCombo.getItems().addAll(
                "Achats et commandes",
                "Mouvements de stock", 
                "Analyse fournisseurs"
            );
            reportTypeCombo.getSelectionModel().selectFirst();
        }
    }

    private void setupNavigation() {
        if (lblProduits != null) {
            lblProduits.setOnMouseClicked(event -> goToProduits());
        }
        if (lblFournisseurs != null) {
            lblFournisseurs.setOnMouseClicked(event -> goToFournisseurs());
        }
        if (lblCommandes != null) {
            lblCommandes.setOnMouseClicked(event -> goToCommandes());
        }
        if (lblStock != null) {
            lblStock.setOnMouseClicked(event -> goToStock());
        }
    }

    @FXML
    public void goToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/dashboardResApprov.fxml"));
            Stage stage = (Stage) btnDashboard.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToProduits() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/produits.fxml"));
            Stage stage = (Stage) lblProduits.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToFournisseurs() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/fournisseurs.fxml"));
            Stage stage = (Stage) lblFournisseurs.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToCommandes() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/bonCommandes.fxml"));
            Stage stage = (Stage) lblCommandes.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToStock() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/gestionStock.fxml"));
            Stage stage = (Stage) lblStock.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void generateReport() {
        System.out.println("Génération du rapport: " + reportTypeCombo.getValue());
        System.out.println("Période: " + startDatePicker.getValue() + " - " + endDatePicker.getValue());
    }

    @FXML
    public void downloadPDF() {
        System.out.println("Téléchargement PDF du rapport");
    }
}
