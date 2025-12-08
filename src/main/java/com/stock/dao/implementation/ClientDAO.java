package com.stock.dao.implementation;

import com.stock.dao.interfaces.IClientDAO;
import com.stock.model.partenaire.Client;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation DAO pour les clients
 */
public class ClientDAO implements IClientDAO {
    private Connection connection;

    public ClientDAO() throws Exception {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(Client client) throws Exception {
        String sql = "INSERT INTO clients (nom, prenom, raisonSociale, adresse, telephone, email, dateInscription) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getRaisonSociale());
            stmt.setString(4, client.getAdresse());
            stmt.setString(5, client.getTelephone());
            stmt.setString(6, client.getEmail());
            stmt.setObject(7, client.getDateInscription());
            stmt.executeUpdate();
        }
    }

    @Override
    public Client read(int id) throws Exception {
        String sql = "SELECT * FROM clients WHERE idClient=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToClient(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Client> readAll() throws Exception {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients ORDER BY nom, prenom";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        }
        return clients;
    }

    @Override
    public void update(Client client) throws Exception {
        String sql = "UPDATE clients SET nom=?, prenom=?, raisonSociale=?, adresse=?, telephone=?, email=? " +
                     "WHERE idClient=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getRaisonSociale());
            stmt.setString(4, client.getAdresse());
            stmt.setString(5, client.getTelephone());
            stmt.setString(6, client.getEmail());
            stmt.setInt(7, client.getIdClient());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM clients WHERE idClient=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Client findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM clients WHERE email=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToClient(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Client> findByNom(String nom) throws Exception {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE nom LIKE ? OR prenom LIKE ? OR raisonSociale LIKE ? ORDER BY nom, prenom";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + nom + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clients.add(mapResultSetToClient(rs));
                }
            }
        }
        return clients;
    }

    private Client mapResultSetToClient(java.sql.ResultSet rs) throws Exception {
        Client client = new Client(
            rs.getString("nom"),
            rs.getString("prenom"),
            rs.getString("raisonSociale"),
            rs.getString("adresse"),
            rs.getString("telephone"),
            rs.getString("email")
        );
        client.setIdClient(rs.getInt("idClient"));
        client.setDateInscription(rs.getObject("dateInscription", java.time.LocalDate.class));
        return client;
    }
}
