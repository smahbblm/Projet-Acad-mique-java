package com.stock.Controler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import  com.stock.model.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.w3c.dom.Text;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class MagasinierController {
    @FXML
    private Button btnFiltre;
    private TextField barreRecherche;
    @FXML
    private HBox ZoneH;
    @FXML
    private VBox formulEntrée;
    @FXML
    private VBox formulSortie;
    @FXML
private TableView<Produit> mouvementsTable;
    @FXML
private TableColumn<Produit,String> colReference;

    @FXML
private TableColumn<Produit,String> colProduit;
    @FXML
private TableColumn<Produit,Double> colQuantite;
    @FXML
private TableColumn<Produit, Date> colDate;

public  void initialize(){
    colReference.setCellValueFactory(new PropertyValueFactory<>("reference"));

    colProduit.setCellValueFactory(new PropertyValueFactory<>("description"));
    colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
    colDate.setCellValueFactory(new PropertyValueFactory<>("dateAjout"));
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
        colDate.setCellFactory(column->new TableCell<Produit,Date>(){
            protected void updateItem(Date item,boolean empty){
                super.updateItem(item,empty);
                if (empty || item == null){
                    setText(null);
                }
                else{
                    setText(sdf.format(item));
                }
            }

        });
        try{
            Date date=sdf.parse("12-06-2025");
            mouvementsTable.getItems().addAll(
                    new Produit("MM12345","lait",23,date),
                    new Produit("M12345","téléphone",25,date)
                    );

        } catch (ParseException e) {
            e.printStackTrace();
        }



}
    public void chercher(){
        String recherche=barreRecherche.getText().toLowerCase();
        ObservableList<Produit> produitesFiltres= FXCollections.observableArrayList();
        for(Produit P: mouvementsTable.getItems()){
            if(P.getReference().toLowerCase().contains(recherche) || P.getDescription().toLowerCase().contains(recherche)){
                produitesFiltres.add(P);
            }
        }
        mouvementsTable.setItems(produitesFiltres);
        ZoneH.getChildren().remove(barreRecherche);
        ZoneH.getChildren().add(btnFiltre);
    }
@FXML
public void AfficheBarreRecherche(){
     if(barreRecherche==null){
         barreRecherche=new TextField();
         barreRecherche.setPromptText("Recherche par nom de produit,référence");
         barreRecherche.setStyle(
                 "-fx-background-color: white; " +
                         "-fx-border-color: #5B7FE5; " +
                         "-fx-border-radius: 6; " +
                         "-fx-background-radius: 6; " +
                         "-fx-padding: 8 12; " +
                         "-fx-font-size: 13px;"
         );
         barreRecherche.setPrefWidth(400);
         ZoneH.getChildren().remove(btnFiltre);
         ZoneH.getChildren().addAll(barreRecherche);
         barreRecherche.setOnAction(event ->{
              chercher();
         });


     }
}
public void afficherFormuleEntre(){
        formulSortie.setVisible(false);      // ← AJOUTER CETTE LIGNE
        formulEntrée.getChildren().clear();
        formulEntrée.setVisible(true);
        Label titre=new Label("Bon Entrée");
        titre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        ComboBox<Produit> combProduit=new ComboBox<>();
        combProduit.setPromptText("choisir un produit");
        combProduit.setPrefWidth(350);
        combProduit.setStyle("-fx-font-size: 13px; -fx-pref-height: 35px;");

        /*
        try{
            List<Produit> list=produitDAO.getALLProduit();
            combProduit.getItems().addAll(list);
        }catch(SQLException e){
            e.printStackTrace();
        }

         */
        TextField quantite=new TextField();
        quantite.setPromptText("Quantité ajoutée");
        quantite.setPrefWidth(350);
        quantite.setStyle("-fx-font-size: 13px; -fx-pref-height: 35px;");
        HBox boutons=new HBox(10);
        boutons.setAlignment(Pos.CENTER);
    Button btnValider = new Button("Valider");
    btnValider.setPrefWidth(120);
    btnValider.setStyle(
            "-fx-background-color: #27ae60; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 14px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-padding: 10 20; " +
                    "-fx-background-radius: 8; " +
                    "-fx-cursor: hand;"
    );
    Button btnAnnuler = new Button("Annuler");
    btnAnnuler.setPrefWidth(120);
    btnAnnuler.setStyle(
            "-fx-background-color: #e74c3c; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 14px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-padding: 10 20; " +
                    "-fx-background-radius: 8; " +
                    "-fx-cursor: hand;"
    );
    btnAnnuler.setOnAction(e -> formulEntrée.setVisible(false));
         boutons.getChildren().addAll(btnAnnuler,btnValider);
         formulEntrée.getChildren().addAll(titre,combProduit,quantite,boutons);

    }
    public void afficheFormuleSortie(){
        formulEntrée.setVisible(false);
        formulSortie.getChildren().clear();
        formulSortie.setVisible(true);

        // Titre
        Label titre = new Label("Bon Sortie");
        titre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // ComboBox Produit
        ComboBox<Produit> combProduit = new ComboBox<>();
        combProduit.setPromptText("choisir un produit");
        combProduit.setPrefWidth(350);
        combProduit.setStyle("-fx-font-size: 13px; -fx-pref-height: 35px;");


        TextField quantite = new TextField();
        quantite.setPromptText("Quantité éliminée");
        quantite.setStyle("-fx-font-size: 13px; -fx-pref-height: 35px;");



        HBox boutons = new HBox(10);
        boutons.setAlignment(javafx.geometry.Pos.CENTER);

        Button btnAnnuler = new Button("Annuler");
        btnAnnuler.setPrefWidth(120);
        btnAnnuler.setStyle(
                "-fx-background-color: #95a5a6; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 20; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
        btnAnnuler.setOnAction(e -> formulSortie.setVisible(false));



        Button btnValider = new Button("Valider");
        btnValider.setPrefWidth(120);
        btnValider.setStyle(
                "-fx-background-color: #e74c3c; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 20; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
        boutons.getChildren().addAll(btnAnnuler,btnValider);
        formulSortie.getChildren().addAll(titre,combProduit,quantite,boutons);
    }






}
