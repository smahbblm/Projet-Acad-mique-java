package com.stock.dao.implementation;

import com.stock.dao.interfaces.IMouvementStockDAO;
import com.stock.model.stock.MouvementStock;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
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
        return new ArrayList<>();
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

