package com.stock.Controller.Dashboard_Administrateur;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EditUserPageController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleBox;
    @FXML private CheckBox actifCheck;

    private UsersPageController parentController;
    private UsersPageController.User userToEdit;

    public void setParentController(UsersPageController controller) {
        this.parentController = controller;
    }

    public void setUserData(UsersPageController.User user) {
        this.userToEdit = user;

        // Pré-remplir les champs
        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        emailField.setText(user.getEmail());
        roleBox.getItems().addAll(
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

        // On crée un NOUVEAU user (statique)
        UsersPageController.User updatedUser =
                new UsersPageController.User(
                        userToEdit.getId(),                  // même ID
                        nomField.getText(),
                        prenomField.getText(),
                        emailField.getText(),
                        roleBox.getValue(),
                        actifCheck.isSelected()
                );

        // Envoi à la table
        parentController.updateUserInTable(userToEdit, updatedUser);

        // Fermer fenêtre
        nomField.getScene().getWindow().hide();
    }
}
