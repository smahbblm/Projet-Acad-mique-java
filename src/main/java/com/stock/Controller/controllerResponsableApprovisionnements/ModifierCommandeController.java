package com.stock.Controller.controllerResponsableApprovisionnements;

import com.stock.model.document.BonCommande;
import com.stock.model.document.LigneCommande;
import com.stock.model.partenaire.Fournisseur;
import com.stock.model.produit.Produit;
import com.stock.service.CommandeService;
import com.stock.service.FournisseurService;
import com.stock.service.ProduitService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class ModifierCommandeController {

    @FXML
    private Button btnClose;

    @FXML
    private ComboBox<Fournisseur> cmbFournisseur;

    @FXML
    private DatePicker dateLivraison;

    @FXML
    private TextArea txtObservations;

    @FXML
    private ComboBox<Produit> cmbProduit;

    @FXML
    private TextField txtQuantite;

    @FXML
    private TextField txtPrixUnitaire;

    @FXML
    private Button btnAjouterLigne;

    @FXML
    private VBox lignesContainer;

    @FXML
    private Label lblTotal;

    @FXML
    private Button btnAnnuler;

    @FXML
    private Button btnModifier;

    private CommandeService commandeService;
    private FournisseurService fournisseurService;
    private ProduitService produitService;
    private BonCommande commande;
    private Runnable onCommandeUpdated;
    private List<LigneCommande> lignes = new ArrayList<>();
    private float montantTotal = 0;

    @FXML
    public void initialize() {
        try {
            commandeService = new CommandeService();
            fournisseurService = new FournisseurService();
            produitService = new ProduitService();
            loadFournisseurs();
            loadProduits();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFournisseurs() {
        try {
            var fournisseurs = fournisseurService.consulterTousFournisseurs();
            cmbFournisseur.getItems().addAll(fournisseurs);
            cmbFournisseur.setConverter(new javafx.util.StringConverter<Fournisseur>() {
                @Override
                public String toString(Fournisseur f) {
                    return f != null ? f.getRaisonSociale() : "";
                }
                @Override
                public Fournisseur fromString(String string) {
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProduits() {
        try {
            var produits = produitService.consulterTousProduits();
            cmbProduit.getItems().addAll(produits);
            cmbProduit.setConverter(new javafx.util.StringConverter<Produit>() {
                @Override
                public String toString(Produit p) {
                    return p != null ? p.getDesignation() + " (" + p.getReference() + ")" : "";
                }
                @Override
                public Produit fromString(String string) {
                    return null;
                }
            });
            cmbProduit.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    txtPrixUnitaire.setText(String.valueOf(newVal.getPrixAchat()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCommande(BonCommande commande) {
        this.commande = commande;
        cmbFournisseur.setValue(commande.getFournisseur());
        dateLivraison.setValue(commande.getDateLivraisonPrevue());
        txtObservations.setText(commande.getObservations());
        
        lignes = new ArrayList<>(commande.getLignes());
        montantTotal = commande.getMontantTotal();
        updateLignesDisplay();
    }

    @FXML
    public void ajouterLigne() {
        try {
            if (cmbProduit.getValue() == null || txtQuantite.getText().isEmpty() || txtPrixUnitaire.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs de la ligne");
                return;
            }

            int quantite = Integer.parseInt(txtQuantite.getText());
            float prixUnitaire = Float.parseFloat(txtPrixUnitaire.getText());
            float sousTotal = quantite * prixUnitaire;

            LigneCommande ligne = new LigneCommande(cmbProduit.getValue(), quantite, prixUnitaire);
            lignes.add(ligne);

            montantTotal += sousTotal;
            updateLignesDisplay();
            
            cmbProduit.setValue(null);
            txtQuantite.clear();
            txtPrixUnitaire.clear();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs numériques valides");
        }
    }

    private void updateLignesDisplay() {
        lignesContainer.getChildren().clear();
        if (lignes.isEmpty()) {
            lignesContainer.getChildren().add(new Label("Aucun produit ajouté"));
        } else {
            lignes.forEach(l -> {
                HBox row = new HBox(10);
                row.setStyle("-fx-padding:5; -fx-border-color:#e0e0e0; -fx-border-width:0 0 1 0;");
                Label lblProduit = new Label(l.getProduit().getDesignation());
                lblProduit.setPrefWidth(200);
                Label lblQte = new Label("x" + l.getQuantite());
                lblQte.setPrefWidth(50);
                Label lblPrix = new Label(String.format("%.2f €", l.getPrixUnitaire()));
                lblPrix.setPrefWidth(80);
                Label lblSousTotal = new Label(String.format("%.2f €", l.getSousTotal()));
                lblSousTotal.setPrefWidth(80);
                Button btnSuppr = new Button("❌");
                btnSuppr.setStyle("-fx-cursor:hand;");
                btnSuppr.setOnAction(e -> supprimerLigne(l));
                row.getChildren().addAll(lblProduit, lblQte, lblPrix, lblSousTotal, btnSuppr);
                lignesContainer.getChildren().add(row);
            });
        }
        lblTotal.setText(String.format("Total: %.2f €", montantTotal));
    }

    private void supprimerLigne(LigneCommande ligne) {
        montantTotal -= ligne.getSousTotal();
        lignes.remove(ligne);
        updateLignesDisplay();
    }

    public void setOnCommandeUpdated(Runnable callback) {
        this.onCommandeUpdated = callback;
    }



    @FXML
    public void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void modifierCommande() {
        try {
            System.out.println("Modification commande...");
            if (cmbFournisseur.getValue() == null) {
                showAlert("Erreur", "Veuillez sélectionner un fournisseur");
                return;
            }
            if (lignes.isEmpty()) {
                showAlert("Erreur", "Veuillez ajouter au moins un produit");
                return;
            }

            commande.setFournisseur(cmbFournisseur.getValue());
            commande.setDateLivraisonPrevue(dateLivraison.getValue());
            commande.setObservations(txtObservations.getText());
            commande.setMontantTotal(montantTotal);
            commande.setLignes(lignes);

            commandeService.modifierBonCommande(commande);
            
            if (onCommandeUpdated != null) {
                onCommandeUpdated.run();
            }
            showAlert("Succès", "Bon de commande modifié avec succès");
            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la modification: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Succès") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
