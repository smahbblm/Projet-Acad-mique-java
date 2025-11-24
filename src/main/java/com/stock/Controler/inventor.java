package com.stock.Controler;

import com.stock.model.LigneInventaire;
import com.stock.model.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class inventor {
@FXML
private VBox affichage;
    @FXML
    private Label date;
    @FXML
    private VBox listInventor;
    @FXML
    private VBox Visible;
    @FXML
    private TableView<LigneInventaire> TableProduit;
    @FXML
    private  TableColumn<Produit,String> colProduit;
    @FXML
    private TableColumn<Produit,String> colRef;
    @FXML
    private TableColumn<Produit,Integer> colQtéSys;
    @FXML
    private TableColumn<LigneInventaire,Integer> colQtéRel;
    @FXML
    private TableColumn<LigneInventaire,Integer> colEcart;
   @FXML
   private  TextField RechecheProd;

public void initialize(){
     Date today=new Date();
    SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
    date.setText(sdf.format(today));

    TableProduit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    listInventor.setVisible(true);
    Visible.setVisible(false);
    affichage.setVisible(false);
    colProduit.setCellValueFactory(new PropertyValueFactory<>("description"));
    colRef.setCellValueFactory(new PropertyValueFactory<>("reference"));
    colQtéSys.setCellValueFactory(new PropertyValueFactory<>("quantite"));
    colEcart.setCellValueFactory(new PropertyValueFactory<>("ecart"));
    colQtéRel.setCellFactory(
            TextFieldTableCell.forTableColumn(new IntegerStringConverter())
    );
    colQtéRel.setOnEditCommit(event ->{
         LigneInventaire li=event.getRowValue();
         li.setQuantiteReelle(event.getNewValue());
         li.calculerEcart();
         TableProduit.refresh();

    });
}
public void AfficheInventor(){
     affichage.setVisible(true);
    listInventor.setVisible(false);
}
public  void AnnuelForm(){
    affichage.setVisible(false);
}
public  void Démarrage_inventor(){
       listInventor.setVisible(false);
       Visible.setVisible(true);
}
public void RechercheProd(){
    String search=RechecheProd.getText().toLowerCase();
    ObservableList<LigneInventaire> listfiltrer= FXCollections.observableArrayList();
    for(LigneInventaire Li:TableProduit.getItems()){
        if(Li.getProduit().getDescription().contains(search)){
             listfiltrer.add(Li);
        }
    }
    TableProduit.setItems(listfiltrer);

}
  public void  displayTable(){
     Visible.setVisible(false);
  }
}
