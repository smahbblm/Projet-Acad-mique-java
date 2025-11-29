package com.stock.ui.Controller.chef_vents.Bons;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import com.stock.Service.BonService;
import java.time.LocalDate;
import  java.lang.String;
import com.stock.api.ApisBons;

public class NouveauBonController {

    @FXML private TextField numeroidbon;
    @FXML private DatePicker dateCommandeBon;
    @FXML private DatePicker dateLivraisonBon;
    @FXML private ComboBox<String> fournisseurCombo;
    @FXML private ComboBox<String>  statutCombo;
    @FXML private TextField montantField;
    @FXML private Button btnbonmoin;
    // pour  contoller le champ de text
    @FXML private TextArea observationsField;
    //pour le boitton  d'annulation d'ajoute de bon
    @FXML private Button annulerButton;
    @FXML private Button enregistrerBtn;
    ApisBons apisBons =  new ApisBons();

    // cette objet est utilisé pour faire appele à  la fonction annuler dans les apis
    BonService servicebon =  new BonService(apisBons);

     public NouveauBonController(){
         System.out.println("je suis exactement le controller du votre fenetre d'ajoute de bons de livraison . ");
     }
    public void initialize() {
        System.out.println("je suis exactement le point d'entré  de voitre controller");
    }


    // la définition de  tous les éléments fxml dans notre projet
    @FXML
    public void enregistrer(){
        System.out.println("la fonction enregistrer pour enregistre un bon de la table Bon dans DB");
        //la récuperation de données de form
        String nombons = numeroidbon.getText();
        LocalDate dateCommande = dateCommandeBon.getValue();
        LocalDate  dateLivraison = dateLivraisonBon.getValue();
        ObservableList<String> listefournisse = fournisseurCombo.getItems();
        ObservableList<String> status = statutCombo.getItems();
        String montant = montantField.getText();
        String notes = observationsField.getText();
        servicebon.ajouterbons( nombons,   dateCommande,  dateLivraison ,  listefournisse, status ,  montant, notes );
    }

    @FXML
    public void annuler(){
        System.out.println("je suis la fonction  annuler l'ajouter de  bon  dans la base de données");
    }
    @FXML
    public void ssupprimebon(){
         System.out.println("la supprission de bon depuis la base de données");
         servicebon.ssupprimebon();
    }
}


