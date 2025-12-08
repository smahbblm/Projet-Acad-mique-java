package com.stock.dao.implementation;

import com.stock.dao.interfaces.IMouvementStockDAO;
import com.stock.model.produit.Produit;
import com.stock.model.stock.MouvementStock;
import com.stock.model.utilisateur.Magasinier;
import com.stock.model.utilisateur.Utilisateur;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MouvementStockDAO implements IMouvementStockDAO {
    private Connection connection;

    public MouvementStockDAO() throws Exception {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(MouvementStock mouvementStock) throws Exception {

    }

    @Override
    public MouvementStock read(int id) throws Exception {
        return null;
    }

    @Override
    public List<MouvementStock> readAll() throws Exception {
        List<MouvementStock> list_mouvements = new ArrayList<>();

        String sql = "SELECT * FROM mouvement_stock";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                // Constructeur existant
                MouvementStock mv = new MouvementStock(
                        rs.getString("typeMouvements"),
                        rs.getInt("quantite"),
                        rs.getString("reference")
                );

                // Champs supplémentaires
                mv.setIdMouvement(rs.getInt("idMouvement"));
                mv.setDateMouvement(rs.getDate("dateMouvement").toLocalDate());
                mv.setStockAvant(rs.getInt("stockAvant"));
                mv.setStockApres(rs.getInt("stockApres"));

                list_mouvements.add(mv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list_mouvements;
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

