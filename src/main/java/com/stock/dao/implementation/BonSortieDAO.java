package com.stock.dao.implementation;

import com.stock.dao.interfaces.IBonSortieDAO;
import com.stock.model.document.BonSortie;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class BonSortieDAO implements IBonSortieDAO {
    private Connection connection;

    public BonSortieDAO() throws Exception {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(BonSortie bonSortie) throws Exception {}

    @Override
    public BonSortie read(int id) throws Exception {
        return null;
    }

    @Override
    public List<BonSortie> readAll() throws Exception {
        return new ArrayList<>();
    }

    @Override
    public void update(BonSortie bonSortie) throws Exception {}

    @Override
    public void delete(int id) throws Exception {}

    @Override
    public BonSortie findByNumero(String numero) throws Exception {
        return null;
    }

    @Override
    public List<BonSortie> findByStatut(String statut) throws Exception {
        return new ArrayList<>();
    }
}

