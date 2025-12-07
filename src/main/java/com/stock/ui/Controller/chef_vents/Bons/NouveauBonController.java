package com.stock.ui.Controller.chef_vents.Bons;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import com.stock.Service.BonService;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import  java.lang.String;


public class NouveauBonController {

    //la déclarations des éléments du fxml pour les controllées
    @FXML private TextField numeroidbon;
    @FXML private DatePicker dateLivraisonBon;
    @FXML private ComboBox<String>  statutCombo;
    @FXML private TextArea observationsField;
    @FXML private Button annulerBtn;
    @FXML private Button enregistrerBtn;

    public NouveauBonController(){
        System.out.println("je suis le contructeur de la nouvelle bon de livraison ");
    }

    BonService bonService = new BonService();
    public void initialize() {

        System.out.println("je suis exactement le point d'entré  de voitre controller");
    }

    // la définition de  tous les éléments bonLivraison.fxml

    @FXML
    public void enregistrer(){
        System.out.println("la fonction enregistrer pour enregistre un bon de la table Bon dans DB");
        //la récuperation de données de form
        String nombons = numeroidbon.getText();
        LocalDate  dateLivraison = dateLivraisonBon.getValue();
        String status = statutCombo.getValue();
        String notes = observationsField.getText();
        int res =bonService.ajouterbons( nombons,  dateLivraison ,status, notes );
        if (res > 0){
            String message = " le bon est bien ajouter à la base  de donées ";
            TextArea resultArea = new TextArea();
            resultArea.setText(message);
        }
    }
    @FXML
    public void annuler(){
        System.out.println("je suis la fonction  annuler l'ajouter de  bon  dans la base de données");
    }
    @FXML
    public void supprimebon(){
         System.out.println("la supprission de bon depuis la base de données");
         bonService.supprimebon();
    }
}



