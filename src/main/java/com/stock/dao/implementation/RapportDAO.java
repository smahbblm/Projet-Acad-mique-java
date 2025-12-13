package com.stock.dao.implementation;

import com.stock.dao.interfaces.IRapportDAO;
import com.stock.model.systeme.Rapport;
import com.stock.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * DAO pour la génération et la persistance des rapports.
 */
public class RapportDAO implements IRapportDAO {

    private Connection getConn() throws Exception { return DatabaseConnection.getInstance().getConnection(); }

    @Override
    public void create(Rapport rapport) throws Exception {
        String sql = "INSERT INTO rapports (type, dateGeneration, dateDe, dateA, contenu) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, rapport.getType());

            // Conversion LocalDate -> java.sql.Date
            ps.setDate(2, java.sql.Date.valueOf(rapport.getDateGeneration()));
            ps.setDate(3, rapport.getDateDe() != null ? java.sql.Date.valueOf(rapport.getDateDe()) : null);
            ps.setDate(4, rapport.getDateA() != null ? java.sql.Date.valueOf(rapport.getDateA()) : null);

            ps.setString(5, rapport.getContenu());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) rapport.setIdRapport(rs.getInt(1));
            }
        }
    }


    public Rapport genererRapport(String type, LocalDate dateDe, LocalDate dateA) throws Exception {
        Rapport r = new Rapport(type, dateDe, dateA);
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        sb.append("Type: ").append(type).append("\nPériode: ").append(dateDe.format(df)).append(" -> ").append(dateA.format(df)).append("\n\n");
        switch (type) {
            case "ANALYSE_GLOBAL" -> rapportGlobal(sb, dateDe, dateA);
            case "ACHATS" -> rapportAchats(sb, dateDe, dateA);
            case "VENTES" -> rapportVentes(sb, dateDe, dateA);
            case "INVENTAIRE" -> rapportInventaire(sb, dateDe, dateA);
            default -> sb.append("Type inconnu\n");
        }
        r.setContenu(sb.toString());
        create(r); // persistance
        return r;  // renvoie l'objet enrichi
    }

    private void rapportGlobal(StringBuilder sb, LocalDate d1, LocalDate d2) throws Exception {
        try (Connection c = getConn()) {
            sb.append("== Synthèse Globale ==\n");
            appendCount(c, sb, "Utilisateurs actifs", "SELECT COUNT(*) FROM utilisateurs WHERE actif=1");
            appendCount(c, sb, "Produits", "SELECT COUNT(*) FROM produits");
            appendCount(c, sb, "Commandes (VALIDEE/TRANSMISE)", "SELECT COUNT(*) FROM bon_commande WHERE statut IN ('VALIDEE','TRANSMISE')");
            appendSum(c, sb, "Ventes TTC période", "SELECT COALESCE(SUM(montantTTC),0) FROM factures WHERE dateFacture BETWEEN ? AND ?", d1, d2);
            appendSum(c, sb, "Achats période (montantTotal)", "SELECT COALESCE(SUM(montantTotal),0) FROM bon_commande WHERE dateCommande BETWEEN ? AND ?", d1, d2);
            appendCount(c, sb, "Produits en rupture", "SELECT COUNT(*) FROM produits WHERE quantiteStock=0");
            sb.append("\n");
        }
    }

    private void rapportAchats(StringBuilder sb, LocalDate d1, LocalDate d2) throws Exception {
        try (Connection c = getConn()) {
            sb.append("== Rapport Achats ==\n");
            appendSum(c, sb, "Total achats période", "SELECT COALESCE(SUM(montantTotal),0) FROM bon_commande WHERE dateCommande BETWEEN ? AND ?", d1, d2);
            sb.append("Top 10 commandes:\n");
            String sql = "SELECT numero, montantTotal, statut FROM bon_commande WHERE dateCommande BETWEEN ? AND ? ORDER BY montantTotal DESC LIMIT 10";
            try (PreparedStatement ps = c.prepareStatement(sql)) { ps.setObject(1, d1); ps.setObject(2, d2); try (ResultSet rs = ps.executeQuery()) { while (rs.next()) { sb.append(" - ").append(rs.getString(1)).append(" | ").append(rs.getFloat(2)).append(" | ").append(rs.getString(3)).append("\n"); } } }
            sb.append("\n");
        }
    }

    private void rapportVentes(StringBuilder sb, LocalDate d1, LocalDate d2) throws Exception {
        try (Connection c = getConn()) {
            sb.append("== Rapport Ventes ==\n");
            appendSum(c, sb, "Total ventes TTC période", "SELECT COALESCE(SUM(montantTTC),0) FROM factures WHERE dateFacture BETWEEN ? AND ?", d1, d2);
            appendCount(c, sb, "Factures générées", "SELECT COUNT(*) FROM factures WHERE dateFacture BETWEEN '"+d1+"' AND '"+d2+"' ");
            appendCount(c, sb, "Factures payées", "SELECT COUNT(*) FROM factures WHERE statut='PAYEE' AND dateFacture BETWEEN '"+d1+"' AND '"+d2+"' ");
            sb.append("Top 10 clients (par nombre de factures):\n");
            String sql = "SELECT c.nom, c.prenom, COUNT(f.idFacture) nb FROM factures f JOIN clients c ON f.idClient=c.idClient WHERE f.dateFacture BETWEEN ? AND ? GROUP BY c.idClient ORDER BY nb DESC LIMIT 10";
            try (PreparedStatement ps = c.prepareStatement(sql)) { ps.setObject(1, d1); ps.setObject(2, d2); try (ResultSet rs = ps.executeQuery()) { while (rs.next()) { sb.append(" - ").append(rs.getString(1)).append(" ").append(rs.getString(2)).append(" : ").append(rs.getInt(3)).append(" facture(s)\n"); } } }
            sb.append("\n");
        }
    }

    private void rapportInventaire(StringBuilder sb, LocalDate d1, LocalDate d2) throws Exception {
        try (Connection c = getConn()) {
            sb.append("== Rapport Inventaire ==\n");
            appendCount(c, sb, "Produits sous seuil", "SELECT COUNT(*) FROM produits WHERE quantiteStock < seuilMin");
            appendCount(c, sb, "Produits en rupture", "SELECT COUNT(*) FROM produits WHERE quantiteStock=0");
            appendCount(c, sb, "Mouvements période", "SELECT COUNT(*) FROM mouvement_stock WHERE dateMouvement BETWEEN '"+d1+"' AND '"+d2+"' ");
            sb.append("Liste produits sous seuil (max 15):\n");
            String sql = "SELECT reference, designation, quantiteStock, seuilMin FROM produits WHERE quantiteStock < seuilMin ORDER BY quantiteStock ASC LIMIT 15";
            try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) { while (rs.next()) { sb.append(" - ").append(rs.getString(1)).append(" | ").append(rs.getString(2)).append(" | Stock=").append(rs.getInt(3)).append(" / Seuil=").append(rs.getInt(4)).append("\n"); } }
            sb.append("\n");
        }
    }

    private void appendCount(Connection c, StringBuilder sb, String label, String sql) throws Exception {
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) { if (rs.next()) sb.append(label).append(": ").append(rs.getInt(1)).append("\n"); }
    }

    private void appendSum(Connection c, StringBuilder sb, String label, String sql, LocalDate d1, LocalDate d2) throws Exception {
        try (PreparedStatement ps = c.prepareStatement(sql)) { ps.setObject(1, d1); ps.setObject(2, d2); try (ResultSet rs = ps.executeQuery()) { if (rs.next()) sb.append(label).append(": ").append(String.format("%.2f", rs.getDouble(1))).append("\n"); } }
    }

    @Override
    public Rapport read(int id) throws Exception {
        String sql = "SELECT * FROM rapports WHERE idRapport=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Rapport r = new Rapport(rs.getString("type"), rs.getObject("dateDe", java.time.LocalDate.class), rs.getObject("dateA", java.time.LocalDate.class));
                    r.setIdRapport(rs.getInt("idRapport"));
                    r.setDateGeneration(rs.getObject("dateGeneration", java.time.LocalDate.class));
                    r.setContenu(rs.getString("contenu"));
                    return r;
                }
            }
        }
        return null;
    }

    @Override
    public java.util.List<Rapport> readAll() throws Exception {
        java.util.List<Rapport> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM rapports ORDER BY dateGeneration DESC";
        try (PreparedStatement ps = getConn().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Rapport r = new Rapport(rs.getString("type"), rs.getObject("dateDe", java.time.LocalDate.class), rs.getObject("dateA", java.time.LocalDate.class));
                r.setIdRapport(rs.getInt("idRapport"));
                r.setDateGeneration(rs.getObject("dateGeneration", java.time.LocalDate.class));
                r.setContenu(rs.getString("contenu"));
                list.add(r);
            }
        }
        return list;
    }

    @Override
    public void update(Rapport rapport) throws Exception {
        String sql = "UPDATE rapports SET type=?, dateDe=?, dateA=?, contenu=? WHERE idRapport=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, rapport.getType());
            ps.setObject(2, rapport.getDateDe());
            ps.setObject(3, rapport.getDateA());
            ps.setString(4, rapport.getContenu());
            ps.setInt(5, rapport.getIdRapport());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM rapports WHERE idRapport=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public java.util.List<Rapport> findByType(String type) throws Exception {
        java.util.List<Rapport> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM rapports WHERE type=? ORDER BY dateGeneration DESC";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Rapport r = new Rapport(rs.getString("type"), rs.getObject("dateDe", java.time.LocalDate.class), rs.getObject("dateA", java.time.LocalDate.class));
                    r.setIdRapport(rs.getInt("idRapport"));
                    r.setDateGeneration(rs.getObject("dateGeneration", java.time.LocalDate.class));
                    r.setContenu(rs.getString("contenu"));
                    list.add(r);
                }
            }
        }
        return list;
    }
}
