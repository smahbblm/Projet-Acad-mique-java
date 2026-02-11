package com.stock.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import com.stock.service.AuthService;
import com.stock.util.NavigationManager;
import com.stock.util.SessionManager;

/**
 * Contrôleur principal pour la navigation
 */
public class MainController {
    @FXML
    private Label userLabel;

    @FXML
    private Button logoutButton;

    private AuthService authService;

    @FXML
    public void initialize() throws Exception {
        this.authService = new AuthService();

        if (authService.isAuthenticated()) {
            String userName = authService.getCurrentUser().getNom() + " " +
                            authService.getCurrentUser().getPrenom();
            userLabel.setText("Bienvenue " + userName);
        }

        logoutButton.setOnAction(event -> handleLogout());
    }

    @FXML
    private void handleLogout() {
        try {
            authService.logout();
            NavigationManager.getInstance().navigate("/fxml/Login.fxml", "Connexion");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openAdminDashboard() throws Exception {
        NavigationManager.getInstance().navigate("/fxml/admin/DashboardAdmin.fxml", "Dashboard Administrateur");
    }

    @FXML
    private void openApproDashboard() throws Exception {
        NavigationManager.getInstance().navigate("/fxml/appro/DashboardAppro.fxml", "Dashboard Approvisionnement");
    }

    @FXML
    private void openVentesDashboard() throws Exception {
        NavigationManager.getInstance().navigate("/fxml/ventes/DashboardVentes.fxml", "Dashboard Ventes");
    }

    @FXML
    private void openMagasinierDashboard() throws Exception {
        NavigationManager.getInstance().navigate("/fxml/magasinier/DashboardMagasinier.fxml", "Dashboard Magasinier");
    }
}

