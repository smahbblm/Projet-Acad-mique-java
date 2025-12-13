package com.stock.dao.implementation;

import com.stock.dao.interfaces.IBonCommandeDAO;
import com.stock.model.document.BonCommande;
import com.stock.model.partenaire.Fournisseur;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation DAO pour les bons de commande
 */
public class BonCommandeDAO implements IBonCommandeDAO {
    private Connection connection;
    private LigneCommandeDAO ligneCommandeDAO;

    public BonCommandeDAO() throws Exception {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.ligneCommandeDAO=new LigneCommandeDAO();
    }

    @Override
    public void create(BonCommande bonCommande) throws Exception {
        String sql = "INSERT INTO bon_commande (numero, dateCommande, dateLivraisonPrevue, statut, montantTotal, observations, idFournisseur) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bonCommande.getNumero());
            stmt.setObject(2, bonCommande.getDateCommande());
            stmt.setObject(3, bonCommande.getDateLivraisonPrevue());
            stmt.setString(4, bonCommande.getStatut());
            stmt.setFloat(5, bonCommande.getMontantTotal());
            stmt.setString(6, bonCommande.getObservations());
            stmt.setInt(7, bonCommande.getFournisseur().getIdFournisseur());
            stmt.executeUpdate();
        }
    }

    @Override
    public BonCommande read(int id) throws Exception {
        String sql = "SELECT bc.*, f.raisonSociale, f.adresse, f.telephone, f.email " +
                    "FROM bon_commande bc " +
                    "JOIN fournisseurs f ON bc.idFournisseur = f.idFournisseur " +
                    "WHERE bc.idBonCommande=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBonCommande(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<BonCommande> readAll() throws Exception {
        List<BonCommande> bonsCommande = new ArrayList<>();
        String sql = "SELECT bc.*, f.raisonSociale, f.adresse, f.telephone, f.email " +
                    "FROM bon_commande bc " +
                    "JOIN fournisseurs f ON bc.idFournisseur = f.idFournisseur " +
                    "ORDER BY bc.dateCommande DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                bonsCommande.add(mapResultSetToBonCommande(rs));
            }
        }
        return bonsCommande;
    }
    // Méthode privée pour mapper le ResultSet en objet BonCommande
    private BonCommande mapResultSetToBonCommande(ResultSet rs) throws Exception {
        BonCommande bc = new BonCommande();
        bc.setIdBonCommande(rs.getInt("idBonCommande"));
        bc.setNumero(rs.getString("numero"));
        bc.setDateCommande(rs.getDate("dateCommande").toLocalDate());
        bc.setDateLivraisonPrevue(rs.getDate("dateLivraisonPrevue") != null
                ? rs.getDate("dateLivraisonPrevue").toLocalDate()
                : null);
        bc.setStatut(rs.getString("statut"));
        bc.setMontantTotal(rs.getFloat("montantTotal"));
        bc.setObservations(rs.getString("observations"));

        // Créer le fournisseur
        Fournisseur f = new Fournisseur();
        f.setRaisonSociale(rs.getString("raisonSociale"));
        f.setAdresse(rs.getString("adresse"));
        f.setTelephone(rs.getString("telephone"));
        f.setEmail(rs.getString("email"));
        bc.setFournisseur(f);
        bc.setLignes(ligneCommandeDAO.findByBonCommande(bc.getIdBonCommande()));
        // Optionnel : charger les lignes si nécessaire
        // bc.setLignes(ligneCommandeDAO.findByBonCommande(bc.getIdBonCommande()));

        return bc;
    }

    @Override
    public void update(BonCommande bonCommande) throws Exception {
        String sql = "UPDATE bon_commande SET numero=?, dateLivraisonPrevue=?, statut=?, montantTotal=?, observations=? " +
                     "WHERE idBonCommande=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bonCommande.getNumero());
            stmt.setObject(2, bonCommande.getDateLivraisonPrevue());
            stmt.setString(3, bonCommande.getStatut());
            stmt.setFloat(4, bonCommande.getMontantTotal());
            stmt.setString(5, bonCommande.getObservations());
            stmt.setInt(6, bonCommande.getIdBonCommande());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM bon_commande WHERE idBonCommande=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public BonCommande findByNumero(String numero) throws Exception {
        String sql = "SELECT bc.*, f.raisonSociale, f.adresse, f.telephone, f.email " +
                    "FROM bon_commande bc " +
                    "JOIN fournisseurs f ON bc.idFournisseur = f.idFournisseur " +
                    "WHERE bc.numero=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numero);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBonCommande(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<BonCommande> findByStatut(String statut) throws Exception {
        List<BonCommande> bonsCommande = new ArrayList<>();
        String sql = "SELECT bc.*, f.raisonSociale, f.adresse, f.telephone, f.email " +
                    "FROM bon_commande bc " +
                    "JOIN fournisseurs f ON bc.idFournisseur = f.idFournisseur " +
                    "WHERE bc.statut=? ORDER BY bc.dateCommande DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, statut);
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bonsCommande.add(mapResultSetToBonCommande(rs));
                }
            }
        }
        return bonsCommande;
    }

    @Override
    public List<BonCommande> findByFournisseur(int idFournisseur) throws Exception {
        List<BonCommande> bonsCommande = new ArrayList<>();
        String sql = "SELECT bc.*, f.raisonSociale, f.adresse, f.telephone, f.email " +
                    "FROM bon_commande bc " +
                    "JOIN fournisseurs f ON bc.idFournisseur = f.idFournisseur " +
                    "WHERE bc.idFournisseur=? ORDER BY bc.dateCommande DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFournisseur);
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bonsCommande.add(mapResultSetToBonCommande(rs));
                }
            }
        }
        return bonsCommande;
    }
    public void updateStatut(BonCommande bc) throws Exception {
        String sql = "UPDATE bon_commande SET statut = ? WHERE idBonCommande = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bc.getStatut());
            stmt.setInt(2, bc.getIdBonCommande());
            stmt.executeUpdate();
        }
    }


}
