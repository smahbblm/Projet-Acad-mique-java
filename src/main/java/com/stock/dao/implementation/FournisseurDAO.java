package com.stock.dao.implementation;

import com.stock.dao.interfaces.IFournisseurDAO;
import com.stock.model.partenaire.Fournisseur;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation DAO pour les fournisseurs
 */
public class FournisseurDAO implements IFournisseurDAO {
    private Connection connection;

    public FournisseurDAO() throws Exception {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(Fournisseur fournisseur) throws Exception {
        String sql = "INSERT INTO fournisseurs (raisonSociale, adresse, telephone, email, contact, conditions, actif) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, fournisseur.getRaisonSociale());
            stmt.setString(2, fournisseur.getAdresse());
            stmt.setString(3, fournisseur.getTelephone());
            stmt.setString(4, fournisseur.getEmail());
            stmt.setString(5, fournisseur.getContact());
            stmt.setString(6, fournisseur.getConditions());
            stmt.setBoolean(7, fournisseur.isActif());
            stmt.executeUpdate();
        }
    }

    @Override
    public Fournisseur read(int id) throws Exception {
        String sql = "SELECT * FROM fournisseurs WHERE idFournisseur=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFournisseur(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Fournisseur> readAll() throws Exception {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM fournisseurs ORDER BY raisonSociale";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                fournisseurs.add(mapResultSetToFournisseur(rs));
            }
        }
        return fournisseurs;
    }

    @Override
    public void update(Fournisseur fournisseur) throws Exception {
        String sql = "UPDATE fournisseurs SET raisonSociale=?, adresse=?, telephone=?, email=?, contact=?, conditions=?, actif=? " +
                     "WHERE idFournisseur=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, fournisseur.getRaisonSociale());
            stmt.setString(2, fournisseur.getAdresse());
            stmt.setString(3, fournisseur.getTelephone());
            stmt.setString(4, fournisseur.getEmail());
            stmt.setString(5, fournisseur.getContact());
            stmt.setString(6, fournisseur.getConditions());
            stmt.setBoolean(7, fournisseur.isActif());
            stmt.setInt(8, fournisseur.getIdFournisseur());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM fournisseurs WHERE idFournisseur=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Fournisseur findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM fournisseurs WHERE email=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFournisseur(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Fournisseur> findByRaisonSociale(String raisonSociale) throws Exception {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM fournisseurs WHERE raisonSociale LIKE ? ORDER BY raisonSociale";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + raisonSociale + "%");
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fournisseurs.add(mapResultSetToFournisseur(rs));
                }
            }
        }
        return fournisseurs;
    }

    private Fournisseur mapResultSetToFournisseur(java.sql.ResultSet rs) throws Exception {
        Fournisseur fournisseur = new Fournisseur(
            rs.getString("raisonSociale"),
            rs.getString("adresse"),
            rs.getString("telephone"),
            rs.getString("email")
        );
        fournisseur.setIdFournisseur(rs.getInt("idFournisseur"));
        fournisseur.setContact(rs.getString("contact"));
        fournisseur.setConditions(rs.getString("conditions"));
        try { fournisseur.setActif(rs.getBoolean("actif")); } catch (Exception ignored) {}
        return fournisseur;
    }
}
