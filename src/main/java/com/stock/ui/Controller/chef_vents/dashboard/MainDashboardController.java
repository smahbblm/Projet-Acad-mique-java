package com.stock.ui.Controller.chef_vents.dashboard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainDashboardController implements Initializable{
   //les injections des dépendances de type @FXML
   @FXML private siedbarcontroller sidebarController;
   @FXML private content_controller contentController;

   // Méthode appelée au démarrage
   @Override
   public void initialize(URL location, ResourceBundle resources) {
       setupCommunication();
   }
   public void setupCommunication(){
       // Vérifier que les contrôleurs sont bien injectés
       if (sidebarController != null) {
           System.out.println("Sidebar controller injecté avec succès");
       }
       if (contentController != null) {
           System.out.println("Content controller injecté avec succès");
       }
       // TODO: Implémenter la communication entre les contrôleurs
   }

}
