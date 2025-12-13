package com.stock.Controller.Dashboard_Administrateur;

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

public class DashboardController {

    // Changement ici : Node au lieu de Parent
    private Node dashboardHomeContent;

    @FXML private LineChart<String, Number> userEvolutionChart;
    @FXML private PieChart rolesDistributionChart;

    @FXML private BorderPane mainBorderPane;
    @FXML private AnchorPane contentPane;

    @FXML private VBox statsSubMenu;
    @FXML private VBox reportsSubMenu;
    @FXML private VBox systemSubMenu;

    @FXML private Label totalUsersLabel;
    @FXML private Label activeAccountsLabel;
    @FXML private Label inactiveAccountsLabel;
    @FXML private Label criticalAlertsLabel;

    // -----------------------------------------------------------------------
    @FXML
    public void initialize() {
        hideAllSubMenus();

        if (!contentPane.getChildren().isEmpty()) {
            dashboardHomeContent = contentPane.getChildren().get(0);
        }

        // Valeurs statiques
        totalUsersLabel.setText("100");
        activeAccountsLabel.setText("50");
        inactiveAccountsLabel.setText("25");
        criticalAlertsLabel.setText("5");

        // Graph utilisateurs
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Utilisateurs actifs");
        series.getData().add(new XYChart.Data<>("Lun", 50));
        series.getData().add(new XYChart.Data<>("Mar", 52));
        series.getData().add(new XYChart.Data<>("Mer", 53));
        series.getData().add(new XYChart.Data<>("Jeu", 54));
        series.getData().add(new XYChart.Data<>("Ven", 52));
        userEvolutionChart.getData().add(series);

        // Graph rôles
        rolesDistributionChart.getData().add(new PieChart.Data("Responsable ventes", 4));
        rolesDistributionChart.getData().add(new PieChart.Data("Magasinier", 10));
        rolesDistributionChart.getData().add(new PieChart.Data("Resp. approvisionnement", 2));
        rolesDistributionChart.setLegendVisible(true);
        rolesDistributionChart.setLabelsVisible(true);
    }

    // -----------------------------------------------------------------------
    // Menu
    private void hideAllSubMenus() {
        statsSubMenu.setVisible(false);   statsSubMenu.setManaged(false);
        reportsSubMenu.setVisible(false); reportsSubMenu.setManaged(false);
        systemSubMenu.setVisible(false);  systemSubMenu.setManaged(false);
    }

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

    // -----------------------------------------------------------------------

    // Navigation
    @FXML
    private void handleDashboardHome() {
        hideAllSubMenus();
        contentPane.getChildren().clear();
        if (dashboardHomeContent != null) contentPane.getChildren().add(dashboardHomeContent);
    }

    @FXML
    private void handleUserList(MouseEvent event) {
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

    // -----------------------------------------------------------------------
    // Charger un FXML dans contentPane
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

    // -----------------------------------------------------------------------
    // Paramètres et journal
    @FXML private void handleSettings() { showInfo("Paramètres", "Ouverture des paramètres."); }
    @FXML private void handleLogs() { showInfo("Logs", "Consultation du journal."); }
    @FXML private void handleLogout() { showInfo("Déconnexion", "Vous êtes déconnecté."); }

    // -----------------------------------------------------------------------
    // Popup
    private void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
