package com.stock.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import com.stock.model.produit.Produit;
import com.stock.util.AlertUtils;
import com.stock.util.DatabaseConnection;
import com.stock.util.NavigationManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Contrôleur pour l'inventaire détaillé (saisie stock réel, calcul écarts, validation)
 */
public class InventaireDetailController {

    @FXML private Label lblDateInventaire;
    @FXML private TextField txtRecherche;
    @FXML private ComboBox<String> comboCategorie;
    @FXML private TableView<LigneInventaireVM> tableProduits;
    @FXML private TableColumn<LigneInventaireVM, String> colReference;
    @FXML private TableColumn<LigneInventaireVM, String> colDesignation;
    @FXML private TableColumn<LigneInventaireVM, String> colCategorie;
    @FXML private TableColumn<LigneInventaireVM, Integer> colStockTheorique;
    @FXML private TableColumn<LigneInventaireVM, Integer> colStockReel;
    @FXML private TableColumn<LigneInventaireVM, Integer> colEcart;
    @FXML private TableColumn<LigneInventaireVM, String> colEtat;
    @FXML private Label lblTotalProduits;
    @FXML private Label lblAvecEcart;
    @FXML private Label lblSansEcart;
    @FXML private TextArea txtObservations;

    private ObservableList<LigneInventaireVM> lignes;
    private int idInventaire = 0; // 0 = nouveau, sinon déjà créé

    @FXML
    public void initialize() {
        lignes = FXCollections.observableArrayList();
        initTable();
        chargerCategories();
        chargerProduits();
        lblDateInventaire.setText("Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private void initTable() {
        colReference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        colDesignation.setCellValueFactory(new PropertyValueFactory<>("designation"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colStockTheorique.setCellValueFactory(new PropertyValueFactory<>("stockTheorique"));
        colStockReel.setCellValueFactory(new PropertyValueFactory<>("stockReel"));
        colEcart.setCellValueFactory(new PropertyValueFactory<>("ecart"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Rendre colonne stock réel éditable
        colStockReel.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colStockReel.setOnEditCommit(event -> {
            LigneInventaireVM ligne = event.getRowValue();
            ligne.setStockReel(event.getNewValue());
            ligne.calculerEcart();
            calculerResume();
        });

        // Colorer l'état
        colEtat.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                switch (item) {
                    case "OK" -> setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    case "ÉCART" -> setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;");
                    case "NON SAISI" -> setStyle("-fx-text-fill: #95a5a6;");
                }
            }
        });

        // Colorer écart
        colEcart.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item.toString());
                if (item > 0) setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                else if (item < 0) setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                else setStyle("-fx-text-fill: #95a5a6;");
            }
        });
    }

    private void chargerCategories() {
        try {
            var conn = DatabaseConnection.getInstance().getConnection();
            var categories = FXCollections.observableArrayList("");
            try (var ps = conn.prepareStatement("SELECT DISTINCT categorie FROM produits WHERE categorie IS NOT NULL ORDER BY categorie");
                 var rs = ps.executeQuery()) {
                while (rs.next()) categories.add(rs.getString(1));
            }
            comboCategorie.setItems(categories);
        } catch (Exception e) { AlertUtils.showError("Erreur", "Chargement catégories: " + e.getMessage()); }
    }

    private void chargerProduits() {
        lignes.clear();
        try {
            var conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT idProduit, reference, designation, categorie, quantiteStock FROM produits ORDER BY reference";
            try (var ps = conn.prepareStatement(sql); var rs = ps.executeQuery()) {
                while (rs.next()) {
                    LigneInventaireVM ligne = new LigneInventaireVM(
                        rs.getInt("idProduit"),
                        rs.getString("reference"),
                        rs.getString("designation"),
                        rs.getString("categorie"),
                        rs.getInt("quantiteStock")
                    );
                    lignes.add(ligne);
                }
            }
            tableProduits.setItems(lignes);
            calculerResume();
        } catch (Exception e) { AlertUtils.showError("Erreur", "Chargement produits: " + e.getMessage()); }
    }

    private void calculerResume() {
        int total = lignes.size();
        int avecEcart = (int) lignes.stream().filter(l -> l.getStockReel() != null && l.getEcart() != 0).count();
        int sansEcart = (int) lignes.stream().filter(l -> l.getStockReel() != null && l.getEcart() == 0).count();
        lblTotalProduits.setText(String.valueOf(total));
        lblAvecEcart.setText(String.valueOf(avecEcart));
        lblSansEcart.setText(String.valueOf(sansEcart));
    }

    @FXML private void handleFiltrer() {
        String recherche = txtRecherche.getText().toLowerCase();
        String cat = comboCategorie.getValue();
        ObservableList<LigneInventaireVM> filtered = FXCollections.observableArrayList();
        for (LigneInventaireVM l : lignes) {
            boolean matchRecherche = recherche.isEmpty() ||
                (l.getReference() != null && l.getReference().toLowerCase().contains(recherche)) ||
                (l.getDesignation() != null && l.getDesignation().toLowerCase().contains(recherche));
            boolean matchCat = cat == null || cat.isEmpty() || cat.equals(l.getCategorie());
            if (matchRecherche && matchCat) filtered.add(l);
        }
        tableProduits.setItems(filtered);
    }

    @FXML private void handleReinitialiser() { txtRecherche.clear(); comboCategorie.setValue(""); tableProduits.setItems(lignes); }

    @FXML private void handleValider() {
        // Vérifier que toutes les lignes ont un stock réel saisi
        long nonSaisi = lignes.stream().filter(l -> l.getStockReel() == null).count();
        if (nonSaisi > 0) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, nonSaisi + " produit(s) sans stock réel saisi. Continuer ?", ButtonType.OK, ButtonType.CANCEL);
            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.CANCEL) return;
        }

        try {
            var conn = DatabaseConnection.getInstance().getConnection();
            // Créer inventaire si nouveau
            if (idInventaire == 0) {
                try (var ps = conn.prepareStatement("INSERT INTO inventaires (dateInventaire, statut, observations) VALUES (CURDATE(), 'VALIDEE', ?)", java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, txtObservations.getText());
                    ps.executeUpdate();
                    try (var rs = ps.getGeneratedKeys()) { if (rs.next()) idInventaire = rs.getInt(1); }
                }
            }

            // Insérer lignes inventaire
            try (var ps = conn.prepareStatement("INSERT INTO ligne_inventaire (idInventaire, idProduit, quantiteTheorique, quantiteReelle, ecart) VALUES (?, ?, ?, ?, ?)")) {
                for (LigneInventaireVM ligne : lignes) {
                    if (ligne.getStockReel() != null) {
                        ps.setInt(1, idInventaire);
                        ps.setInt(2, ligne.getIdProduit());
                        ps.setInt(3, ligne.getStockTheorique());
                        ps.setInt(4, ligne.getStockReel());
                        ps.setInt(5, ligne.getEcart());
                        ps.addBatch();
                    }
                }
                ps.executeBatch();
            }

            // Créer mouvements AJUSTEMENT pour les écarts
            try (var psMvt = conn.prepareStatement("INSERT INTO mouvement_stock (dateMouvement, typeMouvement, quantite, stockAvant, stockApres, reference, idProduit) VALUES (CURDATE(), 'AJUSTEMENT', ?, ?, ?, ?, ?)");
                 var psUpdate = conn.prepareStatement("UPDATE produits SET quantiteStock=? WHERE idProduit=?")) {
                for (LigneInventaireVM ligne : lignes) {
                    if (ligne.getStockReel() != null && ligne.getEcart() != 0) {
                        int nouvelleQte = ligne.getStockReel();
                        psMvt.setInt(1, Math.abs(ligne.getEcart()));
                        psMvt.setInt(2, ligne.getStockTheorique());
                        psMvt.setInt(3, nouvelleQte);
                        psMvt.setString(4, ligne.getReference());
                        psMvt.setInt(5, ligne.getIdProduit());
                        psMvt.addBatch();
                        psUpdate.setInt(1, nouvelleQte);
                        psUpdate.setInt(2, ligne.getIdProduit());
                        psUpdate.addBatch();
                    }
                }
                psMvt.executeBatch();
                psUpdate.executeBatch();
            }

            AlertUtils.showInfo("Succès", "Inventaire validé et stock mis à jour");
            handleRetour();
        } catch (Exception e) { AlertUtils.showError("Erreur", "Validation: " + e.getMessage()); }
    }

    @FXML private void handleEnregistrerBrouillon() {
        try {
            var conn = DatabaseConnection.getInstance().getConnection();
            if (idInventaire == 0) {
                try (var ps = conn.prepareStatement("INSERT INTO inventaires (dateInventaire, statut, observations) VALUES (CURDATE(), 'BROUILLON', ?)", java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, txtObservations.getText());
                    ps.executeUpdate();
                    try (var rs = ps.getGeneratedKeys()) { if (rs.next()) idInventaire = rs.getInt(1); }
                }
            }
            AlertUtils.showInfo("Succès", "Brouillon enregistré (ID: " + idInventaire + ")");
        } catch (Exception e) { AlertUtils.showError("Erreur", "Enregistrement: " + e.getMessage()); }
    }

    @FXML private void handleGenererRapport() {
        try {
            var dao = new com.stock.dao.implementation.RapportDAO();
            var rapport = dao.genererRapport("INVENTAIRE", LocalDate.now().minusDays(30), LocalDate.now());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Rapport Inventaire");
            alert.setHeaderText("Rapport ID: " + rapport.getIdRapport());
            TextArea ta = new TextArea(rapport.getContenu()); ta.setEditable(false); ta.setWrapText(true); ta.setPrefHeight(400);
            alert.getDialogPane().setExpandableContent(ta);
            alert.showAndWait();
        } catch (Exception e) { AlertUtils.showError("Erreur", "Génération: " + e.getMessage()); }
    }

    @FXML private void handleRetour() {
        try { NavigationManager.getInstance().navigate("/fxml/magasinier/DashboardMagasinier.fxml", "Dashboard Magasinier"); }
        catch (Exception e) { AlertUtils.showError("Erreur", "Navigation: " + e.getMessage()); }
    }

    /**
     * ViewModel pour une ligne d'inventaire (binding JavaFX)
     */
    public static class LigneInventaireVM {
        private final int idProduit;
        private final SimpleStringProperty reference;
        private final SimpleStringProperty designation;
        private final SimpleStringProperty categorie;
        private final SimpleIntegerProperty stockTheorique;
        private Integer stockReel; // nullable pour édition
        private final SimpleIntegerProperty ecart;
        private final SimpleStringProperty etat;

        public LigneInventaireVM(int idProduit, String reference, String designation, String categorie, int stockTheorique) {
            this.idProduit = idProduit;
            this.reference = new SimpleStringProperty(reference);
            this.designation = new SimpleStringProperty(designation);
            this.categorie = new SimpleStringProperty(categorie);
            this.stockTheorique = new SimpleIntegerProperty(stockTheorique);
            this.stockReel = null; // pas encore saisi
            this.ecart = new SimpleIntegerProperty(0);
            this.etat = new SimpleStringProperty("NON SAISI");
        }

        public void calculerEcart() {
            if (stockReel == null) {
                ecart.set(0);
                etat.set("NON SAISI");
            } else {
                int e = stockReel - stockTheorique.get();
                ecart.set(e);
                etat.set(e == 0 ? "OK" : "ÉCART");
            }
        }

        public int getIdProduit() { return idProduit; }
        public String getReference() { return reference.get(); }
        public String getDesignation() { return designation.get(); }
        public String getCategorie() { return categorie.get(); }
        public int getStockTheorique() { return stockTheorique.get(); }
        public Integer getStockReel() { return stockReel; }
        public void setStockReel(Integer val) { this.stockReel = val; calculerEcart(); }
        public int getEcart() { return ecart.get(); }
        public String getEtat() { return etat.get(); }
    }
}

