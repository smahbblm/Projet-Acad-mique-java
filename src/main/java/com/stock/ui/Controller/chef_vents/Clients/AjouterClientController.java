package com.stock.ui.Controller.chef_vents.Clients;

import com.stock.model.partenaire.Client;
import com.stock.service.ClientService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AjouterClientController implements Initializable {

    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextArea  txtAdresse;
    @FXML private TextField txtTelephone;
    @FXML private TextField txtEmail;
    @FXML private DatePicker dateInscription;
    @FXML private Button btnAjouter;
    @FXML private Button btnAnnuler;
    
    private Client clientToEdit;
    private boolean isEditMode = false;

    public AjouterClientController(){
        System.out.print("je suis le controlleur forme ajoute client");
    }
    
    public void initialize(URL url, ResourceBundle  localisation){
        System.out.println("début de chargement de fichier fxml");
        dateInscription.setValue(LocalDate.now());
    }
    
    public void setClientToEdit(Client client) {
        this.clientToEdit = client;
        this.isEditMode = true;
        
        txtNom.setText(client.getNom());
        txtPrenom.setText(client.getPrenom());
        txtAdresse.setText(client.getAdresse());
        txtTelephone.setText(client.getTelephone());
        txtEmail.setText(client.getEmail());
        if (client.getDateInscription() != null) {
            dateInscription.setValue(client.getDateInscription());
        }
        
        btnAjouter.setText("Modifier");
    }
    
    @FXML
    public void ajouterClient(){
        System.out.println("je suis la fonction ajouter un client dans la base de données");
        
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String adresse = txtAdresse.getText();
        String telephone = txtTelephone.getText();
        String email = txtEmail.getText();
        
        if (nom.isEmpty() || email.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs obligatoires");
            alert.setHeaderText("Veuillez remplir tous les champs obligatoires");
            alert.setContentText("Nom et Email sont obligatoires");
            alert.showAndWait();
            return;
        }
        
        try {
            ClientService clientService = new ClientService();
            
            if (isEditMode) {
                clientToEdit.setNom(nom);
                clientToEdit.setPrenom(prenom);
                clientToEdit.setAdresse(adresse);
                clientToEdit.setTelephone(telephone);
                clientToEdit.setEmail(email);
                
                boolean modifReussie = clientService.updateClient(clientToEdit);
                
                if (modifReussie) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Succès");
                    alert.setHeaderText("Client modifié avec succès");
                    alert.showAndWait();
                    fermerFenetre();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setContentText("Impossible de modifier le client");
                    alert.showAndWait();
                }
            } else {
                Client nouveauClient = new Client(nom, prenom, "", adresse, telephone, email);
                boolean ajoutReussi = clientService.addClient(nouveauClient);
                
                if (ajoutReussi) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Succès");
                    alert.setHeaderText("Client ajouté avec succès");
                    alert.showAndWait();
                    fermerFenetre();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setContentText("Impossible d'ajouter le client");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    @FXML
    public void annuler(){
        System.out.println("Annulation");
        fermerFenetre();
    }
    
    private void fermerFenetre() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }
}
