package com.stock.dao.implementation;

import com.stock.dao.interfaces.IRapportDAO;
import com.stock.model.systeme.Rapport;
import com.stock.model.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RapportDAO implements IRapportDAO {

/*
    private Connection getConn() throws Exception {
        return DatabaseConnection.getInstance().getConnection();
    }
*/
    private Connection getConn() throws Exception { return DatabaseConnection.getConnection(); }


    @Override
    public void create(Rapport rapport) throws Exception {
        String sql = "INSERT INTO rapports (type, dateGeneration, dateDe, dateA, contenu) VALUES (?, ?, ?, ?, ?)";

        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, rapport.getType());

            ps.setDate(2, java.sql.Date.valueOf(rapport.getDateGeneration()));

            if (rapport.getDateDe() != null) ps.setDate(3, java.sql.Date.valueOf(rapport.getDateDe()));
            else ps.setNull(3, java.sql.Types.DATE);

            if (rapport.getDateA() != null) ps.setDate(4, java.sql.Date.valueOf(rapport.getDateA()));
            else ps.setNull(4, java.sql.Types.DATE);


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
        sb.append("Type: ").append(type)
                .append("\nPériode: ").append(dateDe.format(df))
                .append(" -> ").append(dateA.format(df))
                .append("\n\n");

        switch (type) {
            case "MOUVEMENTS" -> rapportMouvements(sb, dateDe, dateA);
            case "PRODUITS" -> rapportInventaire(sb, dateDe, dateA);
            case "UTILISATEURS" -> rapportUtilisateurs(sb, dateDe, dateA);
            default -> sb.append("Type inconnu\n");
        }

        r.setContenu(sb.toString());
        create(r); // sauvegarde dans DB
        return r;
    }

    private void rapportMouvements(StringBuilder sb, LocalDate d1, LocalDate d2) throws Exception {
        String sql = "SELECT reference, typeMouvement, quantite, dateMouvement FROM mouvement_stock WHERE dateMouvement BETWEEN ? AND ?";
        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(d1));
            ps.setDate(2, java.sql.Date.valueOf(d2));

            try (ResultSet rs = ps.executeQuery()) {
                sb.append("== Tous les Mouvements ==\n");
                while (rs.next()) {
                    sb.append(" - ").append(rs.getString("reference"))
                            .append(" | Type: ").append(rs.getString("typeMouvement"))
                            .append(" | Qté: ").append(rs.getInt("quantite"))
                            .append(" | Date: ").append(rs.getDate("dateMouvement")).append("\n");
                }
                sb.append("\n");
            }
        }
    }

    private void rapportInventaire(StringBuilder sb, LocalDate d1, LocalDate d2) throws Exception {
        String sql = "SELECT reference, designation, quantiteStock FROM produits";
        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            sb.append("== Inventaire ==\n");
            while (rs.next()) {
                sb.append(" - ").append(rs.getString("reference"))
                        .append(" | ").append(rs.getString("designation"))
                        .append(" | Qté: ").append(rs.getInt("quantiteStock")).append("\n");
            }
            sb.append("\n");
        }
    }

    private void rapportUtilisateurs(StringBuilder sb, LocalDate d1, LocalDate d2) throws Exception {
        String sql = "SELECT nom, prenom, dateCreation FROM utilisateurs WHERE dateCreation BETWEEN ? AND ?";
        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(d1));
            ps.setDate(2, java.sql.Date.valueOf(d2));

            try (ResultSet rs = ps.executeQuery()) {
                sb.append("== Utilisateurs créés ==\n");
                while (rs.next()) {
                    sb.append(" - ").append(rs.getString("nom")).append(" ").append(rs.getString("prenom"))
                            .append(" | Créé le : ").append(rs.getDate("dateCreation")).append("\n");
                }
                sb.append("\n");
            }
        }
    }

    @Override
    public Rapport read(int id) throws Exception {
        String sql = "SELECT * FROM rapports WHERE idRapport = ?";
        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate dateDe = rs.getDate("dateDe") != null ? rs.getDate("dateDe").toLocalDate() : null;
                    LocalDate dateA = rs.getDate("dateA") != null ? rs.getDate("dateA").toLocalDate() : null;
                    LocalDate dateGen = rs.getDate("dateGeneration").toLocalDate();

                    Rapport r = new Rapport(rs.getString("type"), dateDe, dateA);
                    r.setIdRapport(rs.getInt("idRapport"));
                    r.setDateGeneration(dateGen);
                    r.setContenu(rs.getString("contenu"));
                    return r;
                }
            }
        }
        return null;
    }

    @Override
    public List<Rapport> readAll() throws Exception {
        List<Rapport> rapports = new ArrayList<>();
        String sql = "SELECT idRapport, type, dateGeneration, dateDe, dateA, contenu FROM rapports ORDER BY dateGeneration DESC";

        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LocalDate dateDe = rs.getDate("dateDe") != null ? rs.getDate("dateDe").toLocalDate() : null;
                LocalDate dateA = rs.getDate("dateA") != null ? rs.getDate("dateA").toLocalDate() : null;
                LocalDate dateGen = rs.getDate("dateGeneration").toLocalDate();

                Rapport r = new Rapport(rs.getString("type"), dateDe, dateA);
                r.setIdRapport(rs.getInt("idRapport"));
                r.setDateGeneration(dateGen);
                r.setContenu(rs.getString("contenu"));
                rapports.add(r);
            }
        }
        return rapports;
    }

    @Override
    public void update(Rapport rapport) throws Exception {
        String sql = "UPDATE rapports SET type=?, dateDe=?, dateA=?, contenu=? WHERE idRapport=?";
        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, rapport.getType());

            if (rapport.getDateDe() != null) ps.setDate(2, java.sql.Date.valueOf(rapport.getDateDe()));
            else ps.setNull(2, java.sql.Types.DATE);

            if (rapport.getDateA() != null) ps.setDate(3, java.sql.Date.valueOf(rapport.getDateA()));
            else ps.setNull(3, java.sql.Types.DATE);

            ps.setString(4, rapport.getContenu());
            ps.setInt(5, rapport.getIdRapport());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM rapports WHERE idRapport=?";
        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Rapport> findByType(String type) throws Exception {
        List<Rapport> rapports = new ArrayList<>();
        String sql = "SELECT * FROM rapports WHERE type=? ORDER BY dateGeneration DESC";

        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, type);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate dateDe = rs.getDate("dateDe") != null ? rs.getDate("dateDe").toLocalDate() : null;
                    LocalDate dateA = rs.getDate("dateA") != null ? rs.getDate("dateA").toLocalDate() : null;
                    LocalDate dateGen = rs.getDate("dateGeneration").toLocalDate();

                    Rapport r = new Rapport(rs.getString("type"), dateDe, dateA);
                    r.setIdRapport(rs.getInt("idRapport"));
                    r.setDateGeneration(dateGen);
                    r.setContenu(rs.getString("contenu"));
                    rapports.add(r);
                }
            }
        }
        return rapports;
    }
}
