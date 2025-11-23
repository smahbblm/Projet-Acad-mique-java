package com.stock.Controller.controllerResponsableApprovisionnements;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifierFournisseurController {

    @FXML
    private Button btnClose;

    @FXML
    private TextField txtEntreprise;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtTelephone;

    @FXML
    private TextArea txtAdresse;

    @FXML
    private Button btnAnnuler;

    @FXML
    private Button btnModifier;

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void modifierFournisseur() {
        System.out.println("Fournisseur modifié: " + txtEntreprise.getText());
        closeWindow();
    }
}
