package com.stock.ui.Controller.chef_vents.Clients;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

public class AjouterClientController implements Initializable {

    //l'interface Initializable contient deux méthodes inisialize().
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextArea  txtAdresse;
    @FXML private TextField txtTelephone;
    @FXML private TextField txtEmail;
    @FXML private DatePicker dateInscription;
    //les deux variables de deux fonction
    @FXML private TextField btnAjouter;
    @FXML private TextField btnAnnuler;

    //la méthode initialize
    @Override
    public AjouterClientController(){
        System.out.print("je suis le controlleur forme ajoute client");
    }
    public void initialize(URL url, ResourceBundle  localisation){
        System.out.println("début de chargement de fichier fxml");
        //est ce que la récuperation se fait dans à l'intérieur de cette fonction ou bien en dehors decette fonctions
        // c'est la phase de récupération des données de forme
        //pour la date de l'inscription
        dateInscription.setValue(LocalDate.now());
        String nom =  txtNom.getText();
        String prenom = txtPrenom.getText();
        String adresse = txtAdresse.getText();
        String telephone = txtTelephone.getText();
        String email = txtEmail.getText();
        String data_inscription = dateInscription.getValue().toString();
        LocalDate DATE_INSCRIPTION = dateInscription.getValue();
        System.out.println("date d'inscription sous forme de chaine de caractères : "+data_inscription+"date d'inscription sous forme localdte:" + DATE_INSCRIPTION);
        ajouterClient(nom,prenom,adresse,telephone,email,DATE_INSCRIPTION);
    }

    // la méthode ajouterClient et annuler
    @FXML
    public void ajouterClient(String nom, String prenom,String adresse,String telephone,String email, LocalDate DATE_INSCRIPTION){
        System.out.println("je suis la fonction ajouter un client dans la base de données");
        //pour ajouter un client dans la base de donnée on doit récupérer les données de forme
        // la création de  la requette qui va etre envoyé au backend pour l'ajouter dans la base de donnée
        //  comment cet client va etre ajouter à la base de données

    }
}
