package com.stock.Controller.controllerResponsableApprovisionnements;

import com.stock.model.produit.Produit;
import com.stock.service.ProduitService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AjouterProduitController {

    @FXML
    private Button btnClose;

    @FXML
    private TextField txtDesignation;

    @FXML
    private TextField txtReference;

    @FXML
    private TextArea txtDescription;

    @FXML
    private ComboBox<String> cmbCategorie;

    @FXML
    private TextField txtPrixAchat;

    @FXML
    private TextField txtPrixVente;

    @FXML
    private TextField txtStock;

    @FXML
    private TextField txtSeuilMin;

    @FXML
    private TextField txtSeuilMax;

    @FXML
    private Button btnAnnuler;

    @FXML
    private Button btnAjouter;

    private ProduitService produitService;
    private Runnable onProductAdded;

    @FXML
    public void initialize() {
        try {
            produitService = new ProduitService();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cmbCategorie.getItems().addAll("Visserie", "Joints", "Outils", "Électronique", "Quincaillerie", "Plomberie");
    }
    
    public void setOnProductAdded(Runnable callback) {
        this.onProductAdded = callback;
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void ajouterProduit() {
        try {
            if (txtDesignation.getText().isEmpty() || txtReference.getText().isEmpty() || 
                cmbCategorie.getValue() == null || txtPrixAchat.getText().isEmpty() || 
                txtPrixVente.getText().isEmpty() || txtStock.getText().isEmpty() || 
                txtSeuilMin.getText().isEmpty() || txtSeuilMax.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs obligatoires");
                return;
            }

            float prixAchat = Float.parseFloat(txtPrixAchat.getText());
            float prixVente = Float.parseFloat(txtPrixVente.getText());
            int stock = Integer.parseInt(txtStock.getText());
            int seuilMin = Integer.parseInt(txtSeuilMin.getText());
            int seuilMax = Integer.parseInt(txtSeuilMax.getText());

            Produit produit = new Produit(
                txtReference.getText(),
                txtDesignation.getText(),
                prixAchat,
                prixVente,
                stock,
                seuilMin,
                seuilMax,
                cmbCategorie.getValue()
            );
            
            if (txtDescription.getText() != null && !txtDescription.getText().isEmpty()) {
                produit.setDescription(txtDescription.getText());
            }

            produitService.ajouterProduit(produit);
            if (onProductAdded != null) {
                onProductAdded.run();
            }
            showAlert("Succès", "Produit ajouté avec succès");
            closeWindow();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs numériques valides");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout du produit: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Succès") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
