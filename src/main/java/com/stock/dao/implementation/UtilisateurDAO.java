package com.stock.dao.implementation;

import com.stock.dao.interfaces.IUtilisateurDAO;
import com.stock.model.utilisateur.Utilisateur;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation DAO pour les utilisateurs
 */
public class UtilisateurDAO implements IUtilisateurDAO {
    private Connection connection;

    public UtilisateurDAO() throws Exception {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(Utilisateur utilisateur) throws Exception {
        String sql = "INSERT INTO utilisateurs (nom, prenom, email, motDePasse, role, dateCreation, actif) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getMotDePasse());
            stmt.setString(5, utilisateur.getRole());
            stmt.setObject(6, utilisateur.getDateCreation());
            stmt.setBoolean(7, utilisateur.isActif());
            stmt.executeUpdate();
        }
    }

    @Override
    public Utilisateur read(int id) throws Exception {
        // Implémentation à venir
        return null;
    }

    @Override
    public List<Utilisateur> readAll() throws Exception {
        // Implémentation à venir
        return new ArrayList<>();
    }

    @Override
    public void update(Utilisateur utilisateur) throws Exception {
        String sql = "UPDATE utilisateurs SET nom=?, prenom=?, email=?, motDePasse=?, role=?, actif=? " +
                     "WHERE idUtilisateur=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getMotDePasse());
            stmt.setString(5, utilisateur.getRole());
            stmt.setBoolean(6, utilisateur.isActif());
            stmt.setInt(7, utilisateur.getIdUtilisateur());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM utilisateurs WHERE idUtilisateur=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Utilisateur findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM utilisateurs WHERE email=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUtilisateur(rs);
            }
        }
        return null;
    }

    private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws Exception {
        com.stock.model.utilisateur.UtilisateurGenerique utilisateur = new com.stock.model.utilisateur.UtilisateurGenerique();
        utilisateur.setIdUtilisateur(rs.getInt("idUtilisateur"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setPrenom(rs.getString("prenom"));
        utilisateur.setEmail(rs.getString("email"));
        utilisateur.setMotDePasse(rs.getString("motDePasse"));
        utilisateur.setRole(rs.getString("role"));
        // Conversion java.sql.Date vers LocalDate
        java.sql.Date sqlDate = rs.getDate("dateCreation");
        if (sqlDate != null) {
            utilisateur.setDateCreation(sqlDate.toLocalDate());
        }
        utilisateur.setActif(rs.getBoolean("actif"));
        return utilisateur;
    }
}
