package com.stock.dao.implementation;

import com.stock.dao.interfaces.IProduitDAO;
import com.stock.model.produit.Produit;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation DAO pour les produits
 */
public class ProduitDAO implements IProduitDAO {
    private Connection connection;

    public ProduitDAO() throws Exception {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(Produit produit) throws Exception {
        String sql = "INSERT INTO produits (reference, designation, description, prixAchat, prixVente, " +
                     "quantiteStock, seuilMin, seuilMax, categorie, dateAjout) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produit.getReference());
            stmt.setString(2, produit.getDesignation());
            stmt.setString(3, produit.getDescription());
            stmt.setFloat(4, produit.getPrixAchat());
            stmt.setFloat(5, produit.getPrixVente());
            stmt.setInt(6, produit.getQuantiteStock());
            stmt.setInt(7, produit.getSeuilMin());
            stmt.setInt(8, produit.getSeuilMax());
            stmt.setString(9, produit.getCategorie());
            stmt.setObject(10, produit.getDateAjout());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produit.setIdProduit(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public Produit read(int id) throws Exception {
        String sql = "SELECT * FROM produits WHERE idProduit=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduit(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Produit> readAll() throws Exception {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produits ORDER BY designation";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produit p = new Produit(
                    rs.getString("reference"),
                    rs.getString("designation"),
                    rs.getFloat("prixAchat"),
                    rs.getFloat("prixVente"),
                    rs.getInt("quantiteStock"),
                    rs.getInt("seuilMin"),
                    rs.getInt("seuilMax"),
                    rs.getString("categorie")
                );
                p.setIdProduit(rs.getInt("idProduit"));
                p.setDescription(rs.getString("description"));
                if (rs.getDate("dateAjout") != null) {
                    p.setDateAjout(rs.getDate("dateAjout").toLocalDate());
                }
                produits.add(p);
            }
        }
        return produits;
    }

    @Override
    public void update(Produit produit) throws Exception {
        String sql = "UPDATE produits SET reference=?, designation=?, description=?, prixAchat=?, " +
                     "prixVente=?, quantiteStock=?, seuilMin=?, seuilMax=?, categorie=? WHERE idProduit=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produit.getReference());
            stmt.setString(2, produit.getDesignation());
            stmt.setString(3, produit.getDescription());
            stmt.setFloat(4, produit.getPrixAchat());
            stmt.setFloat(5, produit.getPrixVente());
            stmt.setInt(6, produit.getQuantiteStock());
            stmt.setInt(7, produit.getSeuilMin());
            stmt.setInt(8, produit.getSeuilMax());
            stmt.setString(9, produit.getCategorie());
            stmt.setInt(10, produit.getIdProduit());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sqlMouvement = "DELETE FROM mouvement_stock WHERE idProduit=?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlMouvement)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        
        String sql = "DELETE FROM produits WHERE idProduit=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Produit findByReference(String reference) throws Exception {
        String sql = "SELECT * FROM produits WHERE reference=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, reference);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduit(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Produit> findByCategorie(String categorie) throws Exception {
        return new ArrayList<>();
    }

    @Override
    public List<Produit> findStockBas() throws Exception {
        return new ArrayList<>();
    }

    @Override
    public List<String> getCategories() throws Exception {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT categorie FROM produits WHERE categorie IS NOT NULL AND categorie != '' ORDER BY categorie";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("categorie"));
            }
        }
        return categories;
    }

    private Produit mapResultSetToProduit(ResultSet rs) throws Exception {
        Produit p = new Produit(
            rs.getString("reference"),
            rs.getString("designation"),
            rs.getFloat("prixAchat"),
            rs.getFloat("prixVente"),
            rs.getInt("quantiteStock"),
            rs.getInt("seuilMin"),
            rs.getInt("seuilMax"),
            rs.getString("categorie")
        );
        p.setIdProduit(rs.getInt("idProduit"));
        p.setDescription(rs.getString("description"));
        if (rs.getDate("dateAjout") != null) {
            p.setDateAjout(rs.getDate("dateAjout").toLocalDate());
        }
        return p;
    }
    public void mettreAJourStock(Produit p) throws Exception {
        String sql = "UPDATE produits SET quantiteStock = ? WHERE idProduit = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, p.getQuantiteStock());
            stmt.setInt(2, p.getIdProduit());
            stmt.executeUpdate();
        }
    }

    // Méthode pour récupérer un produit par id
    public Produit getProduitById(int idProduit) throws Exception {
        String sql = "SELECT * FROM produits WHERE idProduit = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idProduit);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produit p = new Produit();
                    p.setIdProduit(rs.getInt("idProduit"));
                    p.setReference(rs.getString("reference"));
                    p.setDesignation(rs.getString("designation"));
                    p.setDescription(rs.getString("description"));
                    p.setPrixAchat(rs.getFloat("prixAchat"));
                    p.setPrixVente(rs.getFloat("prixVente"));
                    p.setQuantiteStock(rs.getInt("quantiteStock"));
                    p.setSeuilMin(rs.getInt("seuilMin"));
                    p.setSeuilMax(rs.getInt("seuilMax"));
                    p.setCategorie(rs.getString("categorie"));
                    p.setDateAjout(rs.getDate("dateAjout").toLocalDate());
                    return p;
                }
            }
        }
        return null;
    }
}
