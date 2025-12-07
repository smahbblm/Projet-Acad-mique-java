package com.stock.ui.Controller.chef_vents.Clients;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    @FXML private Button btnAjouter;
    @FXML private Button btnAnnuler;

    //la méthode initialize

    public AjouterClientController(){
        System.out.print("je suis le controlleur forme ajoute client");
    }
    public void initialize(URL url, ResourceBundle  localisation){
        System.out.println("début de chargement de fichier fxml");
        //Initialiser la date d'inscription à aujourd'hui
        dateInscription.setValue(LocalDate.now());
    }
    // la méthode ajouterClient et annuler
    @FXML
    public void ajouterClient(){
        System.out.println("je suis la fonction ajouter un client dans la base de données");
        //Récupérer les données du formulaire
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String adresse = txtAdresse.getText();
        String telephone = txtTelephone.getText();
        String email = txtEmail.getText();
        LocalDate DATE_INSCRIPTION = dateInscription.getValue();
        
        System.out.println("Client: " + nom + " " + prenom + " - " + email);
        //TODO: Envoyer au backend
    }
    
    @FXML
    public void annuler(){
        System.out.println("Annulation");
        //TODO: Fermer la fenêtre ou réinitialiser le formulaire
    }
}
