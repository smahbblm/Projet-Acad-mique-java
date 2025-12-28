package com.stock.Controller.controller_Dashboard_Administrateur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.stock.model.stock.MouvementStock;
import com.stock.model.utilisateur.Utilisateur;
import com.stock.service.UtilisateurService;
import com.stock.service.MouvementStockService;

public class DashboardController {

    private Node dashboardHomeContent;

    @FXML private LineChart<String, Number> userEvolutionChart;
    @FXML private PieChart rolesDistributionChart;

    @FXML private BorderPane mainBorderPane;
    @FXML private AnchorPane contentPane;

    @FXML private VBox statsSubMenu;
    @FXML private VBox reportsSubMenu;
    @FXML private VBox systemSubMenu;

    private UtilisateurService utilisateurService;
    private MouvementStockService mouvementStockService;

    @FXML private Label totalUsersLabel;
    @FXML private Label activeAccountsLabel;
    @FXML private Label inactiveAccountsLabel;
    @FXML private Label criticalAlertsLabel;

    @FXML private Label totalEntreesLabel;
    @FXML private Label totalSortiesLabel;
    @FXML private Label stockActuelLabel;

    private List<MouvementStock> masterData = new ArrayList<>();

    @FXML
    public void initialize() {
        hideAllSubMenus();

        try {
            // Initialiser les services
            utilisateurService = new UtilisateurService();
            mouvementStockService = new MouvementStockService();

            // Charger les mouvements de stock
            masterData = mouvementStockService.consulterTousMouvements();
            refreshTotals();

            // Charger les données dynamiques
            refreshDashboard();

            // --- Capturer le dashboardHomeContent ---
            dashboardHomeContent = contentPane.getChildren().get(0);

        } catch (Exception e) {
            e.printStackTrace();
            showInfo("Erreur", "Impossible d'initialiser le tableau de bord.");
        }
    }


    private void refreshDashboard() {
        try {
            List<Utilisateur> allUsers = utilisateurService.getAllUsers();

            int totalUsers = allUsers.size();
            int activeUsers = (int) allUsers.stream().filter(Utilisateur::isActif).count();
            int inactiveUsers = totalUsers - activeUsers;
            long criticalAlerts = allUsers.stream().filter(u -> !u.isActif()).count();

            totalUsersLabel.setText(String.valueOf(totalUsers));
            activeAccountsLabel.setText(String.valueOf(activeUsers));
            inactiveAccountsLabel.setText(String.valueOf(inactiveUsers));
            criticalAlertsLabel.setText(String.valueOf(criticalAlerts));

            updateUserEvolutionChart(allUsers);
            updateRolesDistributionChart(allUsers);

            refreshTotals();
        } catch (Exception e) {
            e.printStackTrace();
            showInfo("Erreur", "Impossible de charger les données dynamiques.");
        }
    }

    private void updateUserEvolutionChart(List<Utilisateur> users) {
        userEvolutionChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Utilisateurs créés par jour");

        Map<String, Long> usersByDate = users.stream()
                .collect(Collectors.groupingBy(u -> u.getDateCreation().toString(), TreeMap::new, Collectors.counting()));

        usersByDate.forEach((date, count) -> series.getData().add(new XYChart.Data<>(date, count)));
        userEvolutionChart.getData().add(series);
    }

    private void updateRolesDistributionChart(List<Utilisateur> users) {
        rolesDistributionChart.getData().clear();

        Map<String, Long> rolesCount = users.stream()
                .collect(Collectors.groupingBy(
                        u -> u.getRole().trim(), // sécurité
                        Collectors.counting()
                ));

        if (rolesCount.isEmpty()) {
            rolesDistributionChart.getData()
                    .add(new PieChart.Data("Aucun rôle", 1));
        } else {
            rolesCount.forEach((role, count) ->
                    rolesDistributionChart.getData()
                            .add(new PieChart.Data(role, count))
            );
        }

        rolesDistributionChart.setLegendVisible(true);
        rolesDistributionChart.setLabelsVisible(true);
        rolesDistributionChart.setAnimated(false);

        javafx.application.Platform.runLater(() -> {
            rolesDistributionChart.applyCss();
            rolesDistributionChart.layout();
        });
    }




    private void hideAllSubMenus() {
        statsSubMenu.setVisible(false); statsSubMenu.setManaged(false);
        reportsSubMenu.setVisible(false); reportsSubMenu.setManaged(false);
        systemSubMenu.setVisible(false); systemSubMenu.setManaged(false);
    }

    private void refreshTotals() {
        updateTotals();
    }

    private void updateTotals() {
        if (masterData == null || masterData.isEmpty()) {
            totalEntreesLabel.setText("0");
            totalSortiesLabel.setText("0");
            stockActuelLabel.setText("0");
            return;
        }

        int totalEntrees = masterData.stream()
                .filter(m -> "ENTREE".equalsIgnoreCase(m.getTypeMouvement()))
                .mapToInt(MouvementStock::getQuantite)
                .sum();

        int totalSorties = masterData.stream()
                .filter(m -> "SORTIE".equalsIgnoreCase(m.getTypeMouvement()))
                .mapToInt(MouvementStock::getQuantite)
                .sum();

        int stockActuel = totalEntrees - totalSorties;

        totalEntreesLabel.setText(String.valueOf(totalEntrees));
        totalSortiesLabel.setText(String.valueOf(totalSorties));
        stockActuelLabel.setText(String.valueOf(stockActuel));
    }

    private void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // Navigation et menus
    @FXML private void handleDashboardHome() {
        hideAllSubMenus();
        contentPane.getChildren().clear();
        if (dashboardHomeContent != null) contentPane.getChildren().add(dashboardHomeContent);
    }

    @FXML private void handleUserList(MouseEvent event) {
        loadPage("/fxml/Dashboard_Administrateur/UsersPage.fxml", "Impossible de charger UsersPage.fxml");
    }

    @FXML private void handleStockMovements() {
        loadPage("/fxml/Dashboard_Administrateur/StockMovement.fxml", "Impossible de charger la page des mouvements de stock");
    }

    @FXML private void handleGenerateReport() {
        loadPage("/fxml/Dashboard_Administrateur/GenerateReport.fxml", "Impossible de charger la page de génération de rapport");
    }

    @FXML private void handleReports() {
        loadPage("/fxml/Dashboard_Administrateur/ViewReports.fxml", "Impossible de charger la page de consultation des rapports");
    }

    @FXML private void handleDatabase() {
        loadPage("/fxml/Dashboard_Administrateur/DatabaseManagement.fxml", "Impossible de charger la page de restauration BDD");
    }

    private void loadPage(String fxmlPath, String errorMessage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent page = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(page);
        } catch (Exception e) {
            e.printStackTrace();
            showInfo("Erreur", errorMessage);
        }
    }

    @FXML private void handleSettings() { showInfo("Paramètres", "Ouverture des paramètres."); }
    @FXML private void handleLogs() { showInfo("Logs", "Consultation du journal."); }
    @FXML private void handleLogout() { showInfo("Déconnexion", "Vous êtes déconnecté."); }

    @FXML private void openGlobalStatisticsPage() {
        hideAllSubMenus();
        loadPage("/fxml/Dashboard_Administrateur/GlobalStatistics.fxml", "Impossible de charger GlobalStatistics.fxml");
    }

    @FXML private void showStatsSubMenu() { hideAllSubMenus(); statsSubMenu.setVisible(true); statsSubMenu.setManaged(true); }
    @FXML private void keepStatsSubMenu() { statsSubMenu.setVisible(true); statsSubMenu.setManaged(true); }
    @FXML private void hideStatsSubMenu() { statsSubMenu.setVisible(false); statsSubMenu.setManaged(false); }

    @FXML private void showReportsSubMenu() { hideAllSubMenus(); reportsSubMenu.setVisible(true); reportsSubMenu.setManaged(true); }
    @FXML private void keepReportsSubMenu() { reportsSubMenu.setVisible(true); reportsSubMenu.setManaged(true); }
    @FXML private void hideReportsSubMenu() { reportsSubMenu.setVisible(false); reportsSubMenu.setManaged(false); }

    @FXML private void showSystemSubMenu() { hideAllSubMenus(); systemSubMenu.setVisible(true); systemSubMenu.setManaged(true); }
    @FXML private void keepSystemSubMenu() { systemSubMenu.setVisible(true); systemSubMenu.setManaged(true); }
    @FXML private void hideSystemSubMenu() { systemSubMenu.setVisible(false); systemSubMenu.setManaged(false); }

}
