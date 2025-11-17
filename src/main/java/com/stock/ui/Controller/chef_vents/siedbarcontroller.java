package com.stock.ui.Controller.chef_vents;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class siedbarcontroller implements Initializable {

    // Variables FXML pour les éléments de menu
    @FXML private Label dashboardMenu;
    @FXML private Label clientsMenu;
    @FXML private Label livraisons;
    @FXML private Label features;
    @FXML private Label retours;
    @FXML private Label historique;
    @FXML private Label disponibilite;
    @FXML private Label deconnexion;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        // Configuration initiale de la sidebar
        System.out.println("Sidebar initialisée");
    }
    
    @FXML
    public void affiche_dashboard(MouseEvent event){
        System.out.println("Dashboard menu cliqué");
        // TODO: Communiquer avec MainDashboardController
    }
    
    @FXML
    public void affiche_clients(MouseEvent event){
        System.out.println("Clients menu cliqué");
        // TODO: Communiquer avec MainDashboardController
    }
    
    @FXML
    public void affiche_livraisons(MouseEvent event){
        System.out.println("Livraisons menu cliqué");
        // TODO: Communiquer avec MainDashboardController
    }
    
    // Méthode pour gérer l'état actif des menus
    private void setActiveMenu(Label activeMenu) {
        // Réinitialiser tous les menus
        resetMenuStyles();
        // Marquer le menu actif
        if (activeMenu != null) {
            activeMenu.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 5px;");
        }
    }
    
    private void resetMenuStyles() {
        String defaultStyle = "";
        if (dashboardMenu != null) dashboardMenu.setStyle(defaultStyle);
        if (clientsMenu != null) clientsMenu.setStyle(defaultStyle);
        // TODO: Ajouter les autres menus
    }
}
