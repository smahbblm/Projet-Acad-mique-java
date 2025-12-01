package com.stock.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import com.stock.model.document.BonLivraison;
import com.stock.model.document.LigneLivraison;
import com.stock.model.partenaire.Client;
import com.stock.model.produit.Produit;
import com.stock.util.DatabaseConnection;
import com.stock.util.AlertUtils;
import com.stock.util.NavigationManager;

/**
 * Contrôleur pour la gestion des bons de livraison
 * Flux conforme au diagramme UML: créer -> valider -> transmettre -> livrer
 */
public class GestionLivraisonsController {

    // Filtres
    @FXML private TextField txtRecherche;
    @FXML private ComboBox<String> comboStatut;

    // Table Bons de Livraison
    @FXML private TableView<BonLivraison> tableLivraisons;
    @FXML private TableColumn<BonLivraison, Integer> colId;
    @FXML private TableColumn<BonLivraison, String> colNumero;
    @FXML private TableColumn<BonLivraison, LocalDate> colDate;
    @FXML private TableColumn<BonLivraison, String> colClient;
    @FXML private TableColumn<BonLivraison, String> colAdresse;
    @FXML private TableColumn<BonLivraison, String> colStatut;

    // Lignes
    @FXML private TableView<LigneLivraison> tableLignes;
    @FXML private TableColumn<LigneLivraison, String> colProdRef;
    @FXML private TableColumn<LigneLivraison, String> colProdDes;
    @FXML private TableColumn<LigneLivraison, Integer> colQuantite;
    @FXML private TableColumn<LigneLivraison, Float> colPrix;
    @FXML private TableColumn<LigneLivraison, Float> colSousTotal;

    // Infos sélection
    @FXML private Label lblClientSelection;
    @FXML private Label lblNumeroSelection;
    @FXML private Label lblStatutSelection;
    @FXML private Label lblTotalHT;
    @FXML private Label lblTotalTTC;
    @FXML private Label lblCompteur;

    private ObservableList<BonLivraison> bons;
    private ObservableList<LigneLivraison> lignes;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        bons = FXCollections.observableArrayList();
        lignes = FXCollections.observableArrayList();
        initTables();
        initStatutCombo();
        chargerBonsLivraison();
        installerSelectionListener();
    }

    private void initTables() {
        // Bons de Livraison
        colId.setCellValueFactory(new PropertyValueFactory<>("idBonLivraison"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateLivraison"));
        colClient.setCellValueFactory(new PropertyValueFactory<>("clientNom"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresseLivraison"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        colDate.setCellFactory(col -> new TableCell<BonLivraison, LocalDate>() {
            @Override protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.format(dateFormatter));
            }
        });

        colStatut.setCellFactory(col -> new TableCell<BonLivraison, String>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                switch (item) {
                    case "BROUILLON" -> setStyle("-fx-text-fill:#7f8c8d;");
                    case "EN_ATTENTE" -> setStyle("-fx-text-fill:#f39c12;");
                    case "VALIDEE" -> setStyle("-fx-text-fill:#2980b9; -fx-font-weight:bold;");
                    case "TRANSMISE" -> setStyle("-fx-text-fill:#8e44ad; -fx-font-weight:bold;");
                    case "LIVREE" -> setStyle("-fx-text-fill:#27ae60; -fx-font-weight:bold;");
                    case "ANNULEE" -> setStyle("-fx-text-fill:#e74c3c; -fx-font-weight:bold;");
                }
            }
        });

        // Lignes
        colProdRef.setCellValueFactory(data -> {
            Produit p = data.getValue().getProduit();
            return javafx.beans.property.SimpleStringProperty.stringExpression(javafx.beans.binding.Bindings.createStringBinding(() -> p != null ? p.getReference() : ""));
        });
        colProdDes.setCellValueFactory(data -> {
            Produit p = data.getValue().getProduit();
            return javafx.beans.property.SimpleStringProperty.stringExpression(javafx.beans.binding.Bindings.createStringBinding(() -> p != null ? p.getDesignation() : ""));
        });
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        colSousTotal.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getSousTotal()).asObject());

        colPrix.setCellFactory(col -> new TableCell<LigneLivraison, Float>() {
            @Override protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.2f", item));
            }
        });
        colSousTotal.setCellFactory(col -> new TableCell<LigneLivraison, Float>() {
            @Override protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.2f", item));
            }
        });
    }

    private void initStatutCombo() {
        comboStatut.setItems(FXCollections.observableArrayList("", "BROUILLON", "EN_ATTENTE", "VALIDEE", "TRANSMISE", "LIVREE", "ANNULEE"));
    }

    private void installerSelectionListener() {
        tableLivraisons.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> afficherDetails(selected));
    }

    private void afficherDetails(BonLivraison bl) {
        lignes.clear();
        if (bl == null) {
            lblClientSelection.setText("-");
            lblNumeroSelection.setText("-");
            lblStatutSelection.setText("-");
            lblTotalHT.setText("0.00");
            lblTotalTTC.setText("0.00");
            tableLignes.setItems(lignes);
            return;
        }
        lblClientSelection.setText(bl.getClientNom());
        lblNumeroSelection.setText(bl.getNumero());
        lblStatutSelection.setText(bl.getStatut());
        chargerLignes(bl.getIdBonLivraison());
        calculerTotaux();
    }

    private void calculerTotaux() {
        double totalHT = lignes.stream().mapToDouble(l -> l.getSousTotal()).sum();
        double totalTTC = totalHT * 1.20; // TVA 20%
        lblTotalHT.setText(String.format("%.2f", totalHT));
        lblTotalTTC.setText(String.format("%.2f", totalTTC));
    }

    private void chargerBonsLivraison() {
        bons.clear();
        try {
            var conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT bl.idBonLivraison, bl.numero, bl.dateLivraison, bl.statut, bl.adresseLivraison, " +
                         "c.idClient, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
                         "FROM bon_livraison bl JOIN clients c ON bl.idClient = c.idClient ORDER BY bl.dateLivraison DESC";
            try (var stmt = conn.prepareStatement(sql); var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Client client = new Client(
                        rs.getString("nom"), rs.getString("prenom"), rs.getString("raisonSociale"),
                        rs.getString("adresse"), rs.getString("telephone"), rs.getString("email")
                    );
                    client.setIdClient(rs.getInt("idClient"));
                    BonLivraison bl = new BonLivraison();
                    bl.setIdBonLivraison(rs.getInt("idBonLivraison"));
                    bl.setNumero(rs.getString("numero"));
                    if (rs.getDate("dateLivraison") != null) bl.setDateLivraison(rs.getDate("dateLivraison").toLocalDate());
                    bl.setStatut(rs.getString("statut"));
                    bl.setAdresseLivraison(rs.getString("adresseLivraison"));
                    bl.setClient(client);
                    bons.add(bl);
                }
            }
            tableLivraisons.setItems(bons);
            lblCompteur.setText(bons.size() + " BL");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Chargement bons: " + e.getMessage());
        }
    }

    private void chargerLignes(int idBonLivraison) {
        lignes.clear();
        try {
            var conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT ll.idLigne, ll.quantite, ll.prixUnitaire, p.idProduit, p.reference, p.designation " +
                         "FROM ligne_livraison ll JOIN produits p ON ll.idProduit = p.idProduit " +
                         "WHERE ll.idBonLivraison=?";
            try (var stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idBonLivraison);
                try (var rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Produit p = new Produit(
                            rs.getString("reference"),
                            rs.getString("designation"),
                            0f, // prixAchat non utile ici
                            rs.getFloat("prixUnitaire"),
                            0,0,0,
                            null
                        );
                        p.setIdProduit(rs.getInt("idProduit"));
                        LigneLivraison ligne = new LigneLivraison();
                        ligne.setIdLigne(rs.getInt("idLigne"));
                        ligne.setProduit(p);
                        ligne.setQuantite(rs.getInt("quantite"));
                        ligne.setPrixUnitaire(rs.getFloat("prixUnitaire"));
                        lignes.add(ligne);
                    }
                }
            }
            tableLignes.setItems(lignes);
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Chargement lignes: " + e.getMessage());
        }
    }

    // Actions
    @FXML private void handleFiltrer() {
        String recherche = txtRecherche.getText().toLowerCase();
        String statut = comboStatut.getValue();
        ObservableList<BonLivraison> filtered = FXCollections.observableArrayList();
        for (BonLivraison bl : bons) {
            boolean matchRecherche = recherche.isEmpty() ||
                (bl.getNumero() != null && bl.getNumero().toLowerCase().contains(recherche)) ||
                (bl.getClientNom() != null && bl.getClientNom().toLowerCase().contains(recherche)) ||
                (bl.getAdresseLivraison() != null && bl.getAdresseLivraison().toLowerCase().contains(recherche));
            boolean matchStatut = statut == null || statut.isEmpty() || statut.equals(bl.getStatut());
            if (matchRecherche && matchStatut) filtered.add(bl);
        }
        tableLivraisons.setItems(filtered);
        lblCompteur.setText(filtered.size() + " BL");
    }

    @FXML private void handleReinitialiser() { txtRecherche.clear(); comboStatut.setValue(""); tableLivraisons.setItems(bons); lblCompteur.setText(bons.size()+" BL"); }

    @FXML private void handleNouveau() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nouveau Bon de Livraison");
        dialog.setHeaderText("Créer un bon de livraison brouillon");
        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20,150,10,10));
        TextField numeroField = new TextField("BL-" + UUID.randomUUID().toString().substring(0,8).toUpperCase());
        numeroField.setEditable(false);
        DatePicker dateLivraisonPicker = new DatePicker(LocalDate.now());
        ComboBox<Client> clientCombo = new ComboBox<>(); clientCombo.setPrefWidth(250);
        TextArea adresseArea = new TextArea(); adresseArea.setPromptText("Adresse livraison"); adresseArea.setPrefRowCount(3);
        try { clientCombo.setItems(FXCollections.observableArrayList(chargerClients())); clientCombo.setCellFactory(cb -> new ListCell<>(){ @Override protected void updateItem(Client item, boolean empty){ super.updateItem(item,empty); setText(empty||item==null?null:item.getNom()+" "+item.getPrenom());}}); } catch (Exception e){ AlertUtils.showError("Erreur","Clients non chargés"); }
        grid.add(new Label("Numéro:"),0,0); grid.add(numeroField,1,0);
        grid.add(new Label("Date Livraison:"),0,1); grid.add(dateLivraisonPicker,1,1);
        grid.add(new Label("Client:"),0,2); grid.add(clientCombo,1,2);
        grid.add(new Label("Adresse:"),0,3); grid.add(adresseArea,1,3);
        dialog.getDialogPane().setContent(grid);
        ButtonType creerBtn = new ButtonType("Créer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(creerBtn, ButtonType.CANCEL);
        dialog.setResultConverter(btn -> {
            if (btn == creerBtn) {
                try {
                    if (clientCombo.getValue()==null) { AlertUtils.showWarning("Attention","Client obligatoire"); return null; }
                    var conn = DatabaseConnection.getInstance().getConnection();
                    String sql = "INSERT INTO bon_livraison (numero, dateLivraison, statut, adresseLivraison, idClient) VALUES (?, ?, 'BROUILLON', ?, ?)";
                    try (var stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, numeroField.getText());
                        stmt.setObject(2, dateLivraisonPicker.getValue());
                        stmt.setString(3, adresseArea.getText().trim());
                        stmt.setInt(4, clientCombo.getValue().getIdClient());
                        stmt.executeUpdate();
                    }
                    chargerBonsLivraison();
                    AlertUtils.showInfo("Succès","Bon de livraison créé");
                } catch (Exception e) { AlertUtils.showError("Erreur","Création: "+e.getMessage()); }
            }
            return btn;
        });
        dialog.showAndWait();
    }

    private List<Client> chargerClients() throws Exception {
        List<Client> list = new ArrayList<>();
        var conn = DatabaseConnection.getInstance().getConnection();
        try (var stmt = conn.prepareStatement("SELECT * FROM clients ORDER BY nom, prenom"); var rs = stmt.executeQuery()) {
            while (rs.next()) {
                Client c = new Client(rs.getString("nom"), rs.getString("prenom"), rs.getString("raisonSociale"), rs.getString("adresse"), rs.getString("telephone"), rs.getString("email"));
                c.setIdClient(rs.getInt("idClient"));
                list.add(c);
            }
        }
        return list;
    }

    @FXML private void handleAjouterLigne() {
        BonLivraison bl = tableLivraisons.getSelectionModel().getSelectedItem();
        if (bl == null) { AlertUtils.showWarning("Attention","Sélectionnez un bon de livraison"); return; }
        if (!(bl.getStatut().equals("BROUILLON") || bl.getStatut().equals("EN_ATTENTE"))) {
            AlertUtils.showWarning("Attention","Ajout de lignes seulement pour BROUILLON ou EN_ATTENTE"); return; }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une ligne"); dialog.setHeaderText("Sélection produit et quantité");
        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20,150,10,10));
        ComboBox<Produit> produitCombo = new ComboBox<>(); produitCombo.setPrefWidth(280);
        TextField qteField = new TextField(); qteField.setPromptText("Quantité");
        Label stockLabel = new Label("Stock: -"); stockLabel.setStyle("-fx-text-fill:#555;");
        try {
            var conn = DatabaseConnection.getInstance().getConnection();
            var produitsList = new java.util.ArrayList<Produit>();
            try (var ps = conn.prepareStatement("SELECT idProduit, reference, designation, prixVente, quantiteStock, prixAchat, seuilMin, seuilMax, categorie FROM produits ORDER BY designation"); var rs = ps.executeQuery()) {
                while (rs.next()) {
                    Produit p = new Produit(rs.getString("reference"), rs.getString("designation"), rs.getFloat("prixAchat"), rs.getFloat("prixVente"), rs.getInt("quantiteStock"), rs.getInt("seuilMin"), rs.getInt("seuilMax"), rs.getString("categorie"));
                    p.setIdProduit(rs.getInt("idProduit"));
                    produitsList.add(p);
                }
            }
            produitCombo.setItems(FXCollections.observableArrayList(produitsList));
            produitCombo.setCellFactory(cb -> new ListCell<>() { @Override protected void updateItem(Produit item, boolean empty){ super.updateItem(item, empty); setText(empty||item==null?null: item.getReference()+" - "+item.getDesignation()); }});
            produitCombo.setButtonCell(new ListCell<>() { @Override protected void updateItem(Produit item, boolean empty){ super.updateItem(item, empty); setText(empty||item==null?null: item.getReference()+" - "+item.getDesignation()); }});
            produitCombo.valueProperty().addListener((obs,o,n)-> { if(n!=null) stockLabel.setText("Stock: "+n.getQuantiteStock()); });
        } catch (Exception e){ AlertUtils.showError("Erreur","Chargement produits: "+e.getMessage()); }
        grid.add(new Label("Produit:"),0,0); grid.add(produitCombo,1,0);
        grid.add(new Label("Quantité:"),0,1); grid.add(qteField,1,1);
        grid.add(stockLabel,1,2);
        dialog.getDialogPane().setContent(grid);
        ButtonType addBtn = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtn, ButtonType.CANCEL);
        dialog.setResultConverter(b -> {
            if (b == addBtn) {
                try {
                    Produit p = produitCombo.getValue();
                    if (p == null) { AlertUtils.showWarning("Attention","Produit requis"); return null; }
                    int qte = Integer.parseInt(qteField.getText().trim());
                    if (qte <= 0) { AlertUtils.showWarning("Attention","Quantité > 0 requise"); return null; }
                    if (qte > p.getQuantiteStock()) { AlertUtils.showWarning("Attention","Quantité demandée au-dessus du stock disponible"); return null; }
                    var conn = DatabaseConnection.getInstance().getConnection();
                    try (var ps = conn.prepareStatement("INSERT INTO ligne_livraison (idBonLivraison, idProduit, quantite, prixUnitaire) VALUES (?,?,?,?)")) {
                        ps.setInt(1, bl.getIdBonLivraison());
                        ps.setInt(2, p.getIdProduit());
                        ps.setInt(3, qte);
                        ps.setFloat(4, p.getPrixVente());
                        ps.executeUpdate();
                    }
                    chargerLignes(bl.getIdBonLivraison());
                    calculerTotaux();
                    AlertUtils.showInfo("Succès","Ligne ajoutée");
                } catch (NumberFormatException nfe){ AlertUtils.showError("Erreur","Quantité invalide"); } catch (Exception ex){ AlertUtils.showError("Erreur","Insertion ligne: "+ex.getMessage()); }
            }
            return b;
        });
        dialog.showAndWait();
    }

    @FXML private void handleValider() {
        BonLivraison bl = tableLivraisons.getSelectionModel().getSelectedItem();
        if (bl == null) { AlertUtils.showWarning("Attention","Sélectionnez un bon"); return; }
        if (!(bl.getStatut().equals("BROUILLON") || bl.getStatut().equals("EN_ATTENTE"))) { AlertUtils.showWarning("Attention","Statut incompatible pour validation"); return; }
        changerStatut(bl, "VALIDEE", "Validation");
    }

    @FXML private void handleTransmettre() {
        BonLivraison bl = tableLivraisons.getSelectionModel().getSelectedItem();
        if (bl == null) { AlertUtils.showWarning("Attention","Sélectionnez un bon"); return; }
        if (!bl.getStatut().equals("VALIDEE")) { AlertUtils.showWarning("Attention","Le bon doit être VALIDEE pour transmission"); return; }
        changerStatut(bl, "TRANSMISE", "Transmission au magasinier");
    }

    @FXML private void handleMarquerLivree() {
        BonLivraison bl = tableLivraisons.getSelectionModel().getSelectedItem();
        if (bl == null) { AlertUtils.showWarning("Attention","Sélectionnez un bon"); return; }
        if (!bl.getStatut().equals("TRANSMISE")) { AlertUtils.showWarning("Attention","Le bon doit être TRANSMISE pour marquer LIVREE"); return; }
        // Ici on ne modifie pas le stock (sera fait via bon de sortie côté magasinier), commentaire explicatif.
        changerStatut(bl, "LIVREE", "Livraison effectuée");
    }

    @FXML private void handleAnnuler() {
        BonLivraison bl = tableLivraisons.getSelectionModel().getSelectedItem();
        if (bl == null) { AlertUtils.showWarning("Attention","Sélectionnez un bon"); return; }
        if (bl.getStatut().equals("LIVREE")) { AlertUtils.showWarning("Attention","Impossible d'annuler un bon livré"); return; }
        changerStatut(bl, "ANNULEE", "Annulation");
    }

    @FXML private void handleRetour() {
        try { NavigationManager.getInstance().navigate("/fxml/ventes/DashboardVentes.fxml", "Dashboard Ventes"); } catch (Exception e){ AlertUtils.showError("Erreur","Navigation: "+e.getMessage()); }
    }

    @FXML private void handleGenererFacture() {
        BonLivraison bl = tableLivraisons.getSelectionModel().getSelectedItem();
        if (bl == null) { AlertUtils.showWarning("Attention","Sélectionnez un bon" ); return; }
        if (!(bl.getStatut().equals("LIVREE") || bl.getStatut().equals("VALIDEE"))) { AlertUtils.showWarning("Attention","Le bon doit être VALIDEE ou LIVREE" ); return; }
        // Calcul des montants
        double totalHT = lignes.stream().mapToDouble(LigneLivraison::getSousTotal).sum();
        double tva = totalHT * 0.20;
        double ttc = totalHT + tva;
        Dialog<ButtonType> dialog = new Dialog<>(); dialog.setTitle("Générer Facture"); dialog.setHeaderText("Création facture pour BL "+bl.getNumero());
        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(15));
        TextField numField = new TextField("FACT-"+java.util.UUID.randomUUID().toString().substring(0,8).toUpperCase()); numField.setEditable(false);
        DatePicker dpEcheance = new DatePicker(LocalDate.now().plusDays(30));
        Label lblMontantHT = new Label(String.format("%.2f",totalHT));
        Label lblTVA = new Label(String.format("%.2f",tva));
        Label lblTTC = new Label(String.format("%.2f",ttc));
        grid.add(new Label("Numéro:"),0,0); grid.add(numField,1,0);
        grid.add(new Label("Échéance:"),0,1); grid.add(dpEcheance,1,1);
        grid.add(new Label("Montant HT:"),0,2); grid.add(lblMontantHT,1,2);
        grid.add(new Label("TVA (20%):"),0,3); grid.add(lblTVA,1,3);
        grid.add(new Label("Montant TTC:"),0,4); grid.add(lblTTC,1,4);
        dialog.getDialogPane().setContent(grid);
        ButtonType genBtn = new ButtonType("Générer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(genBtn, ButtonType.CANCEL);
        dialog.setResultConverter(b -> {
            if (b == genBtn) {
                try {
                    var conn = DatabaseConnection.getInstance().getConnection();
                    int idClient = bl.getClient()!=null? bl.getClient().getIdClient():0;
                    if (idClient==0) { AlertUtils.showWarning("Attention","Client introuvable pour facture"); return null; }
                    try (var ps = conn.prepareStatement("INSERT INTO factures (numero, dateFacture, dateEcheance, montantHT, montantTVA, montantTTC, statut, idClient) VALUES (?,?,?,?,?,?, 'GENEREE', ?) ", java.sql.Statement.RETURN_GENERATED_KEYS)) {
                        ps.setString(1, numField.getText());
                        ps.setObject(2, LocalDate.now());
                        ps.setObject(3, dpEcheance.getValue());
                        ps.setDouble(4, totalHT);
                        ps.setDouble(5, tva);
                        ps.setDouble(6, ttc);
                        ps.setInt(7, idClient);
                        ps.executeUpdate();
                        try (var rs = ps.getGeneratedKeys()) { if (rs.next()) {
                            int idFacture = rs.getInt(1);
                            try (var up = conn.prepareStatement("UPDATE bon_livraison SET idFacture=? WHERE idBonLivraison=?")) { up.setInt(1,idFacture); up.setInt(2, bl.getIdBonLivraison()); up.executeUpdate(); }
                        }}
                    }
                    AlertUtils.showInfo("Succès","Facture générée");
                } catch (Exception e){ AlertUtils.showError("Erreur","Génération facture: "+e.getMessage()); }
            }
            return b;
        });
        dialog.showAndWait();
    }

    private void changerStatut(BonLivraison bl, String nouveau, String label) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Confirmer "+label+"?", ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText(label+" - "+bl.getNumero());
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try (var ps = DatabaseConnection.getInstance().getConnection().prepareStatement("UPDATE bon_livraison SET statut=? WHERE idBonLivraison=?")) {
                    ps.setString(1, nouveau); ps.setInt(2, bl.getIdBonLivraison()); ps.executeUpdate();
                    chargerBonsLivraison();
                    AlertUtils.showInfo("Succès","Statut mis à jour");
                } catch (Exception e){ AlertUtils.showError("Erreur","Maj statut: "+e.getMessage()); }
            }
        });
    }
}
