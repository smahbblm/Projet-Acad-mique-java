package com.stock.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Form_controller {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMe;
    @FXML private Button loginBtn;

    public void initialize() {
        loginBtn.setOnAction(event -> handleLogin());
        passwordField.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        String email = emailField.getText();
        String pass = passwordField.getText();

        if (email.isEmpty() || pass.isEmpty()) {
            showAlert("Erreur!", "Veillez remplir tous les champs.");
            return;
        }

        if (email.equals("admin@stock.com") && pass.equals("1234")) {
            showAlert("Succès", "Connexion réussie !");
        } else {
            showAlert("Erreur!", "Email ou mot de passe incorrect.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert;
        if (title.equals("Succès")) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("✓ " + title);
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("✕ " + title);
        }
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
