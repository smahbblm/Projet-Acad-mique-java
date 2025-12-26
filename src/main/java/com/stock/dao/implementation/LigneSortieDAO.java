package com.stock.dao.implementation;

import com.stock.dao.interfaces.ILigneSortieDAO;
import com.stock.model.document.LigneSortie;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LigneSortieDAO implements ILigneSortieDAO {
    private Connection connection;

    public LigneSortieDAO() throws Exception {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(LigneSortie ligneSortie) throws Exception {
        String sql = "INSERT INTO ligne_sortie (idBonSortie, idProduit, quantite) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, ligneSortie.getBonSortie().getIdBonSortie());
            stmt.setInt(2, ligneSortie.getProduit().getIdProduit());
            stmt.setInt(3, ligneSortie.getQuantite());
            stmt.executeUpdate();

            // récupérer l'id généré automatiquement
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                ligneSortie.setIdLigne(rs.getInt(1));
            }
        }
    }


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

