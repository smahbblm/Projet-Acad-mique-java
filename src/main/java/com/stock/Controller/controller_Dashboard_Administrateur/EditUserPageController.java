package com.stock.Controller.Dashboard_Administrateur;

import com.stock.model.utilisateur.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EditUserPageController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleBox;
    @FXML private CheckBox actifCheck;

    private UsersPageController parentController;
    private Utilisateur userToEdit;

    public void setParentController(UsersPageController controller) {
        this.parentController = controller;
    }

    public void setUserData(Utilisateur user) {
        this.userToEdit = user;

        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        emailField.setText(user.getEmail());

        roleBox.getItems().setAll(
                "ADMINISTRATEUR",
                "RESPONSABLE_APPROVISIONNEMENT",
                "RESPONSABLE_VENTES",
                "MAGASINIER"
        );

        roleBox.setValue(user.getRole());
        actifCheck.setSelected(user.isActif());
    }

    @FXML
    private void handleSave() {
        try {
            userToEdit.setNom(nomField.getText());
            userToEdit.setPrenom(prenomField.getText());
            userToEdit.setEmail(emailField.getText());
            userToEdit.setRole(roleBox.getValue());
            userToEdit.setActif(actifCheck.isSelected());

            // Mettre à jour la base de données
            com.stock.service.UtilisateurService service = new com.stock.service.UtilisateurService();
            service.updateUser(userToEdit);

            // Recharger la table
            parentController.loadUsers();

            // Fermer la fenêtre
            nomField.getScene().getWindow().hide();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Impossible de sauvegarder les modifications : " + e.getMessage());
            alert.showAndWait();
        }
    }

}
