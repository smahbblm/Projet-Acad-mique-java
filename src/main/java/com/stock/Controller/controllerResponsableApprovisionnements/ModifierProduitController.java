package com.stock.Controller.controllerResponsableApprovisionnements;

import com.stock.model.produit.Produit;
import com.stock.service.ProduitService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ModifierProduitController {

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
    private Button btnModifier;

    private ProduitService produitService;
    private Produit produit;
    private Runnable onProductUpdated;

    @FXML
    public void initialize() {
        try {
            produitService = new ProduitService();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cmbCategorie.getItems().addAll("Visserie", "Joints", "Outils", "Électronique", "Quincaillerie", "Plomberie");
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
        txtDesignation.setText(produit.getDesignation());
        txtReference.setText(produit.getReference());
        txtDescription.setText(produit.getDescription());
        cmbCategorie.setValue(produit.getCategorie());
        txtPrixAchat.setText(String.valueOf(produit.getPrixAchat()));
        txtPrixVente.setText(String.valueOf(produit.getPrixVente()));
        txtStock.setText(String.valueOf(produit.getQuantiteStock()));
        txtSeuilMin.setText(String.valueOf(produit.getSeuilMin()));
        txtSeuilMax.setText(String.valueOf(produit.getSeuilMax()));
    }

    public void setOnProductUpdated(Runnable callback) {
        this.onProductUpdated = callback;
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void modifierProduit() {
        try {
            if (txtDesignation.getText().isEmpty() || txtReference.getText().isEmpty() || 
                cmbCategorie.getValue() == null || txtPrixAchat.getText().isEmpty() || 
                txtPrixVente.getText().isEmpty() || txtStock.getText().isEmpty() || 
                txtSeuilMin.getText().isEmpty() || txtSeuilMax.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs obligatoires");
                return;
            }

            produit.setDesignation(txtDesignation.getText());
            produit.setReference(txtReference.getText());
            produit.setDescription(txtDescription.getText());
            produit.setCategorie(cmbCategorie.getValue());
            produit.setPrixAchat(Float.parseFloat(txtPrixAchat.getText()));
            produit.setPrixVente(Float.parseFloat(txtPrixVente.getText()));
            produit.setQuantiteStock(Integer.parseInt(txtStock.getText()));
            produit.setSeuilMin(Integer.parseInt(txtSeuilMin.getText()));
            produit.setSeuilMax(Integer.parseInt(txtSeuilMax.getText()));

            produitService.modifierProduit(produit);
            if (onProductUpdated != null) {
                onProductUpdated.run();
            }
            showAlert("Succès", "Produit modifié avec succès");
            closeWindow();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs numériques valides");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la modification: " + e.getMessage());
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
