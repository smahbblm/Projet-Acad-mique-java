package com.stock.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.stock.util.AlertUtils;
import com.stock.util.NavigationManager;

import java.time.LocalDate;

/**
 * Contrôleur pour les rapports (Administrateur)
 */
public class RapportsController {

    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private ComboBox<String> comboTypeRapport;
    @FXML private Button btnGenerer;
    @FXML private Button btnExporter;
    @FXML private Button btnRetour;

    @FXML private TableView<Rapport> tableRapports;
    @FXML private TableColumn<Rapport, Integer> colId;
    @FXML private TableColumn<Rapport, String> colType;
    @FXML private TableColumn<Rapport, LocalDate> colDateGeneration;
    @FXML private TableColumn<Rapport, String> colPeriode;

    @FXML private TextArea txtContenuRapport;
    @FXML private Label lblTitreRapport;

    private ObservableList<Rapport> rapports;

    @FXML
    public void initialize() {
        rapports = FXCollections.observableArrayList();
        initTable();
        chargerTypesRapport();
        chargerRapportsExistants();

        // Valeurs par défaut pour les dates
        dateFin.setValue(LocalDate.now());
        dateDebut.setValue(LocalDate.now().minusMonths(1));
    }

    private void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idRapport"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDateGeneration.setCellValueFactory(new PropertyValueFactory<>("dateGeneration"));
        colPeriode.setCellValueFactory(new PropertyValueFactory<>("periode"));
    }

    private void chargerTypesRapport() {
        comboTypeRapport.setItems(FXCollections.observableArrayList(
            "VENTES",
            "ACHATS",
            "STOCK",
            "UTILISATEURS",
            "FINANCIER",
            "INVENTAIRE"
        ));
    }

    private void chargerRapportsExistants() {
        try {
            rapports.clear();
            var conn = com.stock.util.DatabaseConnection.getInstance().getConnection();

            String sql = "SELECT * FROM rapports ORDER BY dateGeneration DESC";
            try (var stmt = conn.prepareStatement(sql);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Rapport rapport = new Rapport();
                    rapport.setIdRapport(rs.getInt("idRapport"));
                    rapport.setType(rs.getString("type"));
                    rapport.setDateGeneration(rs.getObject("dateGeneration", LocalDate.class));
                    rapport.setContenu(rs.getString("contenu"));
                    rapports.add(rapport);
                }
            }

            tableRapports.setItems(rapports);
        } catch (Exception e) {
            System.err.println("Erreur chargement rapports: " + e.getMessage());
        }
    }

    @FXML
    private void handleGenerer() {
        String typeRapport = comboTypeRapport.getValue();
        LocalDate debut = dateDebut.getValue();
        LocalDate fin = dateFin.getValue();

        if (typeRapport == null) {
            AlertUtils.showWarning("Attention", "Veuillez sélectionner un type de rapport");
            return;
        }

        if (debut == null || fin == null) {
            AlertUtils.showWarning("Attention", "Veuillez sélectionner les dates de début et fin");
            return;
        }

        if (debut.isAfter(fin)) {
            AlertUtils.showWarning("Attention", "La date de début doit être antérieure à la date de fin");
            return;
        }

        try {
            String contenu = genererContenuRapport(typeRapport, debut, fin);
            String periode = debut + " - " + fin;

            lblTitreRapport.setText("Rapport " + typeRapport + " - " + periode);
            txtContenuRapport.setText(contenu);

            // Sauvegarder le rapport en base
            sauvegarderRapport(typeRapport, periode, contenu);

            AlertUtils.showInfo("Succès", "Rapport généré avec succès");

        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Erreur lors de la génération du rapport: " + e.getMessage());
        }
    }

    private String genererContenuRapport(String type, LocalDate debut, LocalDate fin) throws Exception {
        StringBuilder contenu = new StringBuilder();
        var conn = com.stock.util.DatabaseConnection.getInstance().getConnection();

        contenu.append("RAPPORT ").append(type).append("\n");
        contenu.append("Période: ").append(debut).append(" - ").append(fin).append("\n");
        contenu.append("Généré le: ").append(LocalDate.now()).append("\n\n");

        switch (type) {
            case "VENTES":
                genererRapportVentes(contenu, debut, fin, conn);
                break;
            case "ACHATS":
                genererRapportAchats(contenu, debut, fin, conn);
                break;
            case "STOCK":
                genererRapportStock(contenu, conn);
                break;
            case "UTILISATEURS":
                genererRapportUtilisateurs(contenu, conn);
                break;
            case "FINANCIER":
                genererRapportFinancier(contenu, debut, fin, conn);
                break;
            case "INVENTAIRE":
                genererRapportInventaire(contenu, conn);
                break;
        }

        return contenu.toString();
    }

    private void genererRapportVentes(StringBuilder contenu, LocalDate debut, LocalDate fin,
                                    java.sql.Connection conn) throws Exception {
        String sql = "SELECT COUNT(*) as nbFactures, SUM(montantTTC) as totalVentes " +
                    "FROM factures WHERE dateFacture BETWEEN ? AND ?";
        try (var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, debut);
            stmt.setObject(2, fin);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    contenu.append("Nombre de factures: ").append(rs.getInt("nbFactures")).append("\n");
                    contenu.append("Total des ventes: ").append(String.format("%.2f", rs.getDouble("totalVentes"))).append(" DH\n");
                }
            }
        }
    }

    private void genererRapportAchats(StringBuilder contenu, LocalDate debut, LocalDate fin,
                                    java.sql.Connection conn) throws Exception {
        String sql = "SELECT COUNT(*) as nbCommandes, SUM(montantTotal) as totalAchats " +
                    "FROM bon_commande WHERE dateCommande BETWEEN ? AND ?";
        try (var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, debut);
            stmt.setObject(2, fin);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    contenu.append("Nombre de commandes: ").append(rs.getInt("nbCommandes")).append("\n");
                    contenu.append("Total des achats: ").append(String.format("%.2f", rs.getDouble("totalAchats"))).append(" DH\n");
                }
            }
        }
    }

    private void genererRapportStock(StringBuilder contenu, java.sql.Connection conn) throws Exception {
        String sql = "SELECT COUNT(*) as nbProduits, SUM(quantiteStock) as totalStock " +
                    "FROM produits";
        try (var stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            if (rs.next()) {
                contenu.append("Nombre de produits: ").append(rs.getInt("nbProduits")).append("\n");
                contenu.append("Total en stock: ").append(rs.getInt("totalStock")).append(" unités\n");
            }
        }

        // Produits en alerte
        String sqlAlerte = "SELECT COUNT(*) as nbAlertes FROM produits WHERE quantiteStock < seuilMin";
        try (var stmt = conn.prepareStatement(sqlAlerte);
             var rs = stmt.executeQuery()) {
            if (rs.next()) {
                contenu.append("Produits en alerte: ").append(rs.getInt("nbAlertes")).append("\n");
            }
        }
    }

    private void genererRapportUtilisateurs(StringBuilder contenu, java.sql.Connection conn) throws Exception {
        String sql = "SELECT role, COUNT(*) as nombre FROM utilisateurs WHERE actif = 1 GROUP BY role";
        try (var stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            contenu.append("Répartition par rôle:\n");
            while (rs.next()) {
                contenu.append("- ").append(rs.getString("role")).append(": ").append(rs.getInt("nombre")).append("\n");
            }
        }
    }

    private void genererRapportFinancier(StringBuilder contenu, LocalDate debut, LocalDate fin,
                                       java.sql.Connection conn) throws Exception {
        // Calcul du bénéfice (ventes - achats)
        String sqlVentes = "SELECT SUM(montantTTC) as ventes FROM factures WHERE dateFacture BETWEEN ? AND ?";
        String sqlAchats = "SELECT SUM(montantTotal) as achats FROM bon_commande WHERE dateCommande BETWEEN ? AND ?";

        double ventes = 0, achats = 0;

        try (var stmt = conn.prepareStatement(sqlVentes)) {
            stmt.setObject(1, debut);
            stmt.setObject(2, fin);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) ventes = rs.getDouble("ventes");
            }
        }

        try (var stmt = conn.prepareStatement(sqlAchats)) {
            stmt.setObject(1, debut);
            stmt.setObject(2, fin);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) achats = rs.getDouble("achats");
            }
        }

        contenu.append("Ventes: ").append(String.format("%.2f", ventes)).append(" DH\n");
        contenu.append("Achats: ").append(String.format("%.2f", achats)).append(" DH\n");
        contenu.append("Bénéfice: ").append(String.format("%.2f", ventes - achats)).append(" DH\n");
    }

    private void genererRapportInventaire(StringBuilder contenu, java.sql.Connection conn) throws Exception {
        String sql = "SELECT COUNT(*) as nbInventaires FROM inventaires";
        try (var stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            if (rs.next()) {
                contenu.append("Nombre d'inventaires: ").append(rs.getInt("nbInventaires")).append("\n");
            }
        }
    }

    private void sauvegarderRapport(String type, String periode, String contenu) throws Exception {
        var conn = com.stock.util.DatabaseConnection.getInstance().getConnection();
        String sql = "INSERT INTO rapports (type, dateGeneration, contenu) VALUES (?, ?, ?)";
        try (var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type);
            stmt.setObject(2, LocalDate.now());
            stmt.setString(3, contenu);
            stmt.executeUpdate();
        }

        // Recharger la liste
        chargerRapportsExistants();
    }

    @FXML
    private void handleExporter() {
        String contenu = txtContenuRapport.getText();
        if (contenu.isEmpty()) {
            AlertUtils.showWarning("Attention", "Veuillez d'abord générer un rapport");
            return;
        }

        AlertUtils.showInfo("Info", "Fonctionnalité d'exportation à implémenter\n\nContenu du rapport:\n" + contenu);
    }

    @FXML
    private void handleRetour() {
        try {
            NavigationManager.getInstance().navigate("/fxml/admin/DashboardAdmin.fxml", "Dashboard Administrateur");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible de retourner au dashboard: " + e.getMessage());
        }
    }

    // Classe interne pour représenter un rapport
    public static class Rapport {
        private int idRapport;
        private String type;
        private LocalDate dateGeneration;
        private String contenu;

        public Rapport() {}

        // Getters et Setters
        public int getIdRapport() { return idRapport; }
        public void setIdRapport(int idRapport) { this.idRapport = idRapport; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public LocalDate getDateGeneration() { return dateGeneration; }
        public void setDateGeneration(LocalDate dateGeneration) { this.dateGeneration = dateGeneration; }

        public String getContenu() { return contenu; }
        public void setContenu(String contenu) { this.contenu = contenu; }

        public String getPeriode() {
            // Pour l'affichage dans le tableau, on pourrait extraire la période du contenu
            // Pour l'instant, on retourne une valeur par défaut
            return "N/A";
        }
    }
}
