package com.stock.Controller.controllerResponsableApprovisionnements;

import com.stock.model.document.BonCommande;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;

public class DetailsCommandeController {

    @FXML
    private Label lblNumero;
    @FXML
    private Label lblFournisseur;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblStatut;
    @FXML
    private VBox produitsContainer;
    @FXML
    private Label lblMontantTotal;
    @FXML
    private VBox observationsBox;
    @FXML
    private Label lblObservations;

    private BonCommande commande;

    public void setCommande(BonCommande commande) {
        this.commande = commande;
        afficherDetails();
    }

    private void afficherDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        lblNumero.setText("Bon de commande N° " + commande.getNumero());
        lblFournisseur.setText(commande.getFournisseur() != null ? commande.getFournisseur().getRaisonSociale() : "");
        lblDate.setText(commande.getDateCommande().format(formatter));
        lblStatut.setText(commande.getStatut());
        lblMontantTotal.setText(String.format("%.2f €", commande.getMontantTotal()));

        if (commande.getObservations() != null && !commande.getObservations().isEmpty()) {
            lblObservations.setText(commande.getObservations());
        } else {
            observationsBox.setVisible(false);
            observationsBox.setManaged(false);
        }

        produitsContainer.getChildren().clear();
        System.out.println("DEBUG: Nombre de lignes = " + (commande.getLignes() != null ? commande.getLignes().size() : "null"));
        
        if (commande.getLignes() == null || commande.getLignes().isEmpty()) {
            Label lblVide = new Label("Aucun produit dans cette commande");
            lblVide.setStyle("-fx-padding: 20; -fx-text-fill: #999;");
            produitsContainer.getChildren().add(lblVide);
            return;
        }
        
        commande.getLignes().forEach(ligne -> {
            System.out.println("DEBUG: Ajout ligne - " + ligne.getProduit().getDesignation());
            HBox row = new HBox(10);
            row.setStyle("-fx-padding: 12; -fx-border-color: transparent transparent #f0f0f0 transparent;");
            
            Label lblRef = new Label(ligne.getProduit().getReference());
            lblRef.setMinWidth(100);
            lblRef.setPrefWidth(100);
            
            Label lblDesignation = new Label(ligne.getProduit().getDesignation());
            lblDesignation.setMinWidth(200);
            lblDesignation.setPrefWidth(200);
            
            Label lblQuantite = new Label(String.valueOf(ligne.getQuantite()));
            lblQuantite.setMinWidth(80);
            lblQuantite.setPrefWidth(80);
            
            Label lblPrix = new Label(String.format("%.2f €", ligne.getPrixUnitaire()));
            lblPrix.setMinWidth(80);
            lblPrix.setPrefWidth(80);
            
            Label lblSousTotal = new Label(String.format("%.2f €", ligne.getSousTotal()));
            lblSousTotal.setMinWidth(100);
            lblSousTotal.setPrefWidth(100);
            lblSousTotal.setStyle("-fx-font-weight: bold;");
            
            row.getChildren().addAll(lblRef, lblDesignation, lblQuantite, lblPrix, lblSousTotal);
            produitsContainer.getChildren().add(row);
        });
    }

    @FXML
    private void fermer() {
        Stage stage = (Stage) lblNumero.getScene().getWindow();
        stage.close();
    }
}
