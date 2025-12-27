package com.stock.Controller.controller_Dashboard_Administrateur;

import com.stock.model.utilisateur.Utilisateur;
import com.stock.service.UtilisateurService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.Arrays;

public class AddUserPageController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleCombo;

    private UsersPageController parentController;
    private UtilisateurService utilisateurService;

    public void setParentController(UsersPageController controller) {
        this.parentController = controller;
    }

    @FXML
    public void initialize() {
        roleCombo.getItems().setAll(
                "Administrateur",
                "Responsable_Approvisionnement",
                "Responsable_Ventes",
                "Magasinier"
        );

        try {
            utilisateurService = new UtilisateurService();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'initialiser le service utilisateur.");
        }
    }

    @FXML
    private void handleAddUser() {
        try {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String email = emailField.getText();
            String mdp = passwordField.getText();
            String role = roleCombo.getValue();

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty() || role == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            Utilisateur nouvelUtilisateur = com.stock.model.utilisateur.UserFactory
                    .createUser(role, nom, prenom, email, mdp);

            nouvelUtilisateur.setActif(true);
            nouvelUtilisateur.setDateCreation(LocalDate.now());

            utilisateurService.addUser(nouvelUtilisateur);

            // 🔥 Correction ici → loadUsers() au lieu de reloadUsers()
            parentController.loadUsers();

            showAlert("Succès", "Utilisateur ajouté avec succès !");

            // Fermer la fenêtre
            ((Stage) nomField.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ajouter l'utilisateur.");
        }
    }

    private void showAlert(String titre, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
