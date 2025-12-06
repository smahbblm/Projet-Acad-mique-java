package com.stock.Controler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;


public class menu {
    @FXML
    private Button mybouton ;
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



}
