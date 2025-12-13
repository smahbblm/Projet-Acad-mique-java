package com.stock.Controler;

import com.stock.dao.implementation.MouvementStockDAO;
import com.stock.dao.interfaces.IMouvementStockDAO;
import com.stock.model.stock.MouvementStock;
import com.stock.model.produit.Produit;
import com.stock.service.StockService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

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
private TableView<MouvementStock> mouvementsTable;
    @FXML
private TableColumn<MouvementStock,String> colReference;


    @FXML
private TableColumn<MouvementStock,Double> colQuantite;
    @FXML
private TableColumn<MouvementStock, LocalDate> colDate;
    @FXML
private TableColumn<MouvementStock,String> colType;
@FXML
Label TotalMv;
@FXML
Label NbrEntree;
@FXML
Label Nbrsortie;
private StockService stockSer;
private IMouvementStockDAO mouvementStockDAO;
public MagasinierController(){
    try{
       this.stockSer=new StockService();
       this.mouvementStockDAO=new MouvementStockDAO();
    }catch(Exception e){
        e.printStackTrace();
    }

}
public  void initialize(){


     TotalMv.setText(String.valueOf(stockSer.getNombre_mouvement()));
     try{
         NbrEntree.setText(String.valueOf(stockSer.getNbrMouvementsEntree()));
         Nbrsortie.setText(String.valueOf(stockSer.getNbrMouvementsSortie()));
     }catch(Exception e){
         e.printStackTrace();
     }
    colReference.setCellValueFactory(new PropertyValueFactory<>("reference"));
    colReference.setStyle("-fx-alignment: CENTER;");

    colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
    colType.setCellValueFactory(new PropertyValueFactory<>("typeMouvement"));
    colDate.setCellValueFactory(new PropertyValueFactory<>("dateMouvement"));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    colDate.setCellFactory(column -> new TableCell<MouvementStock, LocalDate>() {
        @Override
        protected void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(item.format(formatter));
            }
        }
    });
        try{
            List<MouvementStock> mouvements =stockSer.consulterTousMouvements();
            ObservableList<MouvementStock> data = FXCollections.observableArrayList(mouvements);
            mouvementsTable.setItems(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }




}
    public void chercher(){
        String recherche=barreRecherche.getText().toLowerCase();
        ObservableList<MouvementStock> produitesFiltres= FXCollections.observableArrayList();
        for(MouvementStock M: mouvementsTable.getItems()){
            if(M.getReference().toLowerCase().contains(recherche) ){
                produitesFiltres.add(M);
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
