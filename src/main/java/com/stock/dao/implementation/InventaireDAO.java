<<<<<<< HEAD
package com.stock.dao.implementation;

import com.mysql.cj.xdevapi.PreparableStatement;
import com.stock.dao.interfaces.IInventaireDAO;
import com.stock.model.stock.Inventaire;
<<<<<<< HEAD
import com.stock.util.DatabaseConnection;

import java.sql.*;
=======
import com.stock.model.DatabaseConnection;
import java.sql.Connection;
>>>>>>> e06bae3 (section de livraison)
import java.util.ArrayList;
import java.util.List;

public class InventaireDAO implements IInventaireDAO {
    private Connection connection;

    public InventaireDAO() throws Exception {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void create(Inventaire inventaire) throws Exception {
        String requete="INSERT INTO inventaires (dateInventaire,statut,observations) VALUES (?,?,?)";
        try(PreparedStatement stmt=connection.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS)){
              stmt.setDate(1,java.sql.Date.valueOf(inventaire.getDateInventaire()));
              stmt.setString(2,inventaire.getStatut());
              stmt.setString(3,inventaire.getObservations());
              stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                inventaire.setIdInventaire(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Inventaire read(int id) throws Exception {
        return null;
    }

    @Override
    public List<Inventaire> readAll() throws Exception {
        List<Inventaire> listInventaires = new ArrayList<>();
        String sql = "SELECT * FROM inventaires";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Inventaire inv = new Inventaire();
                inv.setIdInventaire(rs.getInt("idInventaire"));
                inv.setDateInventaire(rs.getDate("dateInventaire").toLocalDate());
                inv.setStatut(rs.getString("statut"));
                inv.setObservations(rs.getString("observations"));
                listInventaires.add(inv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listInventaires;
    }


    @Override
    public void update(Inventaire inventaire) throws Exception {}

    @Override
    public void delete(int id) throws Exception {}


    @Override
    public List<Inventaire> findByStatut(String statut) throws Exception {
        List<Inventaire> listInventaires = new ArrayList<>();

        String sql = "SELECT * FROM inventaires WHERE statut = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, statut); // << utilise bien le paramètre

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Inventaire inventor = new Inventaire();

                    inventor.setIdInventaire(rs.getInt("idInventaire"));
                    inventor.setDateInventaire(rs.getDate("dateInventaire").toLocalDate());
                    inventor.setStatut(rs.getString("statut"));
                    inventor.setObservations(rs.getString("observations"));

                    listInventaires.add(inventor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Erreur lors de la récupération des inventaires");
        }

        return listInventaires;
    }
    public void ChangerStatus(int  idInventaire,String statut){
        String sql="UPDATE inventaires SET statut=? WHERE idInventaire=?";
        try(PreparedStatement stmt= connection.prepareStatement(sql)){
               stmt.setString(1,statut);
               stmt.setInt(2,idInventaire);
               stmt.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();

        }
    }

}

=======
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
        this.connection = DatabaseConnection.getConnection();
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

>>>>>>> d44fc21 (fin  de code)
