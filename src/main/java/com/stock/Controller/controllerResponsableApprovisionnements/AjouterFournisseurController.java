package com.stock.Controller.controllerResponsableApprovisionnements;

import com.stock.model.partenaire.Fournisseur;
import com.stock.service.FournisseurService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AjouterFournisseurController {

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
    private Button btnAjouter;

    private FournisseurService fournisseurService;
    private Runnable onFournisseurAdded;

    @FXML
    public void initialize() {
        try {
            fournisseurService = new FournisseurService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnFournisseurAdded(Runnable callback) {
        this.onFournisseurAdded = callback;
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void ajouterFournisseur() {
        try {
            if (txtRaisonSociale.getText().isEmpty()) {
                showAlert("Erreur", "La raison sociale est obligatoire");
                return;
            }

            Fournisseur fournisseur = new Fournisseur();
            fournisseur.setRaisonSociale(txtRaisonSociale.getText());
            fournisseur.setContact(txtContact.getText());
            fournisseur.setEmail(txtEmail.getText());
            fournisseur.setTelephone(txtTelephone.getText());
            fournisseur.setAdresse(txtAdresse.getText());
            fournisseur.setConditions(txtConditions.getText());

            fournisseurService.ajouterFournisseur(fournisseur);
            if (onFournisseurAdded != null) {
                onFournisseurAdded.run();
            }
            showAlert("Succès", "Fournisseur ajouté avec succès");
            closeWindow();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout: " + e.getMessage());
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
