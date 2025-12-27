package com.stock.Controler;

import com.stock.model.utilisateur.Utilisateur;
import com.stock.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;



public class menu {
    @FXML
    private Label labelNomMagasinier;

    @FXML
    private Button mybouton ;
    @FXML


    public void initialize() {
        try {
            AuthService authService = new AuthService();
            Utilisateur u = authService.getCurrentUser();

            if (u != null) {
                labelNomMagasinier.setText(u.getNom() + " " + u.getPrenom());
            } else {
                labelNomMagasinier.setText(""); // fallback si personne connecté
            }
        } catch (Exception e) {
            e.printStackTrace();
            labelNomMagasinier.setText("");
        }
    }

    @FXML
    private  void pageMouvement(ActionEvent event) throws IOException{
       Parent root= FXMLLoader.load(getClass().getResource("/fxml/Magasinier/mouvements.fxml"));
       Scene scene=new Scene(root);
       Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

     // Afficher la nouvelle scène
     stage.setScene(scene);
     stage.show();
   }
   @FXML
   private void  pageInventaire(ActionEvent event) throws  IOException{
     Parent root=FXMLLoader.load(getClass().getResource("/fxml/Magasinier/inventaire.fxml"));
     Scene scene=new Scene(root);
     Stage stage=(Stage)((Node) event.getSource()).getScene().getWindow();
     stage.setScene(scene);
     stage.show();
 }
  @FXML
  private void pageDashboard(ActionEvent event) throws  IOException{
        Parent root=FXMLLoader.load(getClass().getResource("/fxml/Magasinier/dashboardMagasinier.fxml"));
        Scene scene=new Scene(root);
        Stage stage=(Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void  pageRapport(ActionEvent event) throws  IOException{
        Parent root=FXMLLoader.load(getClass().getResource("/fxml/Magasinier/rapports.fxml"));
        Scene scene=new Scene(root);
        Stage stage=(Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void pageValidationBons(ActionEvent event) throws IOException{
        Parent root=FXMLLoader.load(getClass().getResource("/fxml/Magasinier/sorties.fxml"));
        Scene scene=new Scene(root);
        Stage stage=(Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            AuthService authService = new AuthService();
            authService.logout();


            FXMLLoader loader = new FXMLLoader(getClass().getResource(""));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur lors de la déconnexion !").show();
        }
    }



}
