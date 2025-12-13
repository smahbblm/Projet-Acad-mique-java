package com.stock.Controler;


import com.stock.model.produit.Produit;
import com.stock.model.stock.LigneInventaire;
import com.stock.model.stock.Inventaire;
import com.stock.service.InventaireService;
import com.stock.service.LigneInventaireService;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
    private  TableColumn<LigneInventaire,String> colProduit;
    @FXML
    private TableColumn<LigneInventaire,String> colRef;
    @FXML
    private TableColumn<LigneInventaire,Integer> colQtéSys;
    @FXML
    private TableColumn<LigneInventaire,Integer> colQtéRel;
    @FXML
    private TableColumn<LigneInventaire,Integer> colEcart;

   @FXML
   private Button validerIn;
   @FXML
   private TextArea Observ;
   @FXML
   private MenuButton statusMenu;
   @FXML
   private DatePicker date_inventor;
   @FXML
   private  Button  valide;
   @FXML
   private Label IdInventor;
    private Inventaire inventaireCourant;
    private InventaireService inventorServcie;
   private LigneInventaireService inventorLigneS;
    private ObservableList<LigneInventaire> masterData;
   public inventor() {
       try{
          this.inventorServcie=new InventaireService();
          this.inventorLigneS =new  LigneInventaireService();
       } catch (Exception e) {
           throw new RuntimeException(e);
       }

   }



    public void initialize(){
        TableProduit.setEditable(true);

        Date today=new Date();
        try {
            listInventor.getChildren().clear();

            List<Inventaire> listeInventaires = inventorServcie.consulterTousLesInventaires();

            for (Inventaire inventaire : listeInventaires) {

                // HBox principale (la carte)
                HBox card = new HBox(10);
                card.getStyleClass().add("inventor-cart");

                // La barre colorée à gauche
                Region colorRegion = new Region();
                colorRegion.getStyleClass().add("color_gauche");

                // Le bloc de texte
                VBox info = new VBox(5);

                Label titre = new Label("Inventaire " + inventaire.getIdInventaire());
                Label date = new Label("Date : " + inventaire.getDateInventaire());
                Label desc = new Label("Description : " +
                        (inventaire.getObservations() == null ? "Aucune" : inventaire.getObservations()));

                info.getChildren().addAll(titre, date, desc);

                // On construit la carte
                card.getChildren().addAll(colorRegion, info);

                // On ajoute la carte dans le VBox parent
                listInventor.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
    date.setText(sdf.format(today));
    for (MenuItem item:statusMenu.getItems()){
         item.setOnAction(e ->{
              statusMenu.setText(item.getText());
         });
    }
    TableProduit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    listInventor.setVisible(true);
    Visible.setVisible(false);
    affichage.setVisible(false);
    colProduit.setCellValueFactory(cellData ->
            new ReadOnlyStringWrapper(cellData.getValue().getProduit().getDescription())

            );
    colRef.setCellValueFactory(cellData ->
            new ReadOnlyStringWrapper(cellData.getValue().getProduit().getReference()));
    colRef.setStyle("-fx-alignment: CENTER;");

        colQtéSys.setCellValueFactory(cellData ->
            new ReadOnlyIntegerWrapper(cellData.getValue().getProduit().getQuantiteStock()).asObject());
        colQtéSys.setStyle("-fx-alignment: CENTER;");
        colEcart.setCellValueFactory(cellData->
            new ReadOnlyIntegerWrapper(cellData.getValue().getEcart()).asObject());
        colEcart.setStyle("-fx-alignment: CENTER;");

        colQtéRel.setCellValueFactory(cellData->
                new ReadOnlyIntegerWrapper(cellData.getValue().getQuantiteReelle()).asObject());
    colQtéRel.setCellFactory(
            TextFieldTableCell.forTableColumn(new IntegerStringConverter())
    );
    colQtéRel.setEditable(true);
    colQtéRel.setStyle("-fx-alignment: CENTER;");
    colQtéRel.setOnEditCommit(event ->{
         LigneInventaire li=event.getRowValue();
         li.setQuantiteReelle(event.getNewValue());
         li.calculerEcart();
         TableProduit.refresh();
        TableProduit.edit(event.getTablePosition().getRow(), colQtéRel);

    });


}



    @FXML
    public void Enregistrer(ActionEvent event) throws Exception{
        inventaireCourant=new Inventaire();

        LocalDate dateInventor=date_inventor.getValue();
        String status=statusMenu.getText();
        String observation=Observ.getText();
        inventaireCourant.setDateInventaire(dateInventor);
        inventaireCourant.setStatut(status);
        inventaireCourant.setObservations(observation);
        IdInventor.setText("Inventaire " + inventaireCourant.getIdInventaire());
           inventorServcie.creerInventaire( inventaireCourant);
           System.out.println("Inventaire créee avec succées ");
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("succés");
        alert.setHeaderText(null);
        alert.setContentText("inventaire a été créer avec succés");
        alert.showAndWait();
        date_inventor.setValue(null);
        statusMenu.setText("status");
        Observ.clear();
    }
    @FXML
    public void ValiderLigne(ActionEvent event) {
        if (inventaireCourant == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Veuillez créer d'abord un inventaire !");
            alert.showAndWait();
            return;
        }


        for (LigneInventaire ligne : TableProduit.getItems()) {
            ligne.setInventaire(inventaireCourant);


            try {
                inventorLigneS.enregistrerLigneInvetor(ligne);
                inventorServcie.ChangerStatut(inventaireCourant.getIdInventaire(),"Terminé");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Les lignes d'inventaire ont été enregistrées avec succès.");
        alert.setTitle("Succès !");
        alert.showAndWait();
    }
public void AfficheInventor(){
     affichage.setVisible(true);
    listInventor.setVisible(false);
}
public  void AnnuelForm(){
    affichage.setVisible(false);
}


  public void  displayTable(){
     Visible.setVisible(false);
  }
  public void Démarrage_inventor() throws Exception{
      listInventor.setVisible(false);
      Visible.setVisible(true);
       ObservableList<LigneInventaire> lignes=inventorServcie.generateLigneInventaire();
       TableProduit.setItems(lignes);

  }

}

