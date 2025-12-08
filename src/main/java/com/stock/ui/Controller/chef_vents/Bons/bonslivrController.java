package com.stock.ui.Controller.chef_vents.Bons;

import com.stock.model.DaoBon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextField;
import com.stock.Service.BonService;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class bonslivrController {


    //La  définition des  elements  fxml  dans notre projet
    @FXML  private Button supprimerbon;
    @FXML  private TextField recherchebon;
    @FXML  private Button btnbonplus;


    public void initialize() {
        System.out.println("je suis exactement le controller du votre  fenetre de gestion de Clients . ");

    }
    @FXML
    public void ajoutebon(){
        //chargement de fichier fxml  pour  ajouter un bon de livraison
        System.out.println("chargement du formulaire");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/Bons_liv/NouveauBon.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnbonplus.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        }catch(IOException e){
            e.printStackTrace();
        };
    }
    //la méthode pour la supprission d'un  bon de livraison
    @FXML
    public void supprimebon() {
        BonService bonservice = new BonService();
        List<String> listeBons = bonservice.liste_numero_bonliv();
        System.out.println("listeBons");

        // 2. Créer un dialog de choix
        ChoiceDialog<String> dialog = new ChoiceDialog<>(listeBons.get(0), listeBons);
        dialog.setTitle("Supprimer un bon");
        dialog.setHeaderText("Sélectionner le bon à supprimer");
        dialog.setContentText("Numéro du bon:");

        // 3. Afficher et récupérer le choix
        Optional<String> result = dialog.showAndWait();

        // 4. Si l'utilisateur a choisi
        if (result.isPresent()) {
            String numeroChoisi = result.get();
            bonservice.supprimerBon(numeroChoisi);
            System.out.println("Bon supprimé : " + numeroChoisi);
        }
    }



}
