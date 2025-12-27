package com.stock.dao.implementation;

import com.stock.dao.interfaces.IClientDAO;
import com.stock.model.partenaire.Client;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO implements IClientDAO {
      private Connection connection;
      public ClientDAO() throws Exception {
          this.connection = DatabaseConnection.getConnection();
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
        String sql = "SELECT * FROM clients ";
       try (PreparedStatement stmt = connection.prepareStatement(sql)) {
           try (var rs = stmt.executeQuery()) {
               if (rs.next()) {return mapResultSetToClient(rs);
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
               ResultSet rs = stmt.executeQuery()) {
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
        // Supprimer d'abord les factures liées
        String deleteFact = "DELETE FROM factures WHERE idClient=?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteFact)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        
        // Supprimer les bons de livraison liés
        String deleteBL = "DELETE FROM bon_livraison WHERE idClient=?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteBL)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        
        // Supprimer le client
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
        try {
            Client client = new Client(
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("raisonSociale"),
                rs.getString("adresse"),
                rs.getString("telephone"),
                rs.getString("email")
            );
            client.setIdClient(rs.getInt("idClient"));
            
            // Gestion sécurisée de la date
            try {
                if (rs.getDate("dateInscription") != null) {
                    client.setDateInscription(rs.getDate("dateInscription").toLocalDate());
                }
            } catch (Exception e) {
                System.out.println("Erreur date pour client " + rs.getString("nom") + ": " + e.getMessage());
                // Date par défaut
                client.setDateInscription(java.time.LocalDate.now());
            }
            
            return client;
        } catch (Exception e) {
            System.out.println("ERREUR lors de la création du client: " + e.getMessage());
            throw e;
        }
    }
}
