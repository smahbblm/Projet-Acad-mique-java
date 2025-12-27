package com.stock.ui.Controller.chef_vents.dashboard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainDashboardController implements Initializable{
   @FXML private siedbarcontroller sidebarController;
   @FXML private content_controller contentController;
   @FXML private BorderPane contentArea;

   @Override
   public void initialize(URL location, ResourceBundle resources) {
       setupCommunication();
   }
   
   public void setupCommunication(){
       if (sidebarController != null) {
           System.out.println("Sidebar controller injecté avec succès");
           sidebarController.setMainController(this);
       }
       if (contentController != null) {
           System.out.println("Content controller injecté avec succès");
       }
   }

   public void loadDashboard() {
       System.out.println("Chargement du dashboard");
       loadView("/fxml/gestionChef-vents/dashboard/dashboard-content.fxml");
   }

   public void loadClients() {
       System.out.println("Chargement des clients");
       loadView("/fxml/gestionChef-vents/clients/Clients.fxml");
   }

   public void loadLivraisons() {
       System.out.println("Chargement des livraisons");
       loadView("/fxml/gestionChef-vents/Bons_liv/BonsLivraison.fxml");
   }

   public void loadFactures() {
       System.out.println("Chargement des factures");
       loadView("/fxml/gestionChef-vents/factures/Facture.fxml");
   }

   public void loadHistorique() {
       System.out.println("Chargement de l'historique");
       loadView("/fxml/gestionChef-vents/consultation/HistoriqueVentes.fxml");
   }

   public void loadDisponibilite() {
       System.out.println("Chargement de la disponibilité");
       loadView("/fxml/gestionChef-vents/consultation/DisponibiliteProduits.fxml");
   }

   public void loadBonsRetour() {
       System.out.println("Chargement des bons de retour");
       loadView("/fxml/gestionChef-vents/retours/BonsRetour.fxml");
   }

   private void loadView(String fxmlPath) {
       try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
           javafx.scene.Node view = loader.load();
           if (contentArea != null) {
               contentArea.setCenter(view);
           }
       } catch (IOException e) {
           System.err.println("Erreur lors du chargement de la vue: " + fxmlPath);
           e.printStackTrace();
       } catch (Exception e) {
           System.err.println("Erreur: " + e.getMessage());
           e.printStackTrace();
       }
   }
}
