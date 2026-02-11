package com.stock.dao.implementation;

import com.stock.dao.interfaces.ILigneInventaireDAO;
import com.stock.model.stock.LigneInventaire;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class LigneInventaireDAO implements ILigneInventaireDAO {
    private Connection connection;

    public LigneInventaireDAO() throws Exception {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(LigneInventaire ligneInventaire) throws Exception {}

    @Override
    public LigneInventaire read(int id) throws Exception {
        return null;
    }

    @Override
    public List<LigneInventaire> readAll() throws Exception {
        return new ArrayList<>();
    }

    @Override
    public void update(LigneInventaire ligneInventaire) throws Exception {}

    @Override
    public void delete(int id) throws Exception {}

    @Override
    public List<LigneInventaire> findByInventaire(int idInventaire) throws Exception {
        return new ArrayList<>();
    }
}

