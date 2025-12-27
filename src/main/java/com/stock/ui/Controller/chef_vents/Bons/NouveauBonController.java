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
        //la récuperation de données de form
        String nombons = numeroidbon.getText();
        LocalDate  dateLivraison = dateLivraisonBon.getValue();
        String status = statutCombo.getValue();
        String adresseLivraison = observationsField.getText();
        String notes = observationsField.getText();
        int res =BonService.ajouterbons( nombons,  dateLivraison ,status, adresseLivraison, notes );
        if (res > 0){
            String message = " le bon est bien ajouter à la base  de donées ";
            TextArea resultArea = new TextArea();
            resultArea.setText(message);
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









