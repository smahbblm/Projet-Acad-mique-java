package com.stock.dao.implementation;

import com.stock.dao.interfaces.ILigneCommandeDAO;
import com.stock.model.document.LigneCommande;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class LigneCommandeDAO implements ILigneCommandeDAO {
    private Connection connection;

    public LigneCommandeDAO() throws Exception {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(LigneCommande ligneCommande) throws Exception {}

    @Override
    public LigneCommande read(int id) throws Exception {
        return null;
    }

    @Override
    public List<LigneCommande> readAll() throws Exception {
        return new ArrayList<>();
    }

    @Override
    public void update(LigneCommande ligneCommande) throws Exception {}

    @Override
    public void delete(int id) throws Exception {}

    @Override
    public List<LigneCommande> findByBonCommande(int idBonCommande) throws Exception {
        return new ArrayList<>();
    }
}

