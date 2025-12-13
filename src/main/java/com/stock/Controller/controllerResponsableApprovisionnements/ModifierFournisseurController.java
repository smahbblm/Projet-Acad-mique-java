package com.stock.Controller.controllerResponsableApprovisionnements;

import com.stock.model.partenaire.Fournisseur;
import com.stock.service.FournisseurService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ModifierFournisseurController {

    @FXML
    private Button btnClose;

    @FXML
    private TextField txtRaisonSociale;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtTelephone;

    @FXML
    private TextArea txtAdresse;

    @FXML
    private TextArea txtConditions;

    @FXML
    private Button btnAnnuler;

    @FXML
    private Button btnModifier;

    private FournisseurService fournisseurService;
    private Fournisseur fournisseur;
    private Runnable onFournisseurUpdated;

    @FXML
    public void initialize() {
        try {
            fournisseurService = new FournisseurService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
        txtRaisonSociale.setText(fournisseur.getRaisonSociale());
        txtContact.setText(fournisseur.getContact());
        txtEmail.setText(fournisseur.getEmail());
        txtTelephone.setText(fournisseur.getTelephone());
        txtAdresse.setText(fournisseur.getAdresse());
        txtConditions.setText(fournisseur.getConditions());
    }

    public void setOnFournisseurUpdated(Runnable callback) {
        this.onFournisseurUpdated = callback;
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void modifierFournisseur() {
        try {
            if (txtRaisonSociale.getText().isEmpty()) {
                showAlert("Erreur", "La raison sociale est obligatoire");
                return;
            }

            fournisseur.setRaisonSociale(txtRaisonSociale.getText());
            fournisseur.setContact(txtContact.getText());
            fournisseur.setEmail(txtEmail.getText());
            fournisseur.setTelephone(txtTelephone.getText());
            fournisseur.setAdresse(txtAdresse.getText());
            fournisseur.setConditions(txtConditions.getText());

            fournisseurService.modifierFournisseur(fournisseur);
            if (onFournisseurUpdated != null) {
                onFournisseurUpdated.run();
            }
            showAlert("Succès", "Fournisseur modifié avec succès");
            closeWindow();
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
