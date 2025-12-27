package com.stock.Controller.controllerResponsableApprovisionnements;

import com.stock.service.MouvementStockService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;

public class MouvementsStockController {

    @FXML
    private Label lblDashboard;

    @FXML
    private Label lblProduits;

    @FXML
    private Label lblFournisseurs;

    @FXML
    private Label lblCommandes;

    @FXML
    private Label lblRapports;

    @FXML
    private Button btnInventaire;

    @FXML
    private VBox mouvementsContainer;

    private MouvementStockService mouvementStockService;

    @FXML
    public void initialize() {
        try {
            mouvementStockService = new MouvementStockService();
            loadMouvements();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (lblDashboard != null) {
            lblDashboard.setOnMouseClicked(event -> goToDashboard());
        }

        if (lblProduits != null) {
            lblProduits.setOnMouseClicked(event -> goToProduits());
        }

        if (lblFournisseurs != null) {
            lblFournisseurs.setOnMouseClicked(event -> goToFournisseurs());
        }

        if (lblCommandes != null) {
            lblCommandes.setOnMouseClicked(event -> goToCommandes());
        }

        if (lblRapports != null) {
            lblRapports.setOnMouseClicked(event -> goToRapports());
        }
    }

    private void loadMouvements() {
        try {
            var mouvements = mouvementStockService.consulterTousMouvements();
            mouvementsContainer.getChildren().clear();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            mouvements.forEach(m -> {
                HBox row = new HBox(15);
                row.setStyle("-fx-padding:12; -fx-background-color:#f9f9f9; -fx-background-radius:8;");
                row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                String icon;
                String iconStyle;
                String quantiteColor;
                String quantiteText;

                if (m.getTypeMouvement().equals("ENTREE")) {
                    icon = "⬆";
                    iconStyle = "-fx-background-color:#d4edda; -fx-padding:8; -fx-background-radius:50; -fx-font-size:16;";
                    quantiteColor = "-fx-text-fill:#28a745; -fx-font-weight:bold; -fx-font-size:16;";
                    quantiteText = "+" + m.getQuantite();
                } else {
                    icon = "⬇";
                    iconStyle = "-fx-background-color:#fff3cd; -fx-padding:8; -fx-background-radius:50; -fx-font-size:16;";
                    quantiteColor = "-fx-text-fill:#ffc107; -fx-font-weight:bold; -fx-font-size:16;";
                    quantiteText = "-" + m.getQuantite();
                }

                Label lblIcon = new Label(icon);
                lblIcon.setStyle(iconStyle);

                VBox infoBox = new VBox(4);
                HBox.setHgrow(infoBox, javafx.scene.layout.Priority.ALWAYS);
                Label lblProduit = new Label(m.getProduit().getDesignation());
                lblProduit.setStyle("-fx-font-weight:bold; -fx-font-size:14;");
                Label lblDetails = new Label(m.getReference() + " • " + m.getDateMouvement().atStartOfDay().format(formatter));
                lblDetails.setStyle("-fx-text-fill:#666666; -fx-font-size:12;");
                infoBox.getChildren().addAll(lblProduit, lblDetails);

                VBox quantiteBox = new VBox(2);
                quantiteBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
                Label lblQuantite = new Label(quantiteText);
                lblQuantite.setStyle(quantiteColor);
                Label lblUser = new Label("Admin");
                lblUser.setStyle("-fx-text-fill:#666666; -fx-font-size:11;");
                quantiteBox.getChildren().addAll(lblQuantite, lblUser);

                row.getChildren().addAll(lblIcon, infoBox, quantiteBox);
                mouvementsContainer.getChildren().add(row);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToDashboard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/dashboardResApprov.fxml"));
            Stage stage = (Stage) lblDashboard.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToProduits() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/produits.fxml"));
            Stage stage = (Stage) lblProduits.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToFournisseurs() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/fournisseurs.fxml"));
            Stage stage = (Stage) lblFournisseurs.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToCommandes() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/bonCommandes.fxml"));
            Stage stage = (Stage) lblCommandes.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToInventaire() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/gestionStock.fxml"));
            Stage stage = (Stage) btnInventaire.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToRapports() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/rapports.fxml"));
            Stage stage = (Stage) lblRapports.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
