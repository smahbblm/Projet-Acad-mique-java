package com.stock.dao.implementation;

import com.stock.dao.interfaces.IBonRetourDAO;
import com.stock.model.document.BonRetour;
import com.stock.model.partenaire.Client;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BonRetourDAO implements IBonRetourDAO {
    private Connection connection;

    public BonRetourDAO() throws Exception {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void create(BonRetour bonRetour) throws Exception {
        String sql = "INSERT INTO bon_retour (numero, dateRetour, motif, statut, idClient) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bonRetour.getNumero());
            stmt.setObject(2, bonRetour.getDateRetour());
            stmt.setString(3, bonRetour.getMotif());
            stmt.setString(4, bonRetour.getStatut());
            stmt.setInt(5, bonRetour.getClient().getIdClient());
            stmt.executeUpdate();
        }
    }

    @Override
    public BonRetour read(int id) throws Exception {
        String sql = "SELECT br.*, c.nom, c.prenom FROM bon_retour br JOIN clients c ON br.idClient = c.idClient WHERE br.idBonRetour=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBonRetour(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<BonRetour> readAll() throws Exception {
        List<BonRetour> bons = new ArrayList<>();
        String sql = "SELECT br.*, c.nom, c.prenom FROM bon_retour br JOIN clients c ON br.idClient = c.idClient ORDER BY br.dateRetour DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                bons.add(mapResultSetToBonRetour(rs));
            }
        }
        return bons;
    }

    @Override
    public void update(BonRetour bonRetour) throws Exception {
        String sql = "UPDATE bon_retour SET numero=?, dateRetour=?, motif=?, statut=? WHERE idBonRetour=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bonRetour.getNumero());
            stmt.setObject(2, bonRetour.getDateRetour());
            stmt.setString(3, bonRetour.getMotif());
            stmt.setString(4, bonRetour.getStatut());
            stmt.setInt(5, bonRetour.getIdBonRetour());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM bon_retour WHERE idBonRetour=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public BonRetour findByNumero(String numero) throws Exception {
        String sql = "SELECT br.*, c.nom, c.prenom FROM bon_retour br JOIN clients c ON br.idClient = c.idClient WHERE br.numero=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numero);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBonRetour(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<BonRetour> findByStatut(String statut) throws Exception {
        List<BonRetour> bons = new ArrayList<>();
        String sql = "SELECT br.*, c.nom, c.prenom FROM bon_retour br JOIN clients c ON br.idClient = c.idClient WHERE br.statut=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, statut);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bons.add(mapResultSetToBonRetour(rs));
                }
            }
        }
        return bons;
    }

    @Override
    public List<BonRetour> findByClient(int idClient) throws Exception {
        List<BonRetour> bons = new ArrayList<>();
        String sql = "SELECT br.*, c.nom, c.prenom FROM bon_retour br JOIN clients c ON br.idClient = c.idClient WHERE br.idClient=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bons.add(mapResultSetToBonRetour(rs));
                }
            }
        }
        return bons;
    }

    private BonRetour mapResultSetToBonRetour(ResultSet rs) throws Exception {
        Client client = new Client();
        client.setIdClient(rs.getInt("idClient"));
        client.setNom(rs.getString("nom"));
        client.setPrenom(rs.getString("prenom"));

        BonRetour bonRetour = new BonRetour(
            rs.getString("numero"),
            rs.getString("motif"),
            client
        );
        bonRetour.setIdBonRetour(rs.getInt("idBonRetour"));
        bonRetour.setDateRetour(rs.getObject("dateRetour", java.time.LocalDate.class));
        bonRetour.setStatut(rs.getString("statut"));

        return bonRetour;
    }
}
