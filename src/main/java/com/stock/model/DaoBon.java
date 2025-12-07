package com.stock.model;

import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class DaoBon {
    // cette classe sert à appel l'objet de connexion dans  un premier temps pour ensuite faire des requêtes dans la base de données.
    // la définitions de la méthode qui va m'ajouter les bons de livraison dans
    public int ajouterbon(String numero,LocalDate dateLivraison ,String status  , String notes) {
        // l'utilisation d'objet de connexion pour faire des requetes dans la base de données .
        String sql = "INSERT INTO Bon_livraison(numero, dateLivraison, status,adresseLivraison, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        System.out.println("la connexion vers la base de données");
        Connection objet_connection = DatabaseConnection.getConnection();
        try {
            //l'initialisation d'attributs de la requete
            PreparedStatement stm = objet_connection.prepareStatement(sql);
            stm.setString(1, numero);
            stm.setDate(2, java.sql.Date.valueOf(dateLivraison));
            stm.setString(3,status);;
            stm.setString(7, notes);
            int res = stm.executeUpdate();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            //car si le res  == 0 cela veux dire que le bon n'est ajoute
            return 0;
        }
    }

}
