package com.stock.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import com.stock.util.SessionManager;
import com.stock.util.AlertUtils;
import com.stock.util.NavigationManager;
import com.stock.model.utilisateur.Utilisateur;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Contrôleur pour le dashboard administrateur
 */
public class AdministrateurController {

    // En-tête
    @FXML private Label lblUtilisateurNom;
    @FXML private Button btnDeconnexion;

    // Menu latéral
    @FXML private Button btnAccueil;
    @FXML private Button btnUtilisateurs;
    @FXML private Button btnStatistiques;
    @FXML private Button btnRapports;
    @FXML private Button btnParametres;
    @FXML private Button btnLogs;

    // Statistiques
    @FXML private Label lblNbUtilisateurs;
    @FXML private Label lblNbProduits;
    @FXML private Label lblNbCommandes;
    @FXML private Label lblMontantVentes;

    // Tableaux et listes
    @FXML private TableView<Activite> tableActivites;
    @FXML private TableColumn<Activite, String> colDate;
    @FXML private TableColumn<Activite, String> colUtilisateur;
    @FXML private TableColumn<Activite, String> colAction;
    @FXML private TableColumn<Activite, String> colDetails;
    @FXML private ListView<String> listAlertes;

    // Pied de page
    @FXML private Label lblVersion;
    @FXML private Label lblDateHeure;
    @FXML private Label lblStatutConnexion;

    private Timeline timeline;

    @FXML
    public void initialize() {
        // Charger les informations de l'utilisateur connecté
        chargerInfoUtilisateur();

        // Initialiser les colonnes du tableau
        initTableActivites();

        // Charger les statistiques
        chargerStatistiques();

        // Charger les activités récentes
        chargerActivitesRecentes();

        // Charger les alertes
        chargerAlertes();

        // Démarrer la mise à jour de l'heure
        demarrerHorloge();

        // Appliquer le style au bouton actif
        appliquerStyleBoutonActif(btnAccueil);
    }

    private void chargerInfoUtilisateur() {
        try {
            Utilisateur user = (Utilisateur) SessionManager.getInstance().getCurrentUser();
            if (user != null) {
                lblUtilisateurNom.setText(user.getNom() + " " + user.getPrenom());
            }
        } catch (Exception e) {
            lblUtilisateurNom.setText("Administrateur");
        }
    }

    private void initTableActivites() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colUtilisateur.setCellValueFactory(new PropertyValueFactory<>("utilisateur"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        colDetails.setCellValueFactory(new PropertyValueFactory<>("details"));
    }

    private void chargerStatistiques() {
        try {
            // Charger les statistiques depuis la base de données
            com.stock.util.DatabaseConnection dbConn = com.stock.util.DatabaseConnection.getInstance();
            java.sql.Connection conn = dbConn.getConnection();

            // Compter les utilisateurs
            String sqlUtilisateurs = "SELECT COUNT(*) as total FROM utilisateurs WHERE actif = 1";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlUtilisateurs);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lblNbUtilisateurs.setText(String.valueOf(rs.getInt("total")));
                }
            }

            // Compter les produits
            String sqlProduits = "SELECT COUNT(*) as total FROM produits";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlProduits);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lblNbProduits.setText(String.valueOf(rs.getInt("total")));
                }
            }

            // Compter les commandes
            String sqlCommandes = "SELECT COUNT(*) as total FROM bon_commande";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlCommandes);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lblNbCommandes.setText(String.valueOf(rs.getInt("total")));
                }
            }

            // Calculer le montant des ventes du mois en cours
            String sqlVentes = "SELECT COALESCE(SUM(montantTTC), 0) as total FROM factures " +
                             "WHERE MONTH(dateFacture) = MONTH(CURRENT_DATE()) " +
                             "AND YEAR(dateFacture) = YEAR(CURRENT_DATE())";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlVentes);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double montant = rs.getDouble("total");
                    lblMontantVentes.setText(String.format("%,.2f DH", montant));
                }
            }

            lblStatutConnexion.setText("● Connecté");
            lblStatutConnexion.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 12px;");
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des statistiques: " + e.getMessage());
            e.printStackTrace();
            // Afficher des valeurs par défaut en cas d'erreur
            lblNbUtilisateurs.setText("0");
            lblNbProduits.setText("0");
            lblNbCommandes.setText("0");
            lblMontantVentes.setText("0.00 DH");
        }
    }

    private void chargerActivitesRecentes() {
        try {
            ObservableList<Activite> activites = FXCollections.observableArrayList();

            // Charger les activités depuis la base de données
            com.stock.util.DatabaseConnection dbConn = com.stock.util.DatabaseConnection.getInstance();
            java.sql.Connection conn = dbConn.getConnection();

            // Récupérer les dernières commandes
            String sqlCommandes = "SELECT bc.dateCommande, u.email, bc.numero " +
                                "FROM bon_commande bc " +
                                "JOIN utilisateurs u ON bc.idUtilisateur = u.idUtilisateur " +
                                "ORDER BY bc.dateCommande DESC LIMIT 2";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlCommandes);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    java.sql.Timestamp timestamp = rs.getTimestamp("dateCommande");
                    if (timestamp != null) {
                        LocalDateTime dateTime = timestamp.toLocalDateTime();
                        activites.add(new Activite(
                            dateTime.format(DateTimeFormatter.ofPattern("dd/MM HH:mm")),
                            rs.getString("email"),
                            "Commande",
                            "Création bon de commande " + rs.getString("numero")
                        ));
                    }
                }
            }

            // Récupérer les derniers mouvements de stock
            String sqlStock = "SELECT ms.dateMouvement, u.email, ms.typeMouvement, ms.quantite, p.designation " +
                            "FROM mouvement_stock ms " +
                            "JOIN utilisateurs u ON ms.idUtilisateur = u.idUtilisateur " +
                            "JOIN produits p ON ms.idProduit = p.idProduit " +
                            "ORDER BY ms.dateMouvement DESC LIMIT 2";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlStock);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    java.sql.Timestamp timestamp = rs.getTimestamp("dateMouvement");
                    if (timestamp != null) {
                        LocalDateTime dateTime = timestamp.toLocalDateTime();
                        activites.add(new Activite(
                            dateTime.format(DateTimeFormatter.ofPattern("dd/MM HH:mm")),
                            rs.getString("email"),
                            "Stock",
                            rs.getString("typeMouvement") + " - " +
                            rs.getInt("quantite") + " x " + rs.getString("designation")
                        ));
                    }
                }
            }

            // Récupérer les dernières factures
            String sqlFactures = "SELECT f.dateFacture, u.email, f.numero " +
                               "FROM factures f " +
                               "JOIN bon_livraison bl ON f.idBonLivraison = bl.idBonLivraison " +
                               "JOIN utilisateurs u ON bl.idUtilisateur = u.idUtilisateur " +
                               "ORDER BY f.dateFacture DESC LIMIT 1";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlFactures);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    java.sql.Date sqlDate = rs.getDate("dateFacture");
                    if (sqlDate != null) {
                        LocalDateTime dateTime = sqlDate.toLocalDate().atStartOfDay();
                        activites.add(new Activite(
                            dateTime.format(DateTimeFormatter.ofPattern("dd/MM HH:mm")),
                            rs.getString("email"),
                            "Facture",
                            "Génération facture " + rs.getString("numero")
                        ));
                    }
                }
            }

            // Si aucune donnée, afficher un message
            if (activites.isEmpty()) {
                activites.add(new Activite(
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")),
                    "Système",
                    "Info",
                    "Aucune activité récente dans la base de données"
                ));
            }

            tableActivites.setItems(activites);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des activités: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void chargerAlertes() {
        try {
            ObservableList<String> alertes = FXCollections.observableArrayList();

            // Charger les alertes depuis la base de données
            com.stock.util.DatabaseConnection dbConn = com.stock.util.DatabaseConnection.getInstance();
            java.sql.Connection conn = dbConn.getConnection();

            // Alerte 1: Produits en dessous du seuil minimum
            String sqlProduitsSeuil = "SELECT COUNT(*) as total FROM produits WHERE quantiteStock < seuilMin";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlProduitsSeuil);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int nb = rs.getInt("total");
                    if (nb > 0) {
                        alertes.add("⚠️ " + nb + " produit(s) en dessous du seuil minimum");
                    }
                }
            }

            // Alerte 2: Commandes en attente de validation
            String sqlCommandesAttente = "SELECT COUNT(*) as total FROM bon_commande WHERE statut = 'EN_ATTENTE'";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlCommandesAttente);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int nb = rs.getInt("total");
                    if (nb > 0) {
                        alertes.add("🔔 " + nb + " commande(s) en attente de validation");
                    }
                }
            }

            // Alerte 3: Bons de livraison en attente
            String sqlLivraisonsAttente = "SELECT COUNT(*) as total FROM bon_livraison WHERE statut = 'EN_ATTENTE'";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlLivraisonsAttente);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int nb = rs.getInt("total");
                    if (nb > 0) {
                        alertes.add("📦 " + nb + " bon(s) de livraison en attente");
                    }
                }
            }

            // Alerte 4: Produits avec stock à zéro
            String sqlProduitsZero = "SELECT COUNT(*) as total FROM produits WHERE quantiteStock = 0";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlProduitsZero);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int nb = rs.getInt("total");
                    if (nb > 0) {
                        alertes.add("❌ " + nb + " produit(s) en rupture de stock");
                    }
                }
            }

            // Alerte 5: Factures impayées
            String sqlFacturesImpayees = "SELECT COUNT(*) as total FROM factures WHERE statut = 'IMPAYEE'";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sqlFacturesImpayees);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int nb = rs.getInt("total");
                    if (nb > 0) {
                        alertes.add("💰 " + nb + " facture(s) impayée(s)");
                    }
                }
            }

            // Si aucune alerte, afficher un message positif
            if (alertes.isEmpty()) {
                alertes.add("✅ Aucune alerte - Tout est en ordre");
                alertes.add("📊 Système fonctionnel");
                alertes.add("💡 Pensez à générer les rapports mensuels");
            }

            listAlertes.setItems(alertes);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des alertes: " + e.getMessage());
            e.printStackTrace();
            ObservableList<String> alertes = FXCollections.observableArrayList();
            alertes.add("⚠️ Erreur de chargement des alertes");
            listAlertes.setItems(alertes);
        }
    }

    private void demarrerHorloge() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            lblDateHeure.setText(now.format(formatter));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void appliquerStyleBoutonActif(Button bouton) {
        // Réinitialiser tous les boutons
        btnAccueil.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-alignment: CENTER-LEFT; -fx-padding: 12; -fx-font-size: 14px;");
        btnUtilisateurs.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-alignment: CENTER-LEFT; -fx-padding: 12; -fx-font-size: 14px;");
        btnStatistiques.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-alignment: CENTER-LEFT; -fx-padding: 12; -fx-font-size: 14px;");
        btnRapports.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-alignment: CENTER-LEFT; -fx-padding: 12; -fx-font-size: 14px;");
        btnParametres.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-alignment: CENTER-LEFT; -fx-padding: 12; -fx-font-size: 14px;");
        btnLogs.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-alignment: CENTER-LEFT; -fx-padding: 12; -fx-font-size: 14px;");

        // Appliquer le style au bouton actif
        bouton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-alignment: CENTER-LEFT; -fx-padding: 12; -fx-font-size: 14px;");
    }

    // Gestionnaires d'événements du menu
    @FXML
    private void handleAccueil() {
        appliquerStyleBoutonActif(btnAccueil);
        chargerStatistiques();
        chargerActivitesRecentes();
        chargerAlertes();
    }

    @FXML
    private void handleUtilisateurs() {
        appliquerStyleBoutonActif(btnUtilisateurs);
        try {
            NavigationManager.getInstance().navigate("/fxml/admin/GestionUtilisateurs.fxml", "Gestion des Utilisateurs");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible d'ouvrir la gestion des utilisateurs: " + e.getMessage());
        }
    }

    @FXML
    private void handleStatistiques() {
        appliquerStyleBoutonActif(btnStatistiques);
        try {
            NavigationManager.getInstance().navigate("/fxml/admin/Statistiques.fxml", "Statistiques");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible d'ouvrir les statistiques: " + e.getMessage());
        }
    }

    @FXML
    private void handleRapports() {
        appliquerStyleBoutonActif(btnRapports);
        try {
            NavigationManager.getInstance().navigate("/fxml/admin/Rapports.fxml", "Rapports");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible d'ouvrir les rapports: " + e.getMessage());
        }
    }

    @FXML
    private void handleParametres() {
        appliquerStyleBoutonActif(btnParametres);
        AlertUtils.showInfo("Paramètres", "Page des paramètres en cours de développement");
    }

    @FXML
    private void handleLogs() {
        appliquerStyleBoutonActif(btnLogs);
        AlertUtils.showInfo("Logs Système", "Page des logs en cours de développement");
    }

    // Actions rapides
    @FXML
    private void handleNouvelUtilisateur() {
        AlertUtils.showInfo("Nouvel Utilisateur", "Fonction d'ajout d'utilisateur en cours de développement");
    }

    @FXML
    private void handleRapportMensuel() {
        AlertUtils.showInfo("Rapport Mensuel", "Génération du rapport mensuel...\nFonctionnalité en cours de développement");
    }

    @FXML
    private void handleBackupBD() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Backup de la base de données");
        confirm.setContentText("Voulez-vous vraiment effectuer un backup de la base de données?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                AlertUtils.showInfo("Backup", "Backup de la base de données en cours...\nFonctionnalité en cours de développement");
            }
        });
    }

    @FXML
    private void handleActualiser() {
        chargerStatistiques();
        chargerActivitesRecentes();
        chargerAlertes();
        AlertUtils.showInfo("Actualisation", "Données actualisées avec succès!");
    }

    @FXML
    private void handleDeconnexion() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Déconnexion");
        confirm.setHeaderText("Confirmer la déconnexion");
        confirm.setContentText("Êtes-vous sûr de vouloir vous déconnecter?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Arrêter le timeline
                    if (timeline != null) {
                        timeline.stop();
                    }

                    // Déconnecter l'utilisateur
                    SessionManager.getInstance().invalidateSession();

                    // Retour à l'écran de connexion
                    NavigationManager.getInstance().navigate("/fxml/Login.fxml", "Connexion");
                } catch (Exception e) {
                    AlertUtils.showError("Erreur", "Erreur lors de la déconnexion: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleGenererRapportGlobal() {
        javafx.scene.control.Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Rapport Global");
        dialog.setHeaderText("Générer un rapport d'analyse global");
        var grid = new javafx.scene.layout.GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new javafx.geometry.Insets(15));
        DatePicker dpDe = new DatePicker(java.time.LocalDate.now().minusDays(30));
        DatePicker dpA = new DatePicker(java.time.LocalDate.now());
        ComboBox<String> comboType = new ComboBox<>(FXCollections.observableArrayList("ANALYSE_GLOBAL","ACHATS","VENTES","INVENTAIRE"));
        comboType.setValue("ANALYSE_GLOBAL");
        grid.add(new Label("Du:"),0,0); grid.add(dpDe,1,0);
        grid.add(new Label("Au:"),0,1); grid.add(dpA,1,1);
        grid.add(new Label("Type:"),0,2); grid.add(comboType,1,2);
        dialog.getDialogPane().setContent(grid);
        var btGen = new ButtonType("Générer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btGen, ButtonType.CANCEL);
        dialog.setResultConverter(b -> {
            if (b == btGen) {
                try {
                    var dao = new com.stock.dao.implementation.RapportDAO();
                    var rapport = dao.genererRapport(comboType.getValue(), dpDe.getValue(), dpA.getValue());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Rapport généré");
                    alert.setHeaderText("Rapport ID: " + rapport.getIdRapport());
                    alert.getDialogPane().setExpandableContent(new TextArea(rapport.getContenu()));
                    alert.showAndWait();
                } catch (Exception e) {
                    AlertUtils.showError("Erreur", "Génération rapport: " + e.getMessage());
                }
            }
            return b;
        });
        dialog.showAndWait();
    }

    // Classe interne pour les activités
    public static class Activite {
        private String date;
        private String utilisateur;
        private String action;
        private String details;

        public Activite(String date, String utilisateur, String action, String details) {
            this.date = date;
            this.utilisateur = utilisateur;
            this.action = action;
            this.details = details;
        }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public String getUtilisateur() { return utilisateur; }
        public void setUtilisateur(String utilisateur) { this.utilisateur = utilisateur; }
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public String getDetails() { return details; }
        public void setDetails(String details) { this.details = details; }
    }
}
