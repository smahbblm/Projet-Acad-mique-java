package com.stock.dao.implementation;
//
import com.stock.dao.interfaces.ILigneLivraisonDAO;
import com.stock.model.document.LigneLivraison;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class LigneLivraisonDAO implements ILigneLivraisonDAO {
    private Connection connection;

    public LigneLivraisonDAO() throws Exception {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void create(LigneLivraison ligneLivraison) throws Exception {}

   @Override
   public LigneLivraison read(int id) throws Exception {
        return null;
    }

    @Override
    public List<LigneLivraison> readAll() throws Exception {
        return new ArrayList<>();
    }

    @Override
    public void update(LigneLivraison ligneLivraison) throws Exception {}

    @Override
    public void delete(int id) throws Exception {}

    @Override
    public List<LigneLivraison> findByBonLivraison(int idBonLivraison) throws Exception {
        return new ArrayList<>();
    }
}

