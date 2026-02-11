package com.stock.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.stock.model.document.BonCommande;
import com.stock.dao.implementation.BonCommandeDAO;
import com.stock.util.AlertUtils;
import com.stock.util.NavigationManager;

import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur pour la gestion des bons de commande (Approvisionnement)
 */
public class GestionCommandesController {

    @FXML private TextField txtNumero;
    @FXML private ComboBox<String> comboStatut;
    @FXML private ComboBox<String> comboFournisseur;
    @FXML private Button btnRecherche;

    @FXML private TableView<BonCommande> tableCommandes;
    @FXML private TableColumn<BonCommande, String> colNumero;
    @FXML private TableColumn<BonCommande, LocalDate> colDate;
    @FXML private TableColumn<BonCommande, String> colFournisseur;
    @FXML private TableColumn<BonCommande, Float> colMontant;
    @FXML private TableColumn<BonCommande, String> colStatut;
    @FXML private TableColumn<BonCommande, String> colUtilisateur;

    @FXML private ComboBox<String> comboFournisseurNew;
    @FXML private DatePicker dateLivraison;
    @FXML private TextArea txtNotes;

    @FXML private Button btnCreer;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;
    @FXML private Button btnRetour;

    private BonCommandeDAO bonCommandeDAO;
    private ObservableList<BonCommande> commandes;

    @FXML
    public void initialize() {
        try {
            bonCommandeDAO = new BonCommandeDAO();
            commandes = FXCollections.observableArrayList();

            initTable();
            chargerStatuts();
            chargerFournisseurs();
            chargerCommandes();

        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Erreur lors de l'initialisation: " + e.getMessage());
        }
    }

    private void initTable() {
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));
        colFournisseur.setCellValueFactory(new PropertyValueFactory<>("fournisseur"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colUtilisateur.setCellValueFactory(new PropertyValueFactory<>("utilisateur"));
    }

    private void chargerStatuts() {
        comboStatut.setItems(FXCollections.observableArrayList(
            "BROUILLON", "EN_ATTENTE", "VALIDEE", "TRANSMISE", "RECU", "ANNULEE"
        ));
    }

    private void chargerFournisseurs() {
        try {
            // Charger depuis DAO Fournisseur
            comboFournisseur.setItems(FXCollections.observableArrayList("Fournisseur 1", "Fournisseur 2"));
            comboFournisseurNew.setItems(FXCollections.observableArrayList("Fournisseur 1", "Fournisseur 2"));
        } catch (Exception e) {
            System.err.println("Erreur chargement fournisseurs: " + e.getMessage());
        }
    }

    private void chargerCommandes() {
        try {
            List<BonCommande> listeCommandes = bonCommandeDAO.readAll();
            if (listeCommandes != null && !listeCommandes.isEmpty()) {
                commandes.setAll(listeCommandes);
                tableCommandes.setItems(commandes);
            } else {
                // Charger depuis DB directement si DAO vide
                chargerCommandesDepuisBD();
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement commandes: " + e.getMessage());
            chargerCommandesDepuisBD();
        }
    }

    private void chargerCommandesDepuisBD() {
        try {
            commandes.clear();
            var conn = com.stock.util.DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT * FROM bon_commande ORDER BY dateCommande DESC";
            try (var stmt = conn.prepareStatement(sql);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BonCommande bc = new BonCommande(
                        rs.getString("numero"),
                        rs.getDate("dateCommande").toLocalDate(),
                        null // Fournisseur
                    );
                    bc.setStatut(rs.getString("statut"));
                    bc.setMontantTotal(rs.getFloat("montantTotal"));
                    commandes.add(bc);
                }
            }
            tableCommandes.setItems(commandes);
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible de charger les commandes: " + e.getMessage());
        }
    }

    @FXML
    private void handleRecherche() {
        String numero = txtNumero.getText().toLowerCase();
        String statut = comboStatut.getValue();
        String fournisseur = comboFournisseur.getValue();

        ObservableList<BonCommande> filtered = FXCollections.observableArrayList();

        for (BonCommande bc : commandes) {
            boolean matchNumero = numero.isEmpty() || bc.getNumero().toLowerCase().contains(numero);
            boolean matchStatut = statut == null || statut.isEmpty() || statut.equals(bc.getStatut());
            boolean matchFournisseur = fournisseur == null || fournisseur.isEmpty() ||
                (bc.getFournisseur() != null && bc.getFournisseur().getRaisonSociale().contains(fournisseur));

            if (matchNumero && matchStatut && matchFournisseur) {
                filtered.add(bc);
            }
        }

        tableCommandes.setItems(filtered);
    }

    @FXML
    private void handleCreer() {
        // Implémentation similaire à GestionUtilisateurs
        AlertUtils.showInfo("Info", "Fonctionnalité de création à implémenter");
    }

    @FXML
    private void handleModifier() {
        BonCommande selected = tableCommandes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Attention", "Veuillez sélectionner une commande à modifier");
            return;
        }
        AlertUtils.showInfo("Info", "Fonctionnalité de modification à implémenter");
    }

    @FXML
    private void handleSupprimer() {
        BonCommande selected = tableCommandes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Attention", "Veuillez sélectionner une commande à supprimer");
            return;
        }
        AlertUtils.showInfo("Info", "Fonctionnalité de suppression à implémenter");
    }

    @FXML
    private void handleRetour() {
        try {
            NavigationManager.getInstance().navigate("/fxml/appro/DashboardAppro.fxml", "Dashboard Approvisionnement");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible de retourner au dashboard: " + e.getMessage());
        }
    }

    @FXML private void handleGenererRapportAchats() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Rapport Achats");
        dialog.setHeaderText("Générer un rapport des achats");
        var grid = new javafx.scene.layout.GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new javafx.geometry.Insets(15));
        DatePicker dpDe = new DatePicker(LocalDate.now().minusDays(30));
        DatePicker dpA = new DatePicker(LocalDate.now());
        grid.add(new Label("Du:"),0,0); grid.add(dpDe,1,0);
        grid.add(new Label("Au:"),0,1); grid.add(dpA,1,1);
        dialog.getDialogPane().setContent(grid);
        var okBtn = new ButtonType("Générer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);
        dialog.setResultConverter(b -> {
            if (b == okBtn) {
                try {
                    var dao = new com.stock.dao.implementation.RapportDAO();
                    var rapport = dao.genererRapport("ACHATS", dpDe.getValue(), dpA.getValue());
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Rapport Achats");
                    a.setHeaderText("Rapport ID: " + rapport.getIdRapport());
                    TextArea ta = new TextArea(rapport.getContenu()); ta.setEditable(false); ta.setWrapText(true);
                    a.getDialogPane().setExpandableContent(ta);
                    a.showAndWait();
                } catch (Exception e) { AlertUtils.showError("Erreur", e.getMessage()); }
            }
            return b;
        });
        dialog.showAndWait();
    }

    @FXML private void handleValiderCommande() {
        BonCommande bc = tableCommandes.getSelectionModel().getSelectedItem();
        if (bc == null) { AlertUtils.showWarning("Attention","Sélectionnez une commande"); return; }
        if (!bc.getStatut().equals("EN_ATTENTE") && !bc.getStatut().equals("BROUILLON")) { AlertUtils.showWarning("Attention","Statut doit être BROUILLON ou EN_ATTENTE"); return; }
        changerStatut(bc, "VALIDEE", "Validation");
    }

    @FXML private void handleTransmettreCommande() {
        BonCommande bc = tableCommandes.getSelectionModel().getSelectedItem();
        if (bc == null) { AlertUtils.showWarning("Attention","Sélectionnez une commande"); return; }
        if (!bc.getStatut().equals("VALIDEE")) { AlertUtils.showWarning("Attention","Statut doit être VALIDEE"); return; }
        changerStatut(bc, "TRANSMISE", "Transmission");
    }

    @FXML private void handleRecevoirCommande() {
        BonCommande bc = tableCommandes.getSelectionModel().getSelectedItem();
        if (bc == null) { AlertUtils.showWarning("Attention","Sélectionnez une commande"); return; }
        if (!bc.getStatut().equals("TRANSMISE")) { AlertUtils.showWarning("Attention","Statut doit être TRANSMISE"); return; }
        // Passage à RECU + incrément stock par lignes
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Confirmer réception ?", ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText("Réception commande " + bc.getNumero());
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    var conn = com.stock.util.DatabaseConnection.getInstance().getConnection();
                    try (var ps = conn.prepareStatement("UPDATE bon_commande SET statut='RECU' WHERE numero=?")) { ps.setString(1, bc.getNumero()); ps.executeUpdate(); }
                    // Charger lignes et mettre à jour stock
                    try (var ps = conn.prepareStatement("SELECT idProduit, quantite, prixUnitaire FROM ligne_commande WHERE idBonCommande=?")) {
                        ps.setInt(1, bc.getIdBonCommande());
                        try (var rs = ps.executeQuery()) {
                            while (rs.next()) {
                                int idProd = rs.getInt("idProduit");
                                int qte = rs.getInt("quantite");
                                int stockAvant = 0;
                                try (var ps2 = conn.prepareStatement("SELECT quantiteStock FROM produits WHERE idProduit=?")) { ps2.setInt(1, idProd); try (var rs2 = ps2.executeQuery()) { if (rs2.next()) stockAvant = rs2.getInt(1); } }
                                int stockApres = stockAvant + qte;
                                try (var up = conn.prepareStatement("UPDATE produits SET quantiteStock=? WHERE idProduit=?")) { up.setInt(1, stockApres); up.setInt(2, idProd); up.executeUpdate(); }
                                try (var ms = conn.prepareStatement("INSERT INTO mouvement_stock (dateMouvement, typeMouvement, quantite, stockAvant, stockApres, reference, idProduit) VALUES (CURDATE(),'ENTREE',?,?,?,?,?)")) {
                                    // On suppose reference récupérable
                                    String ref = ""; try (var rref = conn.prepareStatement("SELECT reference FROM produits WHERE idProduit=?")) { rref.setInt(1, idProd); try (var rrs = rref.executeQuery()) { if (rrs.next()) ref = rrs.getString(1); } }
                                    ms.setInt(1, qte); ms.setInt(2, stockAvant); ms.setInt(3, stockApres); ms.setString(4, ref); ms.setInt(5, idProd); ms.executeUpdate();
                                }
                            }
                        }
                    }
                    chargerCommandes();
                    AlertUtils.showInfo("Succès","Commande réceptionnée et stock mis à jour");
                } catch (Exception e) { AlertUtils.showError("Erreur","Réception: " + e.getMessage()); }
            }
        });
    }

    private void changerStatut(BonCommande bc, String nouveau, String label) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Confirmer " + label + " ?", ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText(label + " commande " + bc.getNumero());
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try (var ps = com.stock.util.DatabaseConnection.getInstance().getConnection().prepareStatement("UPDATE bon_commande SET statut=? WHERE numero=?")) {
                    ps.setString(1, nouveau); ps.setString(2, bc.getNumero()); ps.executeUpdate();
                    chargerCommandes();
                    AlertUtils.showInfo("Succès","Statut mis à jour");
                } catch (Exception e) { AlertUtils.showError("Erreur", e.getMessage()); }
            }
        });
    }
}
