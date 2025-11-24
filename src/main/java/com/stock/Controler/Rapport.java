package com.stock.Controler;

import com.stock.model.LigneInventaire;
import com.stock.model.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Rapport {
    @FXML
    private TableView<LigneInventaire> TableProduit;
    @FXML
    private TableColumn<Produit,String> colProduit;
    @FXML
    private TableColumn<Produit,String> colRef;
    @FXML
    private TableColumn<Produit,Integer> colQtéSys;
    @FXML
    private TableColumn<LigneInventaire,Integer> colQtéRel;
    @FXML
    private TableColumn<LigneInventaire,Integer> colEcart;
    @FXML
    private TextField RechecheProd;

    public void initialize(){


        TableProduit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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


}
