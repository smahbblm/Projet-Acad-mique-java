package com.stock.Controller.controllerResponsableApprovisionnements;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AjouterProduitController {

    @FXML
    private Button btnClose;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtReference;

    @FXML
    private ComboBox<String> cmbCategorie;

    @FXML
    private TextField txtPrix;

    @FXML
    private TextField txtStock;

    @FXML
    private TextField txtSeuil;

    @FXML
    private ComboBox<String> cmbUnite;

    @FXML
    private Button btnAnnuler;

    @FXML
    private Button btnAjouter;

    @FXML
    public void initialize() {
        cmbCategorie.getItems().addAll("Visserie", "Joints", "Outils", "Électronique");
        cmbUnite.getItems().addAll("Pièces", "Kg", "Litres", "Mètres");
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void ajouterProduit() {
        System.out.println("Produit ajouté: " + txtNom.getText());
        closeWindow();
    }
}
