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
import java.lang.Exception;
public class Form_controller {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMe;
    @FXML private Button loginBtn;

    private AuthService authService;
    private boolean isInitialized = false;

    public void initialize() {
        try {
            authService = new AuthService();
            isInitialized = true;
        } catch (Exception e) {
            showError("Erreur système", "Impossible d'initialiser le service d'authentification.");
            loginBtn.setDisable(true);
            return;
        }

        loginBtn.setOnAction(event -> handleLogin());
        passwordField.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        if (!isInitialized || authService == null) {
            showError("Erreur système", "Service non initialisé.");
            return;
        }

        String email = emailField.getText();
        String pass = passwordField.getText();

        if (email.isEmpty() || pass.isEmpty()) {
            showError("Erreur !", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            authService.login(email, pass);
            Utilisateur user = authService.getCurrentUser();
            
            if (user == null || user.getRole() == null) {
                showError("Erreur", "Données utilisateur invalides.");
                return;
            }

            switch (user.getRole()) {
                case "ADMINISTRATEUR":
                    openDashboard("/fxml/Dashboard_Administrateur/dashboard.fxml", "Dashboard Admin");
                    break;
                case "RESPONSABLE_VENTES":
                    openDashboard("/fxml/DashboardChef_vents.fxml", "Dashboard Ventes");
                    break;
                case "RESPONSABLE_APPROVISIONNEMENT":
                    openDashboard("/fxml/DashResponsableApprovisionnements/dashboardResApprov.fxml", "Dashboard Approvisionnement");
                    break;
                case "MAGASINIER":
                    openDashboard("/fxml/Magasinier/DashboardMagasinier.fxml", "Dashboard Magasinier");
                    break;
                default:
                    showError("Erreur", "Rôle inconnu: " + user.getRole());
                    return;
            }
            
            showSuccess("Connexion réussie !");

        } catch (AuthenticationException e) {
            showError("Erreur d'authentification", e.getMessage());
        } catch (Exception e) {
            showError("Erreur système", "Une erreur inattendue est survenue.");
        }
    }


    // Méthode générique pour ouvrir un dashboard
    private void openDashboard(String fxmlPath, String title) {
        try {
            if (getClass().getResource(fxmlPath) == null) {
                showError("Erreur", "Fichier FXML introuvable: " + fxmlPath);
                return;
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();

            emailField.getScene().getWindow().hide();
        } catch (Exception e) {
            showError("Erreur système", "Impossible d'ouvrir le dashboard: " + e.getMessage());
        }
    }

    // Petits helpers
    private void showSuccess(String msg) {
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("✓ Succès");
            alert.setContentText(msg);
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'affichage du message de succès: " + msg);
        }
    }

    private void showError(String title, String msg) {
        try {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText("✕ " + title);
            alert.setContentText(msg);
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'affichage du message d'erreur: " + title + " - " + msg);
        }
    }
}
