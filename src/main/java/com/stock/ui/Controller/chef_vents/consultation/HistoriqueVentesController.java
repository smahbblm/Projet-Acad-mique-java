package com.stock.ui.Controller.chef_vents.consultation;

import com.stock.service.ClientService;
import com.stock.model.partenaire.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.List;

public class HistoriqueVentesController {

    @FXML private TableView<?> tableVentes;
    @FXML private ComboBox<String> filtreClient;
    @FXML private ComboBox<String> filtreProduit;
    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private TextField rechercheField;
    @FXML private Text totalVentesNumber;
    @FXML private Text chiffreAffairesNumber;
    @FXML private Text ventesJourNumber;
    @FXML private Button btnExporter;
    @FXML private Button btnActualiser;

    public void initialize() {
        System.out.println("je suis la fonction d'initialisation de la classe HistoriqueVentesController");
        chargerClients();
    }

    // Charger les clients dans la liste déroulante
    private void chargerClients() {
        try {
            ClientService clientService = new ClientService();
            List<Client> listeClients = clientService.getAllClients();
            
            ObservableList<String> nomsClients = FXCollections.observableArrayList();
            for (Client client : listeClients) {
                nomsClients.add(client.getNom() + " " + client.getPrenom());
            }
            
            filtreClient.setItems(nomsClients);
            System.out.println("Clients chargés: " + nomsClients.size());
            
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des clients: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void exporterHistorique() {
        System.out.println("Export historique des ventes");
    }

    @FXML
    public void actualiserHistorique() {
        System.out.println("Actualisation de l'historique");
        chargerClients();
    }
}