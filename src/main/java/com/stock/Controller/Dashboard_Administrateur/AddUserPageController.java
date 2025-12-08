package com.stock.Controller.Dashboard_Administrateur;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddUserPageController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleCombo;

    private UsersPageController parentController;

    public void setParentController(UsersPageController controller) {
        this.parentController = controller;
    }

    @FXML
    public void initialize() {
        roleCombo.getItems().addAll(
                "ADMINISTRATEUR",
                "RESPONSABLE_APPROVISIONNEMENT",
                "RESPONSABLE_VENTES",
                "MAGASINIER"
        );
    }


    @FXML
    private void handleAddUser() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String mdp = passwordField.getText();
        String role = roleCombo.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty() || role == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        UsersPageController.User user =
                new UsersPageController.User(
                        (int)(Math.random()*10000),
                        nom,
                        prenom,
                        email,
                        role,
                        true
                );

        parentController.addUserToTable(user);

        showAlert("Succès", "Utilisateur ajouté avec succès !");

        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }


    private void showAlert(String titre, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
