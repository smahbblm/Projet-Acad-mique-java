package com.stock.dao.implementation;

import com.stock.dao.interfaces.IInventaireDAO;
import com.stock.model.stock.Inventaire;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class InventaireDAO implements IInventaireDAO {
    private Connection connection;

    public InventaireDAO() throws Exception {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(Inventaire inventaire) throws Exception {}

    @Override
    public Inventaire read(int id) throws Exception {
        return null;
    }

    @Override
    public List<Inventaire> readAll() throws Exception {
        return new ArrayList<>();
    }

    @Override
    public void update(Inventaire inventaire) throws Exception {}

    @Override
    public void delete(int id) throws Exception {}

    @Override
    public List<Inventaire> findByStatut(String statut) throws Exception {
        return new ArrayList<>();
    }
}

