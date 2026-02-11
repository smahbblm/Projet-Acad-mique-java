package com.stock.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import com.stock.model.document.BonCommande;
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
 * Contrôleur pour le dashboard approvisionnement
 */
public class ApprovisionnementController {

    // En-tête
    @FXML private Label lblUtilisateurNom;
    @FXML private Button btnDeconnexion;

    // Statistiques
    @FXML private Label lblCommandesAttente;
    @FXML private Label lblCommandesRecues;
    @FXML private Label lblProduitsAlerte;
    @FXML private Label lblFournisseursActifs;

    // Alertes et actions
    @FXML private ListView<String> listAlertes;
    @FXML private TableView<BonCommande> tableCommandes;
    @FXML private TableColumn<BonCommande, String> colNumero;
    @FXML private TableColumn<BonCommande, String> colDate;
    @FXML private TableColumn<BonCommande, String> colFournisseur;
    @FXML private TableColumn<BonCommande, String> colMontant;
    @FXML private TableColumn<BonCommande, String> colStatut;
    // ...existing code...

    // Pied de page
    @FXML private Label lblDateHeure;

    @FXML
    public void initialize() {
        try {
            chargerInfoUtilisateur();
            initTableCommandes();
            chargerStatistiques();
            chargerAlertes();
            chargerCommandesRecentes();
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

    private void initTableCommandes() {
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));
        colFournisseur.setCellValueFactory(new PropertyValueFactory<>("fournisseur"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    private void chargerStatistiques() {
        try {
            com.stock.util.DatabaseConnection dbConn = com.stock.util.DatabaseConnection.getInstance();
            java.sql.Connection conn = dbConn.getConnection();

            // Commandes en attente
            String sqlAttente = "SELECT COUNT(*) as total FROM bon_commande WHERE statut='EN_ATTENTE'";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlAttente);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lblCommandesAttente.setText(String.valueOf(rs.getInt("total")));
                }
            }

            // Commandes reçues
            String sqlRecues = "SELECT COUNT(*) as total FROM bon_commande WHERE statut='RECUE'";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlRecues);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lblCommandesRecues.setText(String.valueOf(rs.getInt("total")));
                }
            }

            // Produits en alerte
            String sqlAlerte = "SELECT COUNT(*) as total FROM produits WHERE quantiteStock < seuilMin";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlAlerte);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lblProduitsAlerte.setText(String.valueOf(rs.getInt("total")));
                }
            }

            // Fournisseurs actifs
            String sqlFournisseurs = "SELECT COUNT(*) as total FROM fournisseurs";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlFournisseurs);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lblFournisseursActifs.setText(String.valueOf(rs.getInt("total")));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement statistiques: " + e.getMessage());
        }
    }

    private void chargerAlertes() {
        try {
            ObservableList<String> alertes = FXCollections.observableArrayList();
            com.stock.util.DatabaseConnection dbConn = com.stock.util.DatabaseConnection.getInstance();
            java.sql.Connection conn = dbConn.getConnection();

            // Alerte: produits en rupture
            String sqlRupture = "SELECT COUNT(*) as total FROM produits WHERE quantiteStock = 0";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlRupture);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt("total") > 0) {
                    alertes.add("❌ " + rs.getInt("total") + " produit(s) en rupture de stock");
                }
            }

            // Alerte: commandes en attente
            String sqlAttente = "SELECT COUNT(*) as total FROM bon_commande WHERE statut='EN_ATTENTE'";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlAttente);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt("total") > 0) {
                    alertes.add("⚠️ " + rs.getInt("total") + " commande(s) en attente");
                }
            }

            // Alerte: produits en alerte seuil
            String sqlAlerte = "SELECT COUNT(*) as total FROM produits WHERE quantiteStock < seuilMin AND quantiteStock > 0";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlAlerte);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt("total") > 0) {
                    alertes.add("🔔 " + rs.getInt("total") + " produit(s) approchant du seuil minimum");
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

    private void chargerCommandesRecentes() {
        try {
            List<BonCommande> commandes = new ArrayList<>();
            java.sql.Connection conn = DatabaseConnection.getInstance().getConnection();

            String sql = "SELECT bc.numero, bc.dateCommande, f.nom, bc.montantTotal, bc.statut " +
                        "FROM bon_commande bc " +
                        "JOIN fournisseurs f ON bc.idFournisseur = f.idFournisseur " +
                        "ORDER BY bc.dateCommande DESC LIMIT 10";

            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Créer une instance avec le constructeur qui demande 3 params
                    BonCommande bc = new BonCommande(
                        rs.getString("numero"),
                        rs.getDate("dateCommande").toLocalDate(),
                        null  // Fournisseur (pas nécessaire pour affichage tableau)
                    );
                    bc.setStatut(rs.getString("statut"));
                    bc.setMontantTotal((float) rs.getDouble("montantTotal"));
                    commandes.add(bc);
                }
            }

            tableCommandes.setItems(FXCollections.observableArrayList(commandes));
        } catch (Exception e) {
            System.err.println("Erreur chargement commandes: " + e.getMessage());
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

    @FXML
    private void handleCreerCommande() {
        try {
            NavigationManager.getInstance().navigate("/fxml/appro/GestionCommandes.fxml", "Gestion des Commandes");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible d'ouvrir la gestion des commandes: " + e.getMessage());
        }
    }

    @FXML
    private void handleConsulterCommandes() {
        try {
            chargerCommandesRecentes();
            AlertUtils.showError("Info", "Commandes mises à jour");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", e.getMessage());
        }
    }

    @FXML
    private void handleGererProduits() {
        try {
            NavigationManager.getInstance().navigate("/fxml/appro/GestionProduits.fxml", "Gestion des Produits");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible d'ouvrir la gestion des produits: " + e.getMessage());
        }
    }

    @FXML
    private void handleGererFournisseurs() {
        try {
            NavigationManager.getInstance().navigate("/fxml/appro/GestionFournisseurs.fxml", "Gestion des Fournisseurs");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible d'ouvrir la gestion des fournisseurs: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeconnexion() {
        try {
            // SessionManager.getInstance().logout();
            // Naviguer vers la page de connexion
            AlertUtils.showError("Info", "Déconnexion réussie");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", e.getMessage());
        }
    }

    @FXML
    private void handleRetour() {
        try {
            // Naviguer vers le menu principal
            AlertUtils.showError("Info", "Retour au menu");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", e.getMessage());
        }
    }

    public void gererProduits() {
        handleGererProduits();
    }

    public void gererFournisseurs() {
        handleGererFournisseurs();
    }

    public void creerBonCommande() {
        handleCreerCommande();
    }

    public void suivreBonCommande() {
        handleConsulterCommandes();
    }

    public void consulterEtatStock() {
        handleGererProduits();
    }

    public void recevoirAlertes() {
        chargerAlertes();
    }
}
