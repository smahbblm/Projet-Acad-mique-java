package com.stock.ui.Controller.chef_vents.Bons;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import com.stock.service.BonService;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import  java.lang.String;


public class NouveauBonController {

    //la déclarations des éléments du fxml pour les controllées
    @FXML private TextField numeroidbon;
    @FXML private DatePicker dateLivraisonBon;
    @FXML private ComboBox<String>  statutCombo;
    @FXML private TextField adresseLivraisonField;
    @FXML private ComboBox<String> clientCombo;
    @FXML private ComboBox<String> bonSortieCombo;
    @FXML private ComboBox<String> factureCombo;
    @FXML private TextArea observationsField;
    @FXML private Button annulerBtn;
    @FXML private Button enregistrerBtn;

    public NouveauBonController(){
        System.out.println("je suis le contructeur de la nouvelle bon de livraison ");
    }

    BonService BonService = new BonService();
    public void initialize() {
        System.out.println("je suis exactement le point d'entré  de voitre controller");
        ObservableList<String> clientsList = BonService.getclient();
        clientCombo.setItems(clientsList);
        ObservableList<String> bonSortieList = BonService.getbonsortie();
        bonSortieCombo.setItems(bonSortieList);
        ObservableList<String> list_factures = BonService.list_factures();
        factureCombo.setItems(list_factures);
    }

    // la définition de  tous les éléments bonLivraison.fxml

    @FXML
    public void enregistrer(){
        System.out.println("la fonction enregistrer pour enregistre un bon de la table Bon dans DB");
        
        String nombons = numeroidbon.getText();
        LocalDate dateLivraison = dateLivraisonBon.getValue();
        String status = statutCombo.getValue();
        String adresseLivraison = adresseLivraisonField.getText();
        String notes = observationsField.getText();
        String clientNom = clientCombo.getValue();
        
        if (nombons == null || nombons.isEmpty() || clientNom == null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez remplir tous les champs obligatoires (Numéro et Client)");
            alert.showAndWait();
            return;
        }
        
        try {
            com.stock.dao.implementation.ClientDAO clientDAO = new com.stock.dao.implementation.ClientDAO();
            com.stock.model.partenaire.Client client = clientDAO.findByNom(clientNom).get(0);
            
            com.stock.dao.implementation.BonLivraisonDAO bonDAO = new com.stock.dao.implementation.BonLivraisonDAO();
            com.stock.model.document.BonLivraison bon = new com.stock.model.document.BonLivraison(nombons, dateLivraison, client);
            bon.setStatut(status);
            bon.setAdresseLivraison(adresseLivraison);
            bon.setObservations(notes);
            bonDAO.create(bon);
            
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Bon de livraison ajouté avec succès");
            alert.showAndWait();
            annuler();
        } catch (Exception e) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    @FXML
    public void annuler(){
        System.out.println("Annulation de la saisie du bon de livraison");
        
        // Vider les TextField
        numeroidbon.clear();
        adresseLivraisonField.clear();
        
        // Vider le TextArea
        observationsField.clear();
        
        // Réinitialiser les ComboBox
        statutCombo.setValue(null);
        clientCombo.setValue(null);
        bonSortieCombo.setValue(null);
        factureCombo.setValue(null);
        
        // Réinitialiser le DatePicker
        dateLivraisonBon.setValue(null);
        
        System.out.println("Tous les champs ont été réinitialisés");
    }
    @FXML
    public void supprimebon(){
         System.out.println("la supprission de bon depuis la base de données");
         //BonService.supprimebon();
    }
}









