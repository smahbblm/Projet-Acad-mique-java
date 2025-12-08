package com.stock.controller;

import com.stock.util.AlertUtils;
import com.stock.util.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.stock.model.document.MouvementStock;

import java.util.Optional;

/**
 * Contrôleur pour le dashboard magasinier
 */
public class MagasinierController {
    @FXML private Label lblUser, lblStockTotal, lblProduitsAlerte, lblMouvementsJour, lblInventairesCours, lblHorloge;
    @FXML private TableView<com.stock.model.document.BonLivraison> tableReceptions;
    @FXML private TableColumn<com.stock.model.document.BonLivraison,String> colRecNumero;
    @FXML private TableColumn<com.stock.model.document.BonLivraison,java.time.LocalDate> colRecDate;
    @FXML private TableColumn<com.stock.model.document.BonLivraison,String> colRecClient;
    @FXML private TableColumn<com.stock.model.document.BonLivraison,String> colRecStatut;

    @FXML private TableView<com.stock.model.document.BonSortie> tableSorties;
    @FXML private TableColumn<com.stock.model.document.BonSortie,String> colSortNumero;
    @FXML private TableColumn<com.stock.model.document.BonSortie,java.time.LocalDate> colSortDate;
    @FXML private TableColumn<com.stock.model.document.BonSortie,String> colSortStatut;

    @FXML private TableView<MouvementStock> tableMouvements;
    @FXML private TableColumn<MouvementStock,java.time.LocalDate> colMouvDate;
    @FXML private TableColumn<MouvementStock,String> colMouvType;
    @FXML private TableColumn<MouvementStock,String> colMouvRef;
    @FXML private TableColumn<MouvementStock,Integer> colMouvQuantite;
    @FXML private TableColumn<MouvementStock,Integer> colMouvAvant;
    @FXML private TableColumn<MouvementStock,Integer> colMouvApres;

    private final java.time.format.DateTimeFormatter dateFmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        initTables();
        chargerStatistiques();
        chargerReceptions();
        chargerSorties();
        chargerMouvements();
        chargerInventairesEnCours();
        demarrerHorloge();
    }

    private void initTables() {
        // Réceptions (bons de livraison en attente ou transmises)
        colRecNumero.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("numero"));
        colRecDate.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateLivraison"));
        colRecClient.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("clientNom"));
        colRecStatut.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("statut"));
        colRecDate.setCellFactory(c -> new javafx.scene.control.TableCell<>(){ @Override protected void updateItem(java.time.LocalDate item, boolean empty){ super.updateItem(item,empty); setText(empty||item==null?null:item.format(dateFmt)); }});
        colRecStatut.setCellFactory(c -> new javafx.scene.control.TableCell<>(){ @Override protected void updateItem(String item, boolean empty){ super.updateItem(item,empty); if(empty||item==null){setText(null);setStyle("");return;} setText(item); switch(item){case"EN_ATTENTE"->setStyle("-fx-text-fill:#f39c12;");case"TRANSMISE"->setStyle("-fx-text-fill:#8e44ad;");case"VALIDEE"->setStyle("-fx-text-fill:#2980b9;");case"LIVREE"->setStyle("-fx-text-fill:#27ae60;");default->setStyle("-fx-text-fill:#7f8c8d;");}}});

        // Sorties (bons de sortie)
        colSortNumero.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("numero"));
        colSortDate.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateSortie"));
        colSortStatut.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("statut"));
        colSortDate.setCellFactory(c -> new javafx.scene.control.TableCell<>(){ @Override protected void updateItem(java.time.LocalDate item, boolean empty){ super.updateItem(item,empty); setText(empty||item==null?null:item.format(dateFmt)); }});
        colSortStatut.setCellFactory(c -> new javafx.scene.control.TableCell<>(){ @Override protected void updateItem(String item, boolean empty){ super.updateItem(item,empty); if(empty||item==null){setText(null);return;} setText(item); switch(item){case"GENEREE"->setStyle("-fx-text-fill:#8e44ad;");case"VALIDEE"->setStyle("-fx-text-fill:#2980b9;");case"ENREGISTREE"->setStyle("-fx-text-fill:#27ae60;");case"ANNULEE"->setStyle("-fx-text-fill:#e74c3c;");default->setStyle("-fx-text-fill:#7f8c8d;");}}});

        // Mouvements
        colMouvDate.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateMouvement"));
        colMouvType.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("typeMouvement"));
        colMouvRef.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("reference"));
        colMouvQuantite.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("quantite"));
        colMouvAvant.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("stockAvant"));
        colMouvApres.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("stockApres"));
        colMouvDate.setCellFactory(c -> new javafx.scene.control.TableCell<>(){ @Override protected void updateItem(java.time.LocalDate item, boolean empty){ super.updateItem(item,empty); setText(empty||item==null?null:item.format(dateFmt)); }});
        colMouvType.setCellFactory(c -> new javafx.scene.control.TableCell<>(){ @Override protected void updateItem(String item, boolean empty){ super.updateItem(item,empty); if(empty||item==null){setText(null);return;} setText(item); switch(item){case"ENTREE"->setStyle("-fx-text-fill:#27ae60;");case"SORTIE"->setStyle("-fx-text-fill:#e67e22;");case"AJUSTEMENT"->setStyle("-fx-text-fill:#9b59b6;");case"INVENTAIRE"->setStyle("-fx-text-fill:#34495e;");}}});
    }

    private void chargerStatistiques() {
        try {
            var conn = com.stock.util.DatabaseConnection.getInstance().getConnection();
            // Stock total
            try (var s = conn.prepareStatement("SELECT COALESCE(SUM(quantiteStock),0) AS total FROM produits"); var r = s.executeQuery()) { if(r.next()) lblStockTotal.setText(String.valueOf(r.getInt("total"))); }
            // Produits en alerte (quantité < seuilMin)
            try (var s = conn.prepareStatement("SELECT COUNT(*) AS c FROM produits WHERE quantiteStock < seuilMin"); var r = s.executeQuery()) { if(r.next()) lblProduitsAlerte.setText(String.valueOf(r.getInt("c"))); }
            // Mouvements aujourd'hui
            try (var s = conn.prepareStatement("SELECT COUNT(*) AS c FROM mouvement_stock WHERE dateMouvement = CURDATE()"); var r = s.executeQuery()) { if(r.next()) lblMouvementsJour.setText(String.valueOf(r.getInt("c"))); }
            // Inventaires en cours
            try (var s = conn.prepareStatement("SELECT COUNT(*) AS c FROM inventaires WHERE statut='EN_COURS'"); var r = s.executeQuery()) { if(r.next()) lblInventairesCours.setText(String.valueOf(r.getInt("c"))); }
        } catch (Exception e) { com.stock.util.AlertUtils.showError("Erreur","Statistiques: "+e.getMessage()); }
    }

    private void chargerReceptions() {
        var data = FXCollections.<com.stock.model.document.BonLivraison>observableArrayList();
        try {
            var conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT bl.idBonLivraison, bl.numero, bl.dateLivraison, bl.statut, c.nom, c.prenom " +
                         "FROM bon_livraison bl JOIN clients c ON bl.idClient=c.idClient " +
                         "WHERE bl.statut IN ('EN_ATTENTE','TRANSMISE') ORDER BY bl.dateLivraison DESC LIMIT 12";
            try (var stmt = conn.prepareStatement(sql); var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    var client = new com.stock.model.partenaire.Client(rs.getString("nom"), rs.getString("prenom"), null, null, null, null); client.setIdClient(0);
                    var bl = new com.stock.model.document.BonLivraison();
                    bl.setIdBonLivraison(rs.getInt("idBonLivraison"));
                    bl.setNumero(rs.getString("numero"));
                    if (rs.getDate("dateLivraison") != null) bl.setDateLivraison(rs.getDate("dateLivraison").toLocalDate());
                    bl.setStatut(rs.getString("statut"));
                    bl.setClient(client);
                    data.add(bl);
                }
            }
        } catch (Exception e) { AlertUtils.showError("Erreur","Réceptions: "+e.getMessage()); }
        tableReceptions.setItems(data);
    }

    private void chargerSorties() {
        var data = FXCollections.<com.stock.model.document.BonSortie>observableArrayList();
        try {
            var conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT idBonSortie, numero, dateSortie, statut FROM bon_sortie ORDER BY dateSortie DESC LIMIT 12";
            try (var stmt = conn.prepareStatement(sql); var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    var bs = new com.stock.model.document.BonSortie(rs.getString("numero"));
                    bs.setIdBonSortie(rs.getInt("idBonSortie"));
                    if (rs.getDate("dateSortie") != null) bs.setDateSortie(rs.getDate("dateSortie").toLocalDate());
                    bs.setStatut(rs.getString("statut"));
                    data.add(bs);
                }
            }
        } catch (Exception e) { AlertUtils.showError("Erreur","Sorties: "+e.getMessage()); }
        tableSorties.setItems(data);
    }

    private void chargerMouvements() {
        var data = FXCollections.<MouvementStock>observableArrayList();
        try {
            var conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT idMouvement, dateMouvement, typeMouvement, quantite, stockAvant, stockApres, reference FROM mouvement_stock ORDER BY dateMouvement DESC, idMouvement DESC LIMIT 25";
            try (var stmt = conn.prepareStatement(sql); var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    var m = new MouvementStock();
                    if (rs.getDate("dateMouvement") != null) m.setDateMouvement(rs.getDate("dateMouvement").toLocalDate());
                    m.setTypeMouvement(rs.getString("typeMouvement"));
                    m.setQuantite(rs.getInt("quantite"));
                    m.setStockAvant(rs.getInt("stockAvant"));
                    m.setStockApres(rs.getInt("stockApres"));
                    m.setReference(rs.getString("reference"));
                    data.add(m);
                }
            }
        } catch (Exception e) { AlertUtils.showError("Erreur","Mouvements: "+e.getMessage()); }
        tableMouvements.setItems(data);
    }

    private void chargerInventairesEnCours() {
        // Déjà compté dans statistiques; placeholder pour future liste détaillée si besoin
    }

    private void demarrerHorloge() {
        var timeline = new javafx.animation.Timeline(new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), ev -> {
            lblHorloge.setText(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))); }));
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();
    }

    // Actions métier
    @FXML private void handleValiderReception() { transitionBonLivraison("EN_ATTENTE", "VALIDEE", "Validation réception"); }
    @FXML private void handleValiderBonLivraison() { transitionBonLivraison("TRANSMISE", "LIVREE", "Validation livraison"); }
    @FXML private void handleGenererBonSortie() { genererBonSortie(); }
    @FXML private void handleEntreeStock() { enregistrerMouvement(true); }
    @FXML private void handleSortieStock() { enregistrerMouvement(false); }
    @FXML private void handleRealiserInventaire() {
        try {
            com.stock.util.NavigationManager.getInstance().navigate("/fxml/magasinier/InventaireDetail.fxml", "Inventaire Détaillé");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Navigation inventaire: " + e.getMessage());
        }
    }

    @FXML private void handleGenererBonSortieDepuisBL() {
        var bl = tableReceptions.getSelectionModel().getSelectedItem();
        if (bl == null) { AlertUtils.showWarning("Attention","Sélectionnez un bon de livraison"); return; }
        if (!bl.getStatut().equals("TRANSMISE") && !bl.getStatut().equals("VALIDEE")) {
            AlertUtils.showWarning("Attention","Le bon doit être TRANSMISE ou VALIDEE"); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Générer un bon de sortie pour le BL " + bl.getNumero() + " ?", ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText("Génération Bon de Sortie");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    var conn = DatabaseConnection.getInstance().getConnection();
                    String numeroBs = "BS-" + java.util.UUID.randomUUID().toString().substring(0,8).toUpperCase();
                    int idBonSortie = 0;

                    // Créer bon de sortie
                    try (var ps = conn.prepareStatement("INSERT INTO bon_sortie (numero, dateSortie, statut) VALUES (?, CURDATE(), 'GENEREE')", java.sql.Statement.RETURN_GENERATED_KEYS)) {
                        ps.setString(1, numeroBs);
                        ps.executeUpdate();
                        try (var rs = ps.getGeneratedKeys()) { if (rs.next()) idBonSortie = rs.getInt(1); }
                    }

                    if (idBonSortie == 0) { AlertUtils.showError("Erreur","Bon de sortie non créé"); return; }

                    // Copier lignes livraison vers lignes sortie
                    try (var psSelect = conn.prepareStatement("SELECT idProduit, quantite FROM ligne_livraison WHERE idBonLivraison=?");
                         var psInsert = conn.prepareStatement("INSERT INTO ligne_sortie (idBonSortie, idProduit, quantite) VALUES (?, ?, ?)")) {
                        psSelect.setInt(1, bl.getIdBonLivraison());
                        try (var rs = psSelect.executeQuery()) {
                            while (rs.next()) {
                                psInsert.setInt(1, idBonSortie);
                                psInsert.setInt(2, rs.getInt("idProduit"));
                                psInsert.setInt(3, rs.getInt("quantite"));
                                psInsert.addBatch();
                            }
                        }
                        psInsert.executeBatch();
                    }

                    // Associer bon sortie au bon livraison
                    try (var up = conn.prepareStatement("UPDATE bon_livraison SET idBonSortie=? WHERE idBonLivraison=?")) {
                        up.setInt(1, idBonSortie); up.setInt(2, bl.getIdBonLivraison()); up.executeUpdate();
                    }

                    chargerSorties();
                    AlertUtils.showInfo("Succès","Bon de sortie " + numeroBs + " généré");
                } catch (Exception e){ AlertUtils.showError("Erreur","Génération BS: "+e.getMessage()); }
            }
        });
    }

    @FXML private void handleValiderBonSortie() {
        var bs = tableSorties.getSelectionModel().getSelectedItem();
        if (bs == null) { AlertUtils.showWarning("Attention","Sélectionnez un bon de sortie"); return; }
        if (!bs.getStatut().equals("GENEREE")) { AlertUtils.showWarning("Attention","Le bon doit être GENEREE"); return; }
        changerStatutBonSortie(bs, "VALIDEE", "Validation bon sortie");
    }

    @FXML private void handleEnregistrerSortieBonSortie() {
        var bs = tableSorties.getSelectionModel().getSelectedItem();
        if (bs == null) { AlertUtils.showWarning("Attention","Sélectionnez un bon de sortie"); return; }
        if (!bs.getStatut().equals("VALIDEE")) { AlertUtils.showWarning("Attention","Le bon doit être VALIDEE"); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Enregistrer la sortie (décrément stock + mouvements) ?", ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText("Enregistrement sortie " + bs.getNumero());
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    var conn = DatabaseConnection.getInstance().getConnection();
                    // Charger lignes bon sortie
                    try (var psSelect = conn.prepareStatement("SELECT idProduit, quantite FROM ligne_sortie WHERE idBonSortie=?")) {
                        psSelect.setInt(1, bs.getIdBonSortie());
                        try (var rs = psSelect.executeQuery()) {
                            while (rs.next()) {
                                int idProd = rs.getInt("idProduit");
                                int qte = rs.getInt("quantite");
                                int stockAvant = 0;
                                String ref = "";

                                // Lire stock actuel + ref
                                try (var psStock = conn.prepareStatement("SELECT quantiteStock, reference FROM produits WHERE idProduit=?")) {
                                    psStock.setInt(1, idProd);
                                    try (var rsStock = psStock.executeQuery()) {
                                        if (rsStock.next()) {
                                            stockAvant = rsStock.getInt("quantiteStock");
                                            ref = rsStock.getString("reference");
                                        }
                                    }
                                }

                                int stockApres = stockAvant - qte;
                                if (stockApres < 0) {
                                    AlertUtils.showWarning("Attention","Stock insuffisant pour produit ID " + idProd + " (ref: " + ref + ")");
                                    continue;
                                }

                                // Décrémenter stock
                                try (var upStock = conn.prepareStatement("UPDATE produits SET quantiteStock=? WHERE idProduit=?")) {
                                    upStock.setInt(1, stockApres); upStock.setInt(2, idProd); upStock.executeUpdate();
                                }

                                // Enregistrer mouvement SORTIE
                                try (var insMvt = conn.prepareStatement("INSERT INTO mouvement_stock (dateMouvement, typeMouvement, quantite, stockAvant, stockApres, reference, idProduit) VALUES (CURDATE(), 'SORTIE', ?, ?, ?, ?, ?)")) {
                                    insMvt.setInt(1, qte); insMvt.setInt(2, stockAvant); insMvt.setInt(3, stockApres); insMvt.setString(4, ref); insMvt.setInt(5, idProd);
                                    insMvt.executeUpdate();
                                }
                            }
                        }
                    }

                    // Passer bon sortie à ENREGISTREE
                    try (var upStatut = conn.prepareStatement("UPDATE bon_sortie SET statut='ENREGISTREE' WHERE idBonSortie=?")) {
                        upStatut.setInt(1, bs.getIdBonSortie()); upStatut.executeUpdate();
                    }

                    chargerSorties(); chargerMouvements(); chargerStatistiques();
                    AlertUtils.showInfo("Succès","Sortie enregistrée et stock mis à jour");
                } catch (Exception e){ AlertUtils.showError("Erreur","Enregistrement sortie: "+e.getMessage()); }
            }
        });
    }

    private void changerStatutBonSortie(com.stock.model.document.BonSortie bs, String nouveau, String label) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Confirmer " + label + " ?", ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText(label + " - " + bs.getNumero());
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try (var ps = DatabaseConnection.getInstance().getConnection().prepareStatement("UPDATE bon_sortie SET statut=? WHERE idBonSortie=?")) {
                    ps.setString(1, nouveau); ps.setInt(2, bs.getIdBonSortie()); ps.executeUpdate();
                    chargerSorties();
                    AlertUtils.showInfo("Succès","Statut mis à jour");
                } catch (Exception e){ AlertUtils.showError("Erreur","Maj statut BS: "+e.getMessage()); }
            }
        });
    }

    private void transitionBonLivraison(String from, String to, String label) {
        var bl = tableReceptions.getSelectionModel().getSelectedItem();
        if (bl == null) { AlertUtils.showWarning("Attention","Sélectionnez un bon de livraison"); return; }
        if (!bl.getStatut().equals(from)) { AlertUtils.showWarning("Attention","Statut actuel doit être " + from); return; }
        var confirm = new Alert(Alert.AlertType.CONFIRMATION, "Confirmer " + label + "?", ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText(label + " " + bl.getNumero());
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try (var stmt = DatabaseConnection.getInstance().getConnection().prepareStatement("UPDATE bon_livraison SET statut=? WHERE idBonLivraison=?")) {
                    stmt.setString(1, to); stmt.setInt(2, bl.getIdBonLivraison()); stmt.executeUpdate();
                    chargerReceptions();
                    AlertUtils.showInfo("Succès", "Statut mis à jour");
                } catch (Exception e) { AlertUtils.showError("Erreur", e.getMessage()); }
            }
        });
    }

    private void genererBonSortie() {
        TextInputDialog dialog = new TextInputDialog("BS-" + java.util.UUID.randomUUID().toString().substring(0,8).toUpperCase());
        dialog.setTitle("Bon de Sortie"); dialog.setHeaderText("Générer un bon de sortie"); dialog.setContentText("Numéro:");
        dialog.showAndWait().ifPresent(numero -> {
            try (var stmt = DatabaseConnection.getInstance().getConnection().prepareStatement("INSERT INTO bon_sortie (numero, dateSortie, statut) VALUES (?, CURDATE(), 'GENEREE')")) {
                stmt.setString(1, numero); stmt.executeUpdate();
                chargerSorties(); AlertUtils.showInfo("Succès","Bon de sortie généré");
            } catch (Exception e) { AlertUtils.showError("Erreur", e.getMessage()); }
        });
    }

    private void enregistrerMouvement(boolean entree) {
        Dialog<ButtonType> dialog = new Dialog<>(); dialog.setTitle(entree?"Entrée Stock":"Sortie Stock"); dialog.setHeaderText((entree?"Entrée":"Sortie")+" de stock");
        var grid = new javafx.scene.layout.GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new javafx.geometry.Insets(20,150,10,10));
        var refField = new TextField(); refField.setPromptText("Référence produit");
        var qteField = new TextField(); qteField.setPromptText("Quantité");
        grid.add(new Label("Référence:"),0,0); grid.add(refField,1,0);
        grid.add(new Label("Quantité:"),0,1); grid.add(qteField,1,1);
        dialog.getDialogPane().setContent(grid);
        var okBtn = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);
        dialog.setResultConverter(b -> {
            if (b == okBtn) {
                try {
                    String ref = refField.getText().trim();
                    int qte = Integer.parseInt(qteField.getText().trim());
                    if (ref.isEmpty() || qte <= 0) { AlertUtils.showWarning("Attention","Données invalides"); return null; }
                    var conn = DatabaseConnection.getInstance().getConnection();
                    // Récup produit
                    int idProd = -1, stockAvant = 0;
                    try (var ps = conn.prepareStatement("SELECT idProduit, quantiteStock FROM produits WHERE reference=?")) { ps.setString(1, ref); try (var rs = ps.executeQuery()){ if(rs.next()){ idProd=rs.getInt("idProduit"); stockAvant=rs.getInt("quantiteStock"); } }}
                    if (idProd == -1) { AlertUtils.showWarning("Attention","Produit introuvable"); return null; }
                    int stockApres = entree ? stockAvant + qte : stockAvant - qte;
                    if (!entree && stockApres < 0) { AlertUtils.showWarning("Attention","Stock insuffisant"); return null; }
                    try (var up = conn.prepareStatement("UPDATE produits SET quantiteStock=? WHERE idProduit=?")) { up.setInt(1, stockApres); up.setInt(2, idProd); up.executeUpdate(); }
                    try (var ins = conn.prepareStatement("INSERT INTO mouvement_stock (dateMouvement, typeMouvement, quantite, stockAvant, stockApres, reference, idProduit) VALUES (CURDATE(), ?, ?, ?, ?, ?, ?)")) {
                        ins.setString(1, entree?"ENTREE":"SORTIE"); ins.setInt(2, qte); ins.setInt(3, stockAvant); ins.setInt(4, stockApres); ins.setString(5, ref); ins.setInt(6, idProd); ins.executeUpdate(); }
                    chargerMouvements(); chargerStatistiques(); AlertUtils.showInfo("Succès","Mouvement enregistré");
                } catch (NumberFormatException nfe) { AlertUtils.showError("Erreur","Quantité invalide"); } catch (Exception e) { AlertUtils.showError("Erreur", e.getMessage()); }
            }
            return b;
        });
        dialog.showAndWait();
    }

    private void realiserInventaire() {
        var confirm = new Alert(Alert.AlertType.CONFIRMATION, "Créer un inventaire en cours?", ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText("Inventaire");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try (var stmt = DatabaseConnection.getInstance().getConnection().prepareStatement("INSERT INTO inventaires (dateInventaire, statut) VALUES (CURDATE(),'EN_COURS')")) {
                    stmt.executeUpdate(); chargerStatistiques(); AlertUtils.showInfo("Succès","Inventaire créé");
                } catch (Exception e) { AlertUtils.showError("Erreur", e.getMessage()); }
            }
        });
    }

    @FXML private void handleControlerQuantites() { controlerQuantites(); }

    private void controlerQuantites() {
        try {
            var conn = DatabaseConnection.getInstance().getConnection();
            int ecarts = 0;
            try (var stmt = conn.prepareStatement("SELECT quantiteStock, seuilMin FROM produits"); var rs = stmt.executeQuery()) {
                while (rs.next()) { int q = rs.getInt(1); int s = rs.getInt(2); if (q < s) ecarts++; }
            }
            AlertUtils.showInfo("Contrôle", ecarts + " produit(s) sous le seuil minimum");
        } catch (Exception e) { AlertUtils.showError("Erreur", e.getMessage()); }
    }

    private void signalerEcarts() {
        AlertUtils.showInfo("Écarts", "Fonction de signalement détaillé à implémenter (enregistrement des écarts dans ligne_inventaire)");
    }

    @FXML private void handleDeconnexion() { try { com.stock.util.NavigationManager.getInstance().navigate("/fxml/Login.fxml","Connexion"); } catch (Exception e){ AlertUtils.showError("Erreur", e.getMessage()); } }
    @FXML private void handleRetourAccueil() { try { com.stock.util.NavigationManager.getInstance().navigate("/fxml/MainLayout.fxml","Accueil"); } catch (Exception e){ AlertUtils.showError("Erreur", e.getMessage()); } }
}
