package com.stock.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.stock.service.AuthService;
import com.stock.util.NavigationManager;

/**
 * Contrôleur pour l'écran de connexion
 */
public class LoginController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    private AuthService authService;

    @FXML
    public void initialize() throws Exception {
        this.authService = new AuthService();
        loginButton.setOnAction(event -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        try {
            // Masquer le message d'erreur précédent
            hideError();

            String email = emailField.getText().trim();
            String password = passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                showError("Veuillez remplir tous les champs");
                return;
            }

            authService.login(email, password);

            // Redirection selon le rôle
            String role = authService.getUserRole();
            navigateByRole(role);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
        }
    }

    private void hideError() {
        if (errorLabel != null) {
            errorLabel.setVisible(false);
            errorLabel.setManaged(false);
        }
    }

    private void navigateByRole(String role) throws Exception {
        NavigationManager manager = NavigationManager.getInstance();
        switch (role) {
            case "ADMINISTRATEUR":
                manager.navigate("/fxml/admin/DashboardAdmin.fxml", "Dashboard Administrateur");
                break;
            case "RESPONSABLE_APPROVISIONNEMENT":
                manager.navigate("/fxml/appro/DashboardAppro.fxml", "Dashboard Approvisionnement");
                break;
            case "RESPONSABLE_VENTES":
                manager.navigate("/fxml/ventes/DashboardVentes.fxml", "Dashboard Ventes");
                break;
            case "MAGASINIER":
                manager.navigate("/fxml/magasinier/DashboardMagasinier.fxml", "Dashboard Magasinier");
                break;
            default:
                manager.navigate("/fxml/MainLayout.fxml", "Accueil");
        }
    }
}

