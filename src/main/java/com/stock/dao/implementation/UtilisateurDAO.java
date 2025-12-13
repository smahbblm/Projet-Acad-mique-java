package com.stock.dao.implementation;

import com.stock.dao.interfaces.IUtilisateurDAO;
import com.stock.model.utilisateur.Utilisateur;
import com.stock.model.utilisateur.UserFactory;
import com.stock.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO implements IUtilisateurDAO {

    private final Connection connection;

    public UtilisateurDAO() throws Exception {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(Utilisateur u) throws Exception {
        String sql = "INSERT INTO utilisateurs (nom, prenom, email, motDePasse, role, dateCreation, actif) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, u.getNom());
            stmt.setString(2, u.getPrenom());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getMotDePasse());
            stmt.setString(5, u.getRole());
            stmt.setObject(6, u.getDateCreation());
            stmt.setBoolean(7, u.isActif());
            stmt.executeUpdate();
        }
    }

    @Override
    public Utilisateur read(int id) throws Exception {
        String sql = "SELECT * FROM utilisateurs WHERE idUtilisateur=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRowToUtilisateur(rs);
        }
        return null;
    }

    @Override
    public List<Utilisateur> readAll() throws Exception {
        List<Utilisateur> users = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(mapRowToUtilisateur(rs));
            }
        }
        return users;
    }

    @Override
    public void update(Utilisateur u) throws Exception {
        String sql = "UPDATE utilisateurs SET nom=?, prenom=?, email=?, motDePasse=?, role=?, actif=? WHERE idUtilisateur=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, u.getNom());
            stmt.setString(2, u.getPrenom());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getMotDePasse());
            stmt.setString(5, u.getRole());
            stmt.setBoolean(6, u.isActif());
            stmt.setInt(7, u.getIdUtilisateur());
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
            if (rs.next()) return mapRowToUtilisateur(rs);
        }
        return null;
    }

    private Utilisateur mapRowToUtilisateur(ResultSet rs) throws Exception {
        String role = rs.getString("role");
        String nom = rs.getString("nom");
        String prenom = rs.getString("prenom");
        String email = rs.getString("email");
        String mdp = rs.getString("motDePasse");

        Utilisateur u = UserFactory.createUser(role, nom, prenom, email, mdp);
        u.setIdUtilisateur(rs.getInt("idUtilisateur"));
        java.sql.Date sqlDate = rs.getDate("dateCreation");
        if (sqlDate != null) u.setDateCreation(sqlDate.toLocalDate());
        u.setActif(rs.getBoolean("actif"));

        return u;
    }
}
