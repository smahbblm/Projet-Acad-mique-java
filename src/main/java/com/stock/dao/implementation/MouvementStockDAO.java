package com.stock.dao.implementation;

import com.stock.dao.interfaces.IMouvementStockDAO;
import com.stock.model.stock.MouvementStock;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class MouvementStockDAO implements IMouvementStockDAO {
    private Connection connection;

    public MouvementStockDAO() throws Exception {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(MouvementStock mouvementStock) throws Exception {
        String sql = "INSERT INTO mouvement_stock (dateMouvement, typeMouvement, quantite, stockAvant, stockApres, reference, idProduit, idUtilisateur) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, mouvementStock.getDateMouvement());
            stmt.setString(2, mouvementStock.getTypeMouvement());
            stmt.setInt(3, mouvementStock.getQuantite());
            stmt.setInt(4, mouvementStock.getStockAvant());
            stmt.setInt(5, mouvementStock.getStockApres());
            stmt.setString(6, mouvementStock.getReference());
            stmt.setInt(7, mouvementStock.getProduit().getIdProduit());
            stmt.setObject(8, mouvementStock.getUtilisateur() != null ? mouvementStock.getUtilisateur().getIdUtilisateur() : null);
            stmt.executeUpdate();
        }
    }

    @Override
    public MouvementStock read(int id) throws Exception {
        return null;
    }

    @Override
    public List<MouvementStock> readAll() throws Exception {
        List<MouvementStock> mouvements = new ArrayList<>();
        String sql = "SELECT ms.*, p.reference, p.designation, u.nom " +
                    "FROM mouvement_stock ms " +
                    "JOIN produits p ON ms.idProduit = p.idProduit " +
                    "LEFT JOIN utilisateurs u ON ms.idUtilisateur = u.idUtilisateur " +
                    "ORDER BY ms.dateMouvement DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                MouvementStock mouvement = new MouvementStock(
                    rs.getString("typeMouvement"),
                    rs.getInt("quantite"),
                    rs.getString("reference")
                );
                mouvement.setIdMouvement(rs.getInt("idMouvement"));
                mouvement.setDateMouvement(rs.getObject("dateMouvement", java.time.LocalDate.class));
                mouvement.setStockAvant(rs.getInt("stockAvant"));
                mouvement.setStockApres(rs.getInt("stockApres"));
                
                com.stock.model.produit.Produit produit = new com.stock.model.produit.Produit(
                    rs.getString("reference"),
                    rs.getString("designation"),
                    0, 0, 0, 0, 0, ""
                );
                produit.setIdProduit(rs.getInt("idProduit"));
                mouvement.setProduit(produit);
                
                mouvements.add(mouvement);
            }
        }
        return mouvements;
    }

    @Override
    public void update(MouvementStock mouvementStock) throws Exception {}

    @Override
    public void delete(int id) throws Exception {}

    @Override
    public List<MouvementStock> findByProduit(int idProduit) throws Exception {
        return new ArrayList<>();
    }

    @Override
    public List<MouvementStock> findByType(String type) throws Exception {
        return new ArrayList<>();
    }
}

