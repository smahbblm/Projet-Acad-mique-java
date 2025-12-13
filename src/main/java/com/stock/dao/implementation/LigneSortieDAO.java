package com.stock.dao.implementation;

import com.stock.dao.interfaces.ILigneSortieDAO;
import com.stock.model.document.LigneSortie;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class LigneSortieDAO implements ILigneSortieDAO {
    private Connection connection;

    public LigneSortieDAO() throws Exception {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(LigneSortie ligneSortie) throws Exception {}

    @Override
    public LigneSortie read(int id) throws Exception {
        return null;
    }

    @Override
    public List<LigneSortie> readAll() throws Exception {
        return new ArrayList<>();
    }

    @Override
    public void update(LigneSortie ligneSortie) throws Exception {}

    @Override
    public void delete(int id) throws Exception {}

    @Override
    public List<LigneSortie> findByBonSortie(int idBonSortie) throws Exception {
        return new ArrayList<>();
    }
}

