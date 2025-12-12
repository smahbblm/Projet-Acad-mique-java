  /*  apres les conflits */
package com.stock.dao.implementation;

import com.stock.dao.interfaces.IBonSortieDAO;
import com.stock.model.document.BonSortie;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BonSortieDAO implements IBonSortieDAO {
    private Connection connection;

    public BonSortieDAO() throws Exception {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(BonSortie bonSortie) throws Exception {
        String sql = "INSERT INTO bon_sortie (numero, dateSortie, statut) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, bonSortie.getNumero());
            stmt.setObject(2, bonSortie.getDateSortie()); // LocalDate fonctionne si tu utilises setObject
            stmt.setString(3, bonSortie.getStatut());
            stmt.executeUpdate();

            // récupérer l'id généré automatiquement
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                bonSortie.setIdBonSortie(rs.getInt(1));
            }
        }
    }


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

/*  avant les conflits*/
//package com.stock.dao.implementation;
//
//import com.stock.dao.interfaces.IBonSortieDAO;
//import com.stock.model.document.BonSortie;
//import com.stock.util.DatabaseConnection;
//import java.sql.Connection;
//import java.util.ArrayList;
//import java.util.List;
//
//public class BonSortieDAO implements IBonSortieDAO {
//    private Connection connection;
//
//    public BonSortieDAO() throws Exception {
//        this.connection = DatabaseConnection.getInstance().getConnection();
//    }
//
//    @Override
//    public void create(BonSortie bonSortie) throws Exception {}
//
//    @Override
//    public BonSortie read(int id) throws Exception {
//        return null;
//    }
//
//    @Override
//    public List<BonSortie> readAll() throws Exception {
//        return new ArrayList<>();
//    }
//
//    @Override
//    public void update(BonSortie bonSortie) throws Exception {}
//
//    @Override
//    public void delete(int id) throws Exception {}
//
//    @Override
//    public BonSortie findByNumero(String numero) throws Exception {
//        return null;
//    }
//
//    @Override
//    public List<BonSortie> findByStatut(String statut) throws Exception {
//        return new ArrayList<>();
//    }
//}
//

