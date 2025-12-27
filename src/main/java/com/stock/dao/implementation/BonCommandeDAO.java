
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

        this.connection = DatabaseConnection.getConnection();
        //this.connection = DatabaseConnection.getConnection();
        this.ligneCommandeDAO=new LigneCommandeDAO();

    }

    @Override
    public void create(BonCommande bonCommande) throws Exception {
        String sql = "INSERT INTO bon_commande (numero, dateCommande, dateLivraisonPrevue, statut, montantTotal, observations, idFournisseur) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, bonCommande.getNumero());
            stmt.setObject(2, bonCommande.getDateCommande());
            stmt.setObject(3, bonCommande.getDateLivraisonPrevue());
            stmt.setString(4, bonCommande.getStatut());
            stmt.setFloat(5, bonCommande.getMontantTotal());
            stmt.setString(6, bonCommande.getObservations());
            stmt.setInt(7, bonCommande.getFournisseur().getIdFournisseur());
            stmt.executeUpdate();
            
            try (var rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idBonCommande = rs.getInt(1);
                    bonCommande.setIdBonCommande(idBonCommande);
                    
                    if (bonCommande.getLignes() != null && !bonCommande.getLignes().isEmpty()) {
                        String sqlLigne = "INSERT INTO ligne_commande (idBonCommande, idProduit, quantite, prixUnitaire, sousTotal) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement stmtLigne = connection.prepareStatement(sqlLigne)) {
                            for (var ligne : bonCommande.getLignes()) {
                                stmtLigne.setInt(1, idBonCommande);
                                stmtLigne.setInt(2, ligne.getProduit().getIdProduit());
                                stmtLigne.setInt(3, ligne.getQuantite());
                                stmtLigne.setFloat(4, ligne.getPrixUnitaire());
                                stmtLigne.setFloat(5, ligne.getSousTotal());
                                stmtLigne.executeUpdate();
                            }
                        }
                    }
                }
            }
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
        
        String sqlDeleteLignes = "DELETE FROM ligne_commande WHERE idBonCommande=?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlDeleteLignes)) {
            stmt.setInt(1, bonCommande.getIdBonCommande());
            stmt.executeUpdate();
        }
        
        if (bonCommande.getLignes() != null && !bonCommande.getLignes().isEmpty()) {
            String sqlLigne = "INSERT INTO ligne_commande (idBonCommande, idProduit, quantite, prixUnitaire, sousTotal) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmtLigne = connection.prepareStatement(sqlLigne)) {
                for (var ligne : bonCommande.getLignes()) {
                    stmtLigne.setInt(1, bonCommande.getIdBonCommande());
                    stmtLigne.setInt(2, ligne.getProduit().getIdProduit());
                    stmtLigne.setInt(3, ligne.getQuantite());
                    stmtLigne.setFloat(4, ligne.getPrixUnitaire());
                    stmtLigne.setFloat(5, ligne.getSousTotal());
                    stmtLigne.executeUpdate();
                }
            }
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sqlLignes = "DELETE FROM ligne_commande WHERE idBonCommande=?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlLignes)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        
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



    private BonCommande mapResultSetToBonCommande(java.sql.ResultSet rs) throws Exception {
        com.stock.model.partenaire.Fournisseur fournisseur = new com.stock.model.partenaire.Fournisseur(
            rs.getString("raisonSociale"),
            rs.getString("adresse"),
            rs.getString("telephone"),
            rs.getString("email")
        );
        fournisseur.setIdFournisseur(rs.getInt("idFournisseur"));

        BonCommande bonCommande = new BonCommande(
            rs.getString("numero"),
            rs.getObject("dateLivraisonPrevue", java.time.LocalDate.class),
            fournisseur
        );

        bonCommande.setIdBonCommande(rs.getInt("idBonCommande"));
        bonCommande.setDateCommande(rs.getObject("dateCommande", java.time.LocalDate.class));
        bonCommande.setStatut(rs.getString("statut"));
        bonCommande.setMontantTotal(rs.getFloat("montantTotal"));
        bonCommande.setObservations(rs.getString("observations"));
        
        bonCommande.setLignes(loadLignesCommande(bonCommande.getIdBonCommande()));

        return bonCommande;
    }
    
    private List<com.stock.model.document.LigneCommande> loadLignesCommande(int idBonCommande) throws Exception {
        List<com.stock.model.document.LigneCommande> lignes = new ArrayList<>();
        String sql = "SELECT lc.*, p.reference, p.designation, p.prixAchat, p.prixVente, p.quantiteStock, p.seuilMin, p.seuilMax, p.categorie " +
                    "FROM ligne_commande lc " +
                    "JOIN produits p ON lc.idProduit = p.idProduit " +
                    "WHERE lc.idBonCommande=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idBonCommande);
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    com.stock.model.produit.Produit produit = new com.stock.model.produit.Produit(
                        rs.getString("reference"),
                        rs.getString("designation"),
                        rs.getFloat("prixAchat"),
                        rs.getFloat("prixVente"),
                        rs.getInt("quantiteStock"),
                        rs.getInt("seuilMin"),
                        rs.getInt("seuilMax"),
                        rs.getString("categorie")
                    );
                    produit.setIdProduit(rs.getInt("idProduit"));
                    
                    com.stock.model.document.LigneCommande ligne = new com.stock.model.document.LigneCommande(
                        produit,
                        rs.getInt("quantite"),
                        rs.getFloat("prixUnitaire")
                    );
                    lignes.add(ligne);
                }
            }
        }
        return lignes;
    }

}
