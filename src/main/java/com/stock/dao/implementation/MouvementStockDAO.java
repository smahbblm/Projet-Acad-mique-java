package com.stock.dao.implementation;

import com.stock.dao.interfaces.IMouvementStockDAO;
import com.stock.model.stock.MouvementStock;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MouvementStockDAO implements IMouvementStockDAO {
    private Connection connection;

    public MouvementStockDAO() throws Exception {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(MouvementStock mouvementStock) throws Exception {}

    @Override
    public MouvementStock read(int id) throws Exception {
        return null;
    }

    @Override
    public List<MouvementStock> readAll() throws Exception {
        List<MouvementStock> list_mouvements=new ArrayList<>();
        String sql = "SELECT m.*, " +
                "p.idProduit, p.nom AS produitNom, p.prix AS produitPrix, " +
                "u.idUtilisateur, u.nom AS utilisateurNom, u.email AS utilisateurEmail " +
                "FROM mouvement_stock m " +
                "JOIN produit p ON m.produit_id = p.idProduit " +
                "JOIN utilisateur u ON m.utilisateur_id = u.idUtilisateur";

        try(PreparedStatement  stmt=Connection.prepareStatement(sql);
            ResultSet rs =stmt.executeQuery()){
            while(rs.next()) {
                MouvementStock mv=new MouvementStock(
                       rs.getInt("idMouvement"),
                       rs.getDate("dateMouvement"),
                       rs.getString("typeMouvements"),
                       rs.getInt("quantite"),
                        rs.getInt("stockAvant"),
                        rs.getInt("stockApres"),
                      rs.getString("reference"),
                      rs.getObject("produit"),
                      rs.getObject("utilisateur"););

                list_mouvements.add(mv;

            }

        }catch(SQLException e){
            e.printStackTrace();
        }

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

