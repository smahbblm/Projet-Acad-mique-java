package com.stock.Controler;

import com.stock.dao.implementation.UtilisateurDAO;
import com.stock.model.document.*;
import com.stock.model.stock.MouvementStock; // ✅ c'est celui qu'il faut

import com.stock.model.produit.Produit;
import com.stock.model.utilisateur.Magasinier;
import com.stock.model.utilisateur.Utilisateur;
import com.stock.service.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    @FXML
    private TableView<BonLivraison> tableLivraisons;
    @FXML private TableColumn<BonLivraison, String> colNumeroLivraison;
    @FXML private TableColumn<BonLivraison, LocalDate> colDateLivraison;
    @FXML private TableColumn<BonLivraison, String> colClient;
    @FXML private TableColumn<BonLivraison, String> colAdresse;
    @FXML private TableColumn<BonLivraison, String> colStatutLivraison;

    @FXML private TableColumn<BonLivraison, Void> colActionLivraison;


    private CommandeService commandeService;
    private ProduitService produitService;
    private StockService mouvementStockService;
    private LigneCommandeService ligneCommandeService;
    private VenteService livraisonService;
    private BonSortie bonSortie;
    private LigneLivraisonService ligneLivraisonService;
    private BonSortieService  bonSortieService;
    private LigneSortieService ligneSortieService;

    public  ValidationMagasinierController(){
         try{
             this.commandeService=new CommandeService();
             this.mouvementStockService=new StockService();
             this.produitService=new ProduitService();
             this.ligneCommandeService=new LigneCommandeService();
             this.livraisonService=new VenteService();
             this.bonSortieService=new BonSortieService();
             this.ligneLivraisonService=new LigneLivraisonService();
             this.ligneSortieService=new LigneSortieService();
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
        colNumeroLivraison.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colDateLivraison.setCellValueFactory(new PropertyValueFactory<>("dateLivraison"));
        colClient.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClient().getNom())
        );
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresseLivraison"));
        colStatutLivraison.setCellValueFactory(new PropertyValueFactory<>("statut"));

        colActionLivraison.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Valider");

            {
                btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                btn.setOnAction(e -> {
                    BonLivraison bl = getTableView().getItems().get(getIndex());
                    validerBonLivraison(bl);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        loadLivraisons();
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
            AuthService authService = new AuthService();
            Utilisateur magasinierConnecte = authService.getCurrentUser();
            // 1. récupérer toutes les lignes du bon
            List<LigneCommande> lignes = ligneCommandeService.getLignesByBonCommande(bc.getIdBonCommande());
            if (magasinierConnecte == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Utilisateur magasinier introuvable !");
                alert.show();
                return;
            }
            for (LigneCommande ligne : lignes) {
                Produit p = produitService.getProduitById(ligne.getProduit().getReference());
                int stockAvant = p.getQuantiteStock();
                int stockApres = stockAvant + ligne.getQuantite();

                // 2. créer mouvement_stock
                MouvementStock mv = new MouvementStock();
                mv.setProduit(p);
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
    @FXML
    public void loadLivraisons() {
        try {
            // 1. récupérer tous les bons de livraison
            List<BonLivraison> livraisons = livraisonService.consulterTousLesBonsLivraison();

            // 2. filtrer uniquement les bons en attente
            List<BonLivraison> livraisonsNonValidees = livraisons.stream()
                    .filter(l -> l.getStatut().equals("EN_ATTENTE") || l.getStatut().equals("TRANSMISE"))
                    .toList();

            // 3. convertir en ObservableList pour JavaFX
            ObservableList<BonLivraison> data = FXCollections.observableArrayList(livraisonsNonValidees);

            // 4. mettre à jour la table
            tableLivraisons.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors du chargement des livraisons !");
            alert.show();
        }
    }
    private String generateNumeroSortie() throws Exception {
        // Exemple : BS + date du jour + un numéro aléatoire
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int random = (int) (Math.random() * 1000); // nombre entre 0 et 999
        return "BS-" + date + "-" + String.format("%03d", random);
    }


    private void validerBonLivraison(BonLivraison bl) {
        try {
            AuthService authService = new AuthService();
            Utilisateur magasinierConnecte = authService.getCurrentUser();
            BonSortie bs = new BonSortie();
            bs.setNumero(generateNumeroSortie()); // Méthode pour générer un numéro unique
            bs.setDateSortie(LocalDate.now());
            bs.setStatut("GENEREE");
            bonSortieService.creerBonSortie(bs);

            // 2 Créer les lignes de sortie
            List<LigneLivraison> lignes = ligneLivraisonService.getLignesByBonLivraison(bl.getIdBonLivraison());

            for (LigneLivraison ligne : lignes) {
                LigneSortie ls = new LigneSortie();
                ls.setBonSortie(bs);
                ls.setProduit(ligne.getProduit());
                ls.setQuantite(ligne.getQuantite());
                ligneSortieService.creerLigneSortie(ls);

                // 3 Enregistrer le mouvement de stock
                Produit p = ligne.getProduit();
                int stockAvant = p.getQuantiteStock();
                int stockApres = stockAvant - ligne.getQuantite();

                MouvementStock mv = new MouvementStock();
                mv.setTypeMouvement("SORTIE");
                mv.setProduit(p);
                mv.setQuantite(ligne.getQuantite());
                mv.setStockAvant(stockAvant);
                mv.setStockApres(stockApres);
                mv.setReference(p.getReference());
                mv.setUtilisateur(magasinierConnecte);
                mv.setDateMouvement(LocalDate.now());

                mouvementStockService.enregistrerSortie(mv);

                // 4️⃣ Mettre à jour le stock du produit
                p.setQuantiteStock(stockApres);
                produitService.mettreAJourStock(p);
            }

            // 5️⃣ Mettre à jour le statut du bon de livraison
            bl.setStatut("VALIDEE");
            livraisonService.mettreAJourStatut(bl);

            loadLivraisons();

            new Alert(Alert.AlertType.INFORMATION, "Bon de livraison validé avec succès !").show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur lors de la validation du bon !").show();
        }
    }





}
