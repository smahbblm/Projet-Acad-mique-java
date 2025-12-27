package com.stock.ui.Controller.chef_vents.consultation;

import com.stock.dao.implementation.HistoriqueVenteDAO;
import com.stock.model.document.HistoriqueVente;
import com.stock.service.ClientService;
import com.stock.service.ProduitService;
import com.stock.model.partenaire.Client;
import com.stock.model.produit.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.List;

public class HistoriqueVentesController {

    @FXML private TableView<HistoriqueVente> tableVentes;
    @FXML private TableColumn<HistoriqueVente, String> colNumeroFacture;
    @FXML private TableColumn<HistoriqueVente, LocalDate> colDate;
    @FXML private TableColumn<HistoriqueVente, String> colClient;
    @FXML private TableColumn<HistoriqueVente, String> colProduit;
    @FXML private TableColumn<HistoriqueVente, Integer> colQuantite;
    @FXML private TableColumn<HistoriqueVente, Double> colPrixUnitaire;
    @FXML private TableColumn<HistoriqueVente, Double> colMontantTotal;
    @FXML private ComboBox<String> filtreClient;
    @FXML private ComboBox<String> filtreProduit;
    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private TextField rechercheField;
    @FXML private Text totalVentesNumber;
    @FXML private Text chiffreAffairesNumber;
    @FXML private Text ventesJourNumber;
    @FXML private Button btnActualiser;

    private ObservableList<HistoriqueVente> historiqueList = FXCollections.observableArrayList();
    private ObservableList<HistoriqueVente> historiqueListComplete = FXCollections.observableArrayList();

    public void initialize() {
        System.out.println("je suis la fonction d'initialisation de la classe HistoriqueVentesController");
        setupTableColumns();
        chargerClients();
        chargerProduits();
        chargerHistorique();
        setupSearchListener();
    }

    private void setupSearchListener() {
        // Listener pour la recherche en temps réel
        rechercheField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrerHistorique();
        });
        
        // Listeners pour les filtres
        filtreClient.setOnAction(e -> filtrerHistorique());
        filtreProduit.setOnAction(e -> filtrerHistorique());
        dateDebut.setOnAction(e -> filtrerHistorique());
        dateFin.setOnAction(e -> filtrerHistorique());
    }

    private void setupTableColumns() {
        if (colNumeroFacture != null) {
            colNumeroFacture.setCellValueFactory(new PropertyValueFactory<>("numeroFacture"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("dateVente"));
            colClient.setCellValueFactory(new PropertyValueFactory<>("nomClient"));
            colProduit.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
            colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
            colPrixUnitaire.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
            colMontantTotal.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
            
            tableVentes.setItems(historiqueList);
        }
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

    // Charger les produits dans la liste déroulante
    private void chargerProduits() {
        try {
            ProduitService produitService = new ProduitService();
            List<Produit> listeProduits = produitService.consulterTousProduits();
            
            ObservableList<String> nomsProduits = FXCollections.observableArrayList();
            for (Produit produit : listeProduits) {
                nomsProduits.add(produit.getDesignation());
            }
            
            filtreProduit.setItems(nomsProduits);
            System.out.println("Produits chargés: " + nomsProduits.size());
            
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des produits: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private void chargerHistorique() {
        try {
            HistoriqueVenteDAO dao = new HistoriqueVenteDAO();
            historiqueListComplete.clear();
            historiqueListComplete.addAll(dao.getAllVentes());
            
            historiqueList.clear();
            historiqueList.addAll(historiqueListComplete);
            
            mettreAJourStatistiques();
            
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'historique: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filtrerHistorique() {
        String recherche = rechercheField.getText().toLowerCase();
        String clientSelectionne = filtreClient.getValue();
        String produitSelectionne = filtreProduit.getValue();
        LocalDate dateDebutValue = dateDebut.getValue();
        LocalDate dateFinValue = dateFin.getValue();
        
        ObservableList<HistoriqueVente> ventesFiltered = FXCollections.observableArrayList();
        
        for (HistoriqueVente vente : historiqueListComplete) {
            boolean correspond = true;
            
            // Filtre par recherche textuelle
            if (recherche != null && !recherche.isEmpty()) {
                boolean rechercheOk = vente.getNumeroFacture().toLowerCase().contains(recherche) ||
                                    vente.getNomClient().toLowerCase().contains(recherche) ||
                                    vente.getNomProduit().toLowerCase().contains(recherche);
                if (!rechercheOk) correspond = false;
            }
            
            // Filtre par client
            if (clientSelectionne != null && !clientSelectionne.isEmpty()) {
                if (!vente.getNomClient().equals(clientSelectionne)) {
                    correspond = false;
                }
            }
            
            // Filtre par produit
            if (produitSelectionne != null && !produitSelectionne.isEmpty()) {
                if (!vente.getNomProduit().equals(produitSelectionne)) {
                    correspond = false;
                }
            }
            
            // Filtre par date début
            if (dateDebutValue != null) {
                if (vente.getDateVente().isBefore(dateDebutValue)) {
                    correspond = false;
                }
            }
            
            // Filtre par date fin
            if (dateFinValue != null) {
                if (vente.getDateVente().isAfter(dateFinValue)) {
                    correspond = false;
                }
            }
            
            if (correspond) {
                ventesFiltered.add(vente);
            }
        }
        
        historiqueList.clear();
        historiqueList.addAll(ventesFiltered);
        mettreAJourStatistiques();
    }
    
    private void mettreAJourStatistiques() {
        totalVentesNumber.setText(String.valueOf(historiqueList.size()));
        double chiffreAffaires = historiqueList.stream().mapToDouble(HistoriqueVente::getMontantTotal).sum();
        chiffreAffairesNumber.setText(String.format("%.2f", chiffreAffaires));
    }

    @FXML
    public void actualiserHistorique() {
        System.out.println("Actualisation de l'historique");
        chargerClients();
        chargerHistorique();
        // Réinitialiser les filtres
        rechercheField.clear();
        filtreClient.setValue(null);
        filtreProduit.setValue(null);
        dateDebut.setValue(null);
        dateFin.setValue(null);
    }
}