package com.stock.Controler;

import com.stock.dao.implementation.UtilisateurDAO;
import com.stock.model.document.BonCommande;
import com.stock.model.document.LigneCommande;
import com.stock.model.stock.MouvementStock; // ✅ c'est celui qu'il faut

import com.stock.model.produit.Produit;
import com.stock.model.utilisateur.Magasinier;
import com.stock.model.utilisateur.Utilisateur;
import com.stock.service.CommandeService;
import com.stock.service.LigneCommandeService;
import com.stock.service.ProduitService;
import com.stock.service.StockService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class ValidationMagasinierController {
    @FXML
    private TableView<BonCommande> tableCommandes;
    @FXML private TableColumn<BonCommande, String> colNumeroCommande;
    @FXML private TableColumn<BonCommande, LocalDate> colDateCommande;
    @FXML private TableColumn<BonCommande, String> colFournisseur;
    @FXML private TableColumn<BonCommande, String> colStatutCommande;
    @FXML private TableColumn<BonCommande, Double> colMontantCommande;
    @FXML private TableColumn<BonCommande, Integer> colNbProduitsCommande;
    @FXML private TableColumn<BonCommande, Void> colActionCommande;
    private CommandeService commandeService;
    private ProduitService produitService;
    private StockService mouvementStockService;
    private LigneCommandeService ligneCommandeService;


    public  ValidationMagasinierController(){
         try{
             this.commandeService=new CommandeService();
             this.mouvementStockService=new StockService();
             this.produitService=new ProduitService();
             this.ligneCommandeService=new LigneCommandeService();
         }catch(Exception e){
             e.printStackTrace();
         }
    }
    @FXML
    public void initialize()  {
        colNumeroCommande.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colDateCommande.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));
        colFournisseur.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFournisseur().getRaisonSociale())
        );

        colStatutCommande.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colMontantCommande.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        colNbProduitsCommande.setCellValueFactory(new PropertyValueFactory<>("nbProduits"));
        colActionCommande.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Valider");

            {
                btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                btn.setOnAction(e -> {
                    BonCommande bc = getTableView().getItems().get(getIndex());
                    validerBonCommande(bc);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(btn);
            }
        });

        loadCommandes();
    }
    public void loadCommandes() {
        try {
            List<BonCommande> commandes = commandeService.consulterTousLesBonsCommande();
            // filtrer uniquement les commandes non validées
            List<BonCommande> commandesNonValides = commandes.stream()
                    .filter(c -> c.getStatut().equals("EN_ATTENTE") || c.getStatut().equals("TRANSMISE"))
                    .toList();

            ObservableList<BonCommande> data = FXCollections.observableArrayList(commandesNonValides);
            tableCommandes.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            // afficher une alerte à l'utilisateur
        }
    }
    private void validerBonCommande(BonCommande bc) {
        try {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
            Utilisateur magasinierConnecte =utilisateurDAO.findByEmail("magasin@test.com");

            // 1. récupérer toutes les lignes du bon
            List<LigneCommande> lignes = ligneCommandeService.getLignesByBonCommande(bc.getIdBonCommande());
            if (magasinierConnecte == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Utilisateur magasinier introuvable !");
                alert.show();
                return;
            }
            for (LigneCommande ligne : lignes) {
                Produit p = produitService.getProduitById(ligne.getProduit().getIdProduit());
                int stockAvant = p.getQuantiteStock();
                int stockApres = stockAvant + ligne.getQuantite();

                // 2. créer mouvement_stock
                MouvementStock mv = new MouvementStock();
                mv.setProduit(p); // 🔹 important
                mv.setTypeMouvement("ENTREE");
                mv.setQuantite(ligne.getQuantite());
                mv.setStockAvant(stockAvant);
                mv.setStockApres(stockApres);
                mv.setReference(p.getReference());
                mv.setUtilisateur(magasinierConnecte);
                mv.setDateMouvement(LocalDate.now());

                mouvementStockService.enregistrerEntree(mv);



                // 3. mettre à jour le stock du produit
                p.setQuantiteStock(stockApres);
                produitService.mettreAJourStock(p);
            }

            // 4. mettre à jour le statut du bon de commande
            bc.setStatut("VALIDEE");
            commandeService.mettreAJourStatut(bc);

            // 5. recharger la table
            loadCommandes();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Bon de commande validé avec succès !");
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la validation du bon !");
            alert.show();
        }
    }




}
