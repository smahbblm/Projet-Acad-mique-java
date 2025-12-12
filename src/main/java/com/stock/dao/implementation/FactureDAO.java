package com.stock.dao.implementation;

import com.stock.dao.interfaces.IFactureDAO;
import com.stock.model.document.Facture;
import com.stock.model.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation DAO pour les factures
 */
public class FactureDAO implements IFactureDAO {
    private Connection connection;

    public FactureDAO() throws Exception {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void create(Facture facture) throws Exception {
        String sql = "INSERT INTO factures (numero, dateFacture, dateEcheance, montantHT, montantTVA, montantTTC, statut, idClient) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, facture.getNumero());
            stmt.setObject(2, facture.getDateFacture());
            stmt.setObject(3, facture.getDateEcheance());
            stmt.setFloat(4, facture.getMontantHT());
            stmt.setFloat(5, facture.getMontantTVA());
            stmt.setFloat(6, facture.getMontantTTC());
            stmt.setString(7, facture.getStatut());
            stmt.setInt(8, facture.getClient().getIdClient());
            stmt.executeUpdate();
        }
    }

    @Override
    public Facture read(int id) throws Exception {
        String sql = "SELECT f.*, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
                    "FROM factures f " +
                    "JOIN clients c ON f.idClient = c.idClient " +
                    "WHERE f.idFacture=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFacture(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Facture> readAll() throws Exception {
        List<Facture> factures = new ArrayList<>();
        String sql = "SELECT f.*, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
                    "FROM factures f " +
                    "JOIN clients c ON f.idClient = c.idClient " +
                    "ORDER BY f.dateFacture DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                factures.add(mapResultSetToFacture(rs));
            }
        }
        return factures;
    }

    @Override
    public void update(Facture facture) throws Exception {
        String sql = "UPDATE factures SET numero=?, dateEcheance=?, montantHT=?, montantTVA=?, montantTTC=?, statut=? " +
                     "WHERE idFacture=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, facture.getNumero());
            stmt.setObject(2, facture.getDateEcheance());
            stmt.setFloat(3, facture.getMontantHT());
            stmt.setFloat(4, facture.getMontantTVA());
            stmt.setFloat(5, facture.getMontantTTC());
            stmt.setString(6, facture.getStatut());
            stmt.setInt(7, facture.getIdFacture());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM factures WHERE idFacture=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Facture findByNumero(String numero) throws Exception {
        String sql = "SELECT f.*, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
                    "FROM factures f " +
                    "JOIN clients c ON f.idClient = c.idClient " +
                    "WHERE f.numero=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numero);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFacture(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Facture> findByStatut(String statut) throws Exception {
        List<Facture> factures = new ArrayList<>();
        String sql = "SELECT f.*, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
                    "FROM factures f " +
                    "JOIN clients c ON f.idClient = c.idClient " +
                    "WHERE f.statut=? ORDER BY f.dateFacture DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, statut);
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    factures.add(mapResultSetToFacture(rs));
                }
            }
        }
        return factures;
    }

    @Override
    public List<Facture> findByClient(int idClient) throws Exception {
        List<Facture> factures = new ArrayList<>();
        String sql = "SELECT f.*, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
                    "FROM factures f " +
                    "JOIN clients c ON f.idClient = c.idClient " +
                    "WHERE f.idClient=? ORDER BY f.dateFacture DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    factures.add(mapResultSetToFacture(rs));
                }
            }
        }
        return factures;
    }

    private Facture mapResultSetToFacture(java.sql.ResultSet rs) throws Exception {
        // Créer le client
        com.stock.model.partenaire.Client client = new com.stock.model.partenaire.Client(
            rs.getString("nom"),
            rs.getString("prenom"),
            rs.getString("raisonSociale"),
            rs.getString("adresse"),
            rs.getString("telephone"),
            rs.getString("email")
        );
        client.setIdClient(rs.getInt("idClient"));

        // Créer la facture
        Facture facture = new Facture(
            rs.getString("numero"),
            rs.getObject("dateEcheance", java.time.LocalDate.class),
            client
        );

        facture.setIdFacture(rs.getInt("idFacture"));
        facture.setDateFacture(rs.getObject("dateFacture", java.time.LocalDate.class));
        facture.setMontantHT(rs.getFloat("montantHT"));
        facture.setMontantTVA(rs.getFloat("montantTVA"));
        facture.setMontantTTC(rs.getFloat("montantTTC"));
        facture.setStatut(rs.getString("statut"));

        return facture;
    }
}
