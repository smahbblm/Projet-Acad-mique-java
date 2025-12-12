package com.stock.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.sql.Statement;

public class DaoBon {
    // cette classe sert à appel l'objet de connexion dans  un premier temps pour ensuite faire des requêtes dans la base de données.
    // la définitions de la méthode qui va m'ajouter les bons de livraison dans
    public int ajouterbon(String numero,LocalDate dateLivraison ,String status ,String adresseLivraison,  String notes) {
        // l'utilisation d'objet de connexion pour faire des requetes dans la base de données .
        String sql = "INSERT INTO Bon_livraison(numero, dateLivraison, statut,adresseLivraison, observations,idClient ) VALUES (?, ?, ?, ?,?,?)";
        System.out.println("la connexion vers la base de données");
        Connection objet_connection = DatabaseConnection.getConnection();
        try {
            //l'initialisation d'attributs de la requete
            PreparedStatement stm = objet_connection.prepareStatement(sql);
            stm.setString(1, numero);
            stm.setDate(2, java.sql.Date.valueOf(dateLivraison));
            stm.setString(3,status);
            stm.setString(4,adresseLivraison);
            stm.setString(5, notes);
            stm.setInt(6,1);
            int res = stm.executeUpdate();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            //car si le res  == 0 cela veux dire que le bon n'est ajoute
            return 0;
        }
    }
    //la requette qui permet  de récupere les clients qu'on a dans la base de données
    public ObservableList<String> getallclients(){
        //la connection vers la base de données
        String sql = "SELECT nom FROM clients ";
        // l création de la liste ayant la caractéristque d'oservation de plus
        ObservableList<String> liste_noms = FXCollections.observableArrayList();

        try (
                //dans ce bloc de codes j'ai essaié d'utilisé le bloc try-with-ressoureces
                Connection objet_conne = DatabaseConnection.getConnection();
                Statement stm = objet_conne.createStatement();
                ResultSet res = stm.executeQuery(sql);
                ){
            while (res.next()){
                //c'est pour ajouter les noms des clients.
                liste_noms.add(res.getString("nom"));
            }
            return liste_noms;
        }catch (SQLException e){
            e.printStackTrace();
        }
        // la fermiture de la connexion
        return liste_noms;

    }
    // la méthode de la récuperation de  bon_sortie
    public ObservableList<String> getbonsortie(){
        System.out.println("la méthode de la récuperation de  bon_sortie");
        String sql = "SELECT numero FROM bon_sortie";
        ObservableList<String> liste_numeros = FXCollections.observableArrayList();
        try (
                Connection objet_conne = DatabaseConnection.getConnection();
                Statement stm = objet_conne.createStatement();
                ResultSet res = stm.executeQuery(sql);
        ){
            while(res.next()){
                liste_numeros.add(res.getString("numero"));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return liste_numeros;
    }
    //méthode pour la récuperation de la liste des factures
    public ObservableList<String> getlistefactures(){
        System.out.println("fonction de la récuperation de la liste des factures ");
        String sql = "SELECT numero FROM factures";
        ObservableList<String> list_numeros = FXCollections.observableArrayList();
        try(
                // les ressoureces
                Connection objet_conne = DatabaseConnection.getConnection();
                Statement stm = objet_conne.createStatement();
                ResultSet res = stm.executeQuery(sql);
        ){
                while(res.next()) {
                      list_numeros.add(res.getString("numero"));
                 }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list_numeros;
    }
    //méthode pour la supprission d'un bon  de la base de données
    public int supprimerbon(String numero){
        System.out.println("la méthode de la supprission d'un bon de la base de données");
        String sql = "DELETE FROM bon_livraison WHERE numero = ?";
        try(
                Connection objet_conne = DatabaseConnection.getConnection();
                PreparedStatement stm = objet_conne.prepareStatement(sql);
        ){
            //mais ici je dois récuperer comme meme l'id de l'element  que je veux supprimer
            stm.setString(1,numero);
            int res = stm.executeUpdate();
            return res;
        }catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }
    //liste de numero de bons de livraisons
    public ObservableList<String> getbonslivraison(){
        String SQL = "SELECT numero FROM bon_livraison";
        ObservableList<String> liste_numeros_bon_livraison = FXCollections.observableArrayList();
        try(
                Connection objet_conn = new DatabaseConnection().getConnection();
                PreparedStatement stm = objet_conn.prepareStatement(SQL);
                ResultSet res = stm.executeQuery(SQL);
        ){
            while(res.next()){
                System.out.println("le bon de numeros de bon de livraison "+res.getString("numero"));
                //liste_numeros contient juste les numeros de bon de livraison
                liste_numeros_bon_livraison.add(res.getString("numero"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        };
       return  liste_numeros_bon_livraison;
    }
    // Méthode pour récupérer tous les bons de livraison
    public ObservableList<com.stock.model.document.BonLivraison> getallbons(){
        String sql = "SELECT bl.*, c.nom as clientNom FROM bon_livraison bl LEFT JOIN clients c ON bl.idClient = c.idClient";
        ObservableList<com.stock.model.document.BonLivraison> liste_bons = FXCollections.observableArrayList();
        try(
                Connection objet_conne = DatabaseConnection.getConnection();
                Statement stm = objet_conne.createStatement();
                ResultSet res = stm.executeQuery(sql);
        ){
            while(res.next()){
                com.stock.model.document.BonLivraison bon = new com.stock.model.document.BonLivraison();
                bon.setNumero(res.getString("numero"));
                bon.setDateLivraison(res.getDate("dateLivraison").toLocalDate());
                bon.setStatut(res.getString("statut"));
                bon.setAdresseLivraison(res.getString("adresseLivraison"));
                bon.setObservations(res.getString("observations"));
                
                // Créer un client avec le nom
                com.stock.model.partenaire.Client client = new com.stock.model.partenaire.Client();
                client.setNom(res.getString("clientNom"));
                bon.setClient(client);
                
                liste_bons.add(bon);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return liste_bons;
    }
    //méthode pour le nombre totale de bons de  livraisons
    public int gettotalbons(){
        String sql = "SELECT COUNT(*) FROM bon_livraison ";
        try(
             Connection objt_conn = DatabaseConnection.getConnection();
             PreparedStatement stm = objt_conn.prepareStatement(sql);
             ResultSet res = stm.executeQuery();
             ){
            //avec cette resultat je dois faire le traitement necessaire pour que me  returne un int
              if (res.next()){
                  return res.getInt(1);
              }
        }catch(SQLException e ){
            e.printStackTrace();
        }
        return 0;
    }
}
