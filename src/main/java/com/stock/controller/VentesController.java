package com.stock.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import com.stock.model.document.BonLivraison;
import com.stock.model.utilisateur.Utilisateur;
import com.stock.util.SessionManager;
import com.stock.util.AlertUtils;
import com.stock.util.DatabaseConnection;
import com.stock.util.NavigationManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur pour le dashboard ventes (Responsable Ventes)
 * Conforme au diagramme de classes UML
 */
public class VentesController {

    // En-tête
    @FXML private Label lblUtilisateurNom;
    @FXML private Button btnDeconnexion;

    // Statistiques
    @FXML private Label lblClientsActifs;
    @FXML private Label lblLivraisonsAttente;
    @FXML private Label lblLivraisonsLivrees;
    @FXML private Label lblFacturesImpayees;

    // Alertes et actions
    @FXML private ListView<String> listAlertes;
    @FXML private TableView<BonLivraison> tableLivraisons;
    @FXML private TableColumn<BonLivraison, String> colNumero;
    @FXML private TableColumn<BonLivraison, String> colDate;
    @FXML private TableColumn<BonLivraison, String> colClient;
    @FXML private TableColumn<BonLivraison, String> colAdresse;
    @FXML private TableColumn<BonLivraison, String> colStatut;

    // Pied de page
    @FXML private Label lblDateHeure;

    @FXML
    public void initialize() {
        try {
            chargerInfoUtilisateur();
            initTableLivraisons();
            chargerStatistiques();
            chargerAlertes();
            chargerLivraisonsRecentes();
            demarrerHorloge();
        } catch (Exception e) {
            AlertUtils.showError("Erreur lors de l'initialisation", e.getMessage());
        }
    }

    private void chargerInfoUtilisateur() {
        try {
            Utilisateur user = (Utilisateur) SessionManager.getInstance().getCurrentUser();
            if (user != null) {
                lblUtilisateurNom.setText(user.getNom() + " " + user.getPrenom());
            }
        } catch (Exception e) {
            lblUtilisateurNom.setText("Utilisateur");
        }
    }

    private void initTableLivraisons() {
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateLivraison"));
        colClient.setCellValueFactory(new PropertyValueFactory<>("clientNom"));  // Utiliser la méthode helper
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresseLivraison"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    private void chargerStatistiques() {
        try {
            var conn = DatabaseConnection.getInstance().getConnection();

            // Clients actifs
            String sqlClients = "SELECT COUNT(*) as total FROM clients";
            try (var stmt = conn.prepareStatement(sqlClients);
                 var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lblClientsActifs.setText(String.valueOf(rs.getInt("total")));
                }
            }

            // Livraisons en attente
            String sqlAttente = "SELECT COUNT(*) as total FROM bon_livraison WHERE statut='EN_ATTENTE'";
            try (var stmt = conn.prepareStatement(sqlAttente);
                 var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lblLivraisonsAttente.setText(String.valueOf(rs.getInt("total")));
                }
            }

            // Livraisons livrées
            String sqlLivrees = "SELECT COUNT(*) as total FROM bon_livraison WHERE statut='LIVREE'";
            try (var stmt = conn.prepareStatement(sqlLivrees);
                 var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lblLivraisonsLivrees.setText(String.valueOf(rs.getInt("total")));
                }
            }

            // Factures impayées
            String sqlFactures = "SELECT COUNT(*) as total FROM factures WHERE statut='GENEREE'";
            try (var stmt = conn.prepareStatement(sqlFactures);
                 var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lblFacturesImpayees.setText(String.valueOf(rs.getInt("total")));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement statistiques: " + e.getMessage());
        }
    }

    private void chargerAlertes() {
        try {
            ObservableList<String> alertes = FXCollections.observableArrayList();
            var conn = DatabaseConnection.getInstance().getConnection();

            // Alerte: livraisons en retard
            String sqlRetard = "SELECT COUNT(*) as total FROM bon_livraison WHERE statut='EN_ATTENTE' AND dateLivraison < CURDATE()";
            try (var stmt = conn.prepareStatement(sqlRetard);
                 var rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt("total") > 0) {
                    alertes.add("⚠️ " + rs.getInt("total") + " livraison(s) en retard");
                }
            }

            // Alerte: factures impayées
            String sqlImpayees = "SELECT COUNT(*) as total FROM factures WHERE statut='GENEREE'";
            try (var stmt = conn.prepareStatement(sqlImpayees);
                 var rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt("total") > 0) {
                    alertes.add("💰 " + rs.getInt("total") + " facture(s) impayée(s)");
                }
            }

            // Alerte: produits en rupture (impact ventes)
            String sqlRupture = "SELECT COUNT(*) as total FROM produits WHERE quantiteStock = 0";
            try (var stmt = conn.prepareStatement(sqlRupture);
                 var rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt("total") > 0) {
                    alertes.add("❌ " + rs.getInt("total") + " produit(s) indisponible(s)");
                }
            }

            if (alertes.isEmpty()) {
                alertes.add("✅ Aucune alerte");
            }

            listAlertes.setItems(alertes);
        } catch (Exception e) {
            System.err.println("Erreur chargement alertes: " + e.getMessage());
        }
    }

    private void chargerLivraisonsRecentes() {
        try {
            List<BonLivraison> livraisons = new ArrayList<>();
            var conn = DatabaseConnection.getInstance().getConnection();

            String sql = "SELECT bl.idBonLivraison, bl.numero, bl.dateLivraison, bl.adresseLivraison, bl.statut, " +
                        "c.idClient, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
                        "FROM bon_livraison bl " +
                        "JOIN clients c ON bl.idClient = c.idClient " +
                        "ORDER BY bl.dateLivraison DESC LIMIT 10";

            try (var stmt = conn.prepareStatement(sql);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Créer l'objet Client
                    com.stock.model.partenaire.Client client = new com.stock.model.partenaire.Client(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("raisonSociale"),
                        rs.getString("adresse"),
                        rs.getString("telephone"),
                        rs.getString("email")
                    );
                    client.setIdClient(rs.getInt("idClient"));

                    // Créer le BonLivraison avec le client
                    BonLivraison bl = new BonLivraison();
                    bl.setIdBonLivraison(rs.getInt("idBonLivraison"));
                    bl.setNumero(rs.getString("numero"));
                    if (rs.getDate("dateLivraison") != null) {
                        bl.setDateLivraison(rs.getDate("dateLivraison").toLocalDate());
                    }
                    bl.setClient(client);
                    bl.setAdresseLivraison(rs.getString("adresseLivraison"));
                    bl.setStatut(rs.getString("statut"));
                    livraisons.add(bl);
                }
            }

            tableLivraisons.setItems(FXCollections.observableArrayList(livraisons));
        } catch (Exception e) {
            System.err.println("Erreur chargement livraisons: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void demarrerHorloge() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            lblDateHeure.setText(LocalDateTime.now().format(formatter));
        }));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    /**
     * Gérer les clients - Conforme au diagramme UML
     */
    @FXML
    private void handleGererClients() {
        try {
            NavigationManager.getInstance().navigate("/fxml/ventes/GestionClients.fxml", "Gestion des Clients");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible d'ouvrir la gestion des clients: " + e.getMessage());
        }
    }

    /**
     * Créer un bon de livraison - Conforme au diagramme UML
     */
    @FXML
    private void handleCreerBonLivraison() {
        try {
            NavigationManager.getInstance().navigate("/fxml/ventes/GestionLivraisons.fxml", "Gestion des Livraisons");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible d'ouvrir la gestion des livraisons: " + e.getMessage());
        }
    }

    /**
     * Gérer les factures - Conforme au diagramme UML
     */
    @FXML
    private void handleGererFactures() {
        try {
            NavigationManager.getInstance().navigate("/fxml/ventes/GestionFactures.fxml", "Gestion des Factures");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible d'ouvrir la gestion des factures: " + e.getMessage());
        }
    }

    /**
     * Consulter disponibilité des produits - Conforme au diagramme UML
     */
    @FXML
    private void handleConsulterDisponibilite() {
        try {
            var conn = DatabaseConnection.getInstance().getConnection();
            StringBuilder rapport = new StringBuilder("📦 DISPONIBILITÉ DES PRODUITS\n\n");

            String sql = "SELECT reference, designation, quantiteStock, seuilMin FROM produits ORDER BY designation";
            try (var stmt = conn.prepareStatement(sql);
                 var rs = stmt.executeQuery()) {
                int disponibles = 0, alertes = 0, ruptures = 0;
                while (rs.next()) {
                    int qte = rs.getInt("quantiteStock");
                    int seuil = rs.getInt("seuilMin");
                    if (qte == 0) ruptures++;
                    else if (qte < seuil) alertes++;
                    else disponibles++;
                }
                rapport.append("✅ Disponibles: ").append(disponibles).append("\n");
                rapport.append("⚠️ En alerte: ").append(alertes).append("\n");
                rapport.append("❌ En rupture: ").append(ruptures).append("\n");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Disponibilité Stock");
            alert.setHeaderText(null);
            alert.setContentText(rapport.toString());
            alert.showAndWait();
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible de consulter la disponibilité: " + e.getMessage());
        }
    }

    /**
     * Générer un rapport de ventes - Conforme au diagramme UML
     */
    @FXML
    private void handleGenererRapport() {
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Rapport Ventes");
            alert.setHeaderText("Génération du rapport");
            alert.setContentText("📊 Rapport des ventes en cours de génération...\n\n" +
                "Cette fonctionnalité permettra d'exporter:\n" +
                "- Historique des livraisons\n" +
                "- Chiffre d'affaires\n" +
                "- Factures générées/payées\n" +
                "- Statistiques clients");
            alert.showAndWait();
        } catch (Exception e) {
            AlertUtils.showError("Erreur", e.getMessage());
        }
    }

    @FXML
    private void handleGenererRapportVentesDetaille() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Rapport Ventes");
        dialog.setHeaderText("Générer un rapport des ventes");
        var grid = new javafx.scene.layout.GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new javafx.geometry.Insets(15));
        DatePicker dpDe = new DatePicker(java.time.LocalDate.now().minusDays(30));
        DatePicker dpA = new DatePicker(java.time.LocalDate.now());
        grid.add(new Label("Du:"),0,0); grid.add(dpDe,1,0);
        grid.add(new Label("Au:"),0,1); grid.add(dpA,1,1);
        dialog.getDialogPane().setContent(grid);
        var okBtn = new ButtonType("Générer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);
        dialog.setResultConverter(b -> {
            if (b == okBtn) {
                try {
                    var dao = new com.stock.dao.implementation.RapportDAO();
                    var rapport = dao.genererRapport("VENTES", dpDe.getValue(), dpA.getValue());
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Rapport Ventes");
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

    @FXML
    private void handleDeconnexion() {
        try {
            NavigationManager.getInstance().navigate("/fxml/Login.fxml", "Connexion");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", e.getMessage());
        }
    }

    @FXML
    private void handleRetour() {
        try {
            NavigationManager.getInstance().navigate("/fxml/MainLayout.fxml", "Accueil");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", e.getMessage());
        }
    }

    // ========== Méthodes du diagramme UML ==========

    public void gererClients() {
        handleGererClients();
    }

    public void ajouterClient() {
        // Délégué au contrôleur GestionClientsController
    }

    public void modifierClient() {
        // Délégué au contrôleur GestionClientsController
    }

    public void supprimerClient() {
        // Délégué au contrôleur GestionClientsController
    }

    public void consulterDisponibilite() {
        handleConsulterDisponibilite();
    }

    public void creerBonLivraison() {
        handleCreerBonLivraison();
    }

    public void genererFacture() {
        handleGererFactures();
    }

    public void transmettreBonLivraison() {
        // Implémentation dans GestionLivraisonsController
    }

    public void consulterHistoriqueVentes() {
        chargerLivraisonsRecentes();
    }
}

