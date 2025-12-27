package com.stock.ui.Controller.chef_vents.Facture;

import com.stock.model.partenaire.Client;
import com.stock.model.document.BonLivraison;
import com.stock.service.ClientService;
import com.stock.dao.implementation.BonLivraisonDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class AjouterFactureController {

    @FXML private ComboBox<String> clientComboBox;
    @FXML private TextField numeroFactureField;
    @FXML private DatePicker dateFacturePicker;
    @FXML private DatePicker dateEcheancePicker;
    @FXML private TextField statutFactureField;
    @FXML private TableView<BonLivraison> blTableView;
    @FXML private TableColumn<BonLivraison, Boolean> selectColumn;
    @FXML private TableColumn<BonLivraison, String> numeroBLColumn;
    @FXML private TableColumn<BonLivraison, LocalDate> dateLivraisonColumn;
    @FXML private TableColumn<BonLivraison, String> adresseColumn;
    @FXML private TableColumn<BonLivraison, String> montantEstimeColumn;
    @FXML private TableColumn<BonLivraison, String> statutBLColumn;
    @FXML private TextField finalMontantHTField;
    @FXML private TextField finalMontantTVAField;
    @FXML private TextField finalMontantTTCField;
    @FXML private Button calculerFactureButton;
    @FXML private Button genererFactureButton;
    @FXML private Button annulerButton;

    private ObservableList<Client> clientsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("AjouterFactureController initialisé");
        dateFacturePicker.setValue(LocalDate.now());
        dateEcheancePicker.setValue(LocalDate.now().plusDays(30));
        statutFactureField.setText("BROUILLON");
        
        // Générer numéro de facture automatique
        numeroFactureField.setText("FAC-" + System.currentTimeMillis());
        
        // Charger les clients
        chargerClients();
        
        // Configurer les colonnes du tableau
        if (numeroBLColumn != null) {
            // Colonne checkbox pour sélectionner les BL
            selectColumn.setCellFactory(column -> new javafx.scene.control.cell.CheckBoxTableCell<>());
            
            numeroBLColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("numero"));
            dateLivraisonColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateLivraison"));
            adresseColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("adresseLivraison"));
            statutBLColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("statut"));
            montantEstimeColumn.setCellValueFactory(cellData -> {
                return new javafx.beans.property.SimpleStringProperty("0.00 €");
            });
        }
    }

    private void chargerClients() {
        try {
            ClientService clientService = new ClientService();
            clientsList.addAll(clientService.getAllClients());
            
            ObservableList<String> nomsClients = FXCollections.observableArrayList();
            for (Client client : clientsList) {
                nomsClients.add(client.getNom() + " " + client.getPrenom());
            }
            clientComboBox.setItems(nomsClients);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des clients: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void chargerBonsLivraisonClient() {
        try {
            String clientSelectionne = clientComboBox.getValue();
            if (clientSelectionne == null || clientSelectionne.isEmpty()) {
                return;
            }
            
            // Trouver le client sélectionné
            Client client = null;
            for (Client c : clientsList) {
                if ((c.getNom() + " " + c.getPrenom()).equals(clientSelectionne)) {
                    client = c;
                    break;
                }
            }
            
            if (client != null) {
                BonLivraisonDAO blDAO = new BonLivraisonDAO();
                List<BonLivraison> bons = blDAO.findByClient(client.getIdClient());
                blTableView.setItems(FXCollections.observableArrayList(bons));
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des bons de livraison: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void calculerTotauxFacture() {
        try {
            double totalHT = 1000.0; // À calculer depuis les BL sélectionnés
            double totalTVA = totalHT * 0.20;
            double totalTTC = totalHT + totalTVA;
            
            finalMontantHTField.setText(String.format("%.2f", totalHT));
            finalMontantTVAField.setText(String.format("%.2f", totalTVA));
            finalMontantTTCField.setText(String.format("%.2f", totalTTC));
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul: " + e.getMessage());
        }
    }

    @FXML
    public void genererEtEnregistrerFacture() {
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Facture générée");
            alert.setContentText("La facture a été enregistrée avec succès");
            alert.showAndWait();
            
            fermerFenetre();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de la génération");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    
    private void fermerFenetre() {
        javafx.stage.Stage stage = (javafx.stage.Stage) annulerButton.getScene().getWindow();
        stage.close();
    }
}