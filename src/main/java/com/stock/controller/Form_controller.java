package com.stock.controller;

import com.stock.service.AuthService;
import com.stock.exception.AuthenticationException;
import com.stock.model.utilisateur.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class Form_controller {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMe;
    @FXML private Button loginBtn;

    private AuthService authService;

    public void initialize() {
        try {
            authService = new AuthService(); // liaison avec le backend
        } catch (Exception e) {
            showError("Erreur système", "Impossible d'initialiser le service d'authentification.");
            e.printStackTrace();
        }

        loginBtn.setOnAction(event -> handleLogin());
        passwordField.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        String email = emailField.getText();
        String pass = passwordField.getText();

        if (email.isEmpty() || pass.isEmpty()) {
            showError("Erreur !", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            authService.login(email, pass); // Crée la session

            // Récupération de l'utilisateur depuis la session
            Utilisateur user = authService.getCurrentUser();
            showSuccess("Connexion réussie !");

            // Redirection selon le rôle

            switch (user.getRole()) {
                case "ADMINISTRATEUR":
                    openDashboard("/fxml/DashboardAdmin.fxml", "Dashboard Admin");
                    break;
                case "RESPONSABLE_VENTES":
                    openDashboard("/fxml/DashboardChef_vents.fxml", "Dashboard Ventes");
                    break;
                case "RESPONSABLE_APPROVISIONNEMENT":
                    openDashboard("/fxml/DashboardAppro.fxml", "Dashboard Approvisionnement");
                    break;
                case "MAGASINIER":
                    openDashboard("/fxml/DashboardMagasinier.fxml", "Dashboard Magasinier");
                    break;
                default:
                    showError("Erreur", "Rôle inconnu.");
                    break;
            }

        } catch (AuthenticationException e) {
            showError("Erreur", e.getMessage());
        } catch (Exception e) {
            showError("Erreur système", "Une erreur inattendue est survenue.");
            e.printStackTrace();
        }
    }


    // Méthode générique pour ouvrir un dashboard
    private void openDashboard(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();

            // Fermer la fenêtre de login
            emailField.getScene().getWindow().hide();
        } catch (Exception e) {
            showError("Erreur système", "Impossible d'ouvrir le dashboard.");
            e.printStackTrace();
        }
    }

    // Petits helpers
    private void showSuccess(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText("✓ Succès");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("✕ " + title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
