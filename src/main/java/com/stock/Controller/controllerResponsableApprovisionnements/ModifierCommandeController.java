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
    private Button btnAnnuler;

    @FXML
    private Button btnModifier;

    private CommandeService commandeService;
    private FournisseurService fournisseurService;
    private BonCommande commande;
    private Runnable onCommandeUpdated;

    @FXML
    public void initialize() {
        try {
            commandeService = new CommandeService();
            fournisseurService = new FournisseurService();
            loadFournisseurs();
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



    public void setCommande(BonCommande commande) {
        this.commande = commande;
        cmbFournisseur.setValue(commande.getFournisseur());
        dateLivraison.setValue(commande.getDateLivraisonPrevue());
        txtObservations.setText(commande.getObservations());
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
            if (cmbFournisseur.getValue() == null) {
                showAlert("Erreur", "Veuillez sélectionner un fournisseur");
                return;
            }

            commande.setFournisseur(cmbFournisseur.getValue());
            commande.setDateLivraisonPrevue(dateLivraison.getValue());
            commande.setObservations(txtObservations.getText());

            commandeService.modifierBonCommande(commande);
            
            if (onCommandeUpdated != null) {
                onCommandeUpdated.run();
            }
            showAlert("Succès", "Bon de commande modifié avec succès");
            closeWindow();
        } catch (Exception e) {
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
