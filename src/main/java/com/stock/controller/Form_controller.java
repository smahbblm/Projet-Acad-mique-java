package com.stock.controller;

import com.stock.service.AuthService;
import com.stock.exception.AuthenticationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
            authService.login(email, pass); // 🔥 appel réel à la base de données
            showSuccess("Connexion réussie !");
            // TODO: redirection vers dashboard ici
        } catch (AuthenticationException e) {
            showError("Erreur", e.getMessage());
        } catch (Exception e) {
            showError("Erreur système", "Une erreur inattendue est survenue.");
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
