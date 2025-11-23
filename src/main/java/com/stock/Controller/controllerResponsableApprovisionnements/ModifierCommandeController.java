package com.stock.Controller.controllerResponsableApprovisionnements;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ModifierCommandeController {

    @FXML
    private Button btnClose;

    @FXML
    private ComboBox<String> cmbFournisseur;

    @FXML
    private DatePicker dateLivraison;

    @FXML
    private ComboBox<String> cmbStatut;

    @FXML
    private TextArea txtNotes;

    @FXML
    private Button btnAnnuler;

    @FXML
    private Button btnModifier;

    @FXML
    public void initialize() {
        cmbFournisseur.getItems().addAll("Fournitures Pro", "Tech Supplies", "Industrie Plus", "Matériel Express");
        cmbStatut.getItems().addAll("En cours", "Livrée", "Annulée");
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void modifierCommande() {
        System.out.println("Commande modifiée pour: " + cmbFournisseur.getValue());
        closeWindow();
    }
}
