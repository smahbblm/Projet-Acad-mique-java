package com.stock.Controller.controllerResponsableApprovisionnements;

import com.stock.service.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.time.format.DateTimeFormatter;

public class RapportsController {

    @FXML private Label lblDashboard;
    @FXML private Label lblProduits;
    @FXML private Label lblFournisseurs;
    @FXML private Label lblCommandes;
    @FXML private Label lblStock;

    private ProduitService produitService;
    private FournisseurService fournisseurService;
    private CommandeService commandeService;
    private MouvementStockService mouvementStockService;

    @FXML
    public void initialize() {
        try {
            produitService = new ProduitService();
            fournisseurService = new FournisseurService();
            commandeService = new CommandeService();
            mouvementStockService = new MouvementStockService();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (lblDashboard != null) lblDashboard.setOnMouseClicked(e -> goToDashboard());
        if (lblProduits != null) lblProduits.setOnMouseClicked(e -> goToProduits());
        if (lblFournisseurs != null) lblFournisseurs.setOnMouseClicked(e -> goToFournisseurs());
        if (lblCommandes != null) lblCommandes.setOnMouseClicked(e -> goToCommandes());
        if (lblStock != null) lblStock.setOnMouseClicked(e -> goToStock());
    }

    @FXML
    public void exportProduits() {
        try {
            File file = chooseFile("produits");
            if (file != null) {
                var produits = produitService.consulterTousProduits();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("Référence,Désignation,Catégorie,Prix Achat,Prix Vente,Stock,Seuil Min,Seuil Max\n");
                    for (var p : produits) {
                        writer.write(String.format("%s,%s,%s,%.2f,%.2f,%d,%d,%d\n",
                            p.getReference(), p.getDesignation(), p.getCategorie(),
                            p.getPrixAchat(), p.getPrixVente(), p.getQuantiteStock(),
                            p.getSeuilMin(), p.getSeuilMax()));
                    }
                }
                showSuccess("Rapport produits exporté avec succès");
            }
        } catch (Exception e) {
            showError("Erreur lors de l'export: " + e.getMessage());
        }
    }

    @FXML
    public void exportFournisseurs() {
        try {
            File file = chooseFile("fournisseurs");
            if (file != null) {
                var fournisseurs = fournisseurService.consulterTousFournisseurs();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("Raison Sociale,Adresse,Téléphone,Email,Contact\n");
                    for (var f : fournisseurs) {
                        writer.write(String.format("%s,%s,%s,%s,%s\n",
                            f.getRaisonSociale(), f.getAdresse(), f.getTelephone(),
                            f.getEmail(), f.getContact()));
                    }
                }
                showSuccess("Rapport fournisseurs exporté avec succès");
            }
        } catch (Exception e) {
            showError("Erreur lors de l'export: " + e.getMessage());
        }
    }

    @FXML
    public void exportCommandes() {
        try {
            File file = chooseFile("commandes");
            if (file != null) {
                var commandes = commandeService.consulterTousLesBonsCommande();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("Numéro,Date Commande,Date Livraison,Fournisseur,Montant Total,Statut\n");
                    for (var c : commandes) {
                        writer.write(String.format("%s,%s,%s,%s,%.2f,%s\n",
                            c.getNumero(), c.getDateCommande().format(formatter),
                            c.getDateLivraisonPrevue() != null ? c.getDateLivraisonPrevue().format(formatter) : "",
                            c.getFournisseur().getRaisonSociale(), c.getMontantTotal(), c.getStatut()));
                    }
                }
                showSuccess("Rapport commandes exporté avec succès");
            }
        } catch (Exception e) {
            showError("Erreur lors de l'export: " + e.getMessage());
        }
    }

    @FXML
    public void exportMouvements() {
        try {
            File file = chooseFile("mouvements_stock");
            if (file != null) {
                var mouvements = mouvementStockService.consulterTousMouvements();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("Date,Type,Produit,Référence,Quantité,Stock Avant,Stock Après\n");
                    for (var m : mouvements) {
                        writer.write(String.format("%s,%s,%s,%s,%d,%d,%d\n",
                            m.getDateMouvement().format(formatter), m.getTypeMouvement(),
                            m.getProduit().getDesignation(), m.getReference(),
                            m.getQuantite(), m.getStockAvant(), m.getStockApres()));
                    }
                }
                showSuccess("Rapport mouvements exporté avec succès");
            }
        } catch (Exception e) {
            showError("Erreur lors de l'export: " + e.getMessage());
        }
    }

    private File chooseFile(String defaultName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le rapport");
        fileChooser.setInitialFileName(defaultName + ".csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        return fileChooser.showSaveDialog(lblDashboard.getScene().getWindow());
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void goToDashboard() {
        navigate("/fxml/DashResponsableApprovisionnements/dashboardResApprov.fxml");
    }

    private void goToProduits() {
        navigate("/fxml/DashResponsableApprovisionnements/produits.fxml");
    }

    private void goToFournisseurs() {
        navigate("/fxml/DashResponsableApprovisionnements/fournisseurs.fxml");
    }

    private void goToCommandes() {
        navigate("/fxml/DashResponsableApprovisionnements/bonCommandes.fxml");
    }

    private void goToStock() {
        navigate("/fxml/DashResponsableApprovisionnements/gestionStock.fxml");
    }

    private void navigate(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) lblDashboard.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
