package com.stock.ui.Controller.chef_vents.dashboard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class siedbarcontroller implements Initializable {

    @FXML private Label dashboardMenu;
    @FXML private Label clientsMenu;
    @FXML private Label livraisons;
    @FXML private Label features;
    @FXML private Label retours;
    @FXML private Label historique;
    @FXML private Label disponibilite;
    @FXML private Label deconnexion;

    private MainDashboardController mainController;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        System.out.println("Sidebar initialisée");
    }
    
    public void setMainController(MainDashboardController controller) {
        this.mainController = controller;
    }
    
    @FXML
    public void affiche_dashboard(MouseEvent event){
        System.out.println("Dashboard menu cliqué");
        setActiveMenu(dashboardMenu);
        if (mainController != null) {
            mainController.loadDashboard();
        }
    }
    
    @FXML
    public void affiche_clients(MouseEvent event){
        System.out.println("Clients menu cliqué");
        setActiveMenu(clientsMenu);
        if (mainController != null) {
            mainController.loadClients();
        }
    }
    
    @FXML
    public void affiche_livraisons(MouseEvent event){
        System.out.println("Livraisons menu cliqué");
        setActiveMenu(livraisons);
        if (mainController != null) {
            mainController.loadLivraisons();
        }
    }
    
    @FXML
    public void affiche_factures(MouseEvent event){
        System.out.println("Factures menu cliqué");
        setActiveMenu(features);
        if (mainController != null) {
            mainController.loadFactures();
        }
    }
    
    @FXML
    public void affiche_retours(MouseEvent event){
        System.out.println("Bons de retour menu cliqué");
        setActiveMenu(retours);
        if (mainController != null) {
            mainController.loadBonsRetour();
        }
    }
    
    @FXML
    public void affiche_historique(MouseEvent event){
        System.out.println("Historique menu cliqué");
        setActiveMenu(historique);
        if (mainController != null) {
            mainController.loadHistorique();
        }
    }
    
    @FXML
    public void affiche_disponibilite(MouseEvent event){
        System.out.println("Disponibilité menu cliqué");
        setActiveMenu(disponibilite);
        if (mainController != null) {
            mainController.loadDisponibilite();
        }
    }
    
    @FXML
    public void deconnecter(MouseEvent event){
        System.out.println("Déconnexion");
        try {
            // Invalider la session
            com.stock.util.SessionManager.getInstance().invalidateSession();
            
            // Afficher message de confirmation
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Déconnexion");
            alert.setHeaderText("Déconnexion réussie");
            alert.setContentText("Vous avez été déconnecté avec succès");
            alert.showAndWait();
            
            // Fermer l'application ou retourner à la page de connexion
            javafx.application.Platform.exit();
        } catch (Exception e) {
            System.err.println("Erreur lors de la déconnexion: " + e.getMessage());
        }
    }
    
    private void setActiveMenu(Label activeMenu) {
        resetMenuStyles();
        if (activeMenu != null) {
            activeMenu.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 5px;");
        }
    }
    
    private void resetMenuStyles() {
        String defaultStyle = "";
        if (dashboardMenu != null) dashboardMenu.setStyle(defaultStyle);
        if (clientsMenu != null) clientsMenu.setStyle(defaultStyle);
        if (livraisons != null) livraisons.setStyle(defaultStyle);
        if (features != null) features.setStyle(defaultStyle);
        if (retours != null) retours.setStyle(defaultStyle);
        if (historique != null) historique.setStyle(defaultStyle);
        if (disponibilite != null) disponibilite.setStyle(defaultStyle);
    }
}
