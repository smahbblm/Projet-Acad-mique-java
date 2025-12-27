package com.stock.service;
import com.stock.dao.implementation.BonLivraisonDAO;
import com.stock.dao.implementation.ClientDAO;
import com.stock.dao.implementation.BonSortieDAO;
import com.stock.dao.implementation.FactureDAO;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.net.ConnectException;
import java.time.LocalDate;
import java.util.List;

public class BonService {

    public int ajouterbons(String nombons , LocalDate dateLivraison, String status  ,String adresseLivraison , String notes){
        System.out.println("l'appel de la méthode ajouterbons avec  paramètres du formulaire ");
        try {
            BonLivraisonDAO dao = new BonLivraisonDAO();
            com.stock.model.document.BonLivraison bon = new com.stock.model.document.BonLivraison();
            bon.setNumero(nombons);
            bon.setDateLivraison(dateLivraison);
            bon.setStatut(status);
            bon.setAdresseLivraison(adresseLivraison);
            bon.setObservations(notes);
            dao.create(bon);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public ObservableList<String> getclient(){
        try {
            ClientDAO dao = new ClientDAO();
            List<com.stock.model.partenaire.Client> clients = dao.readAll();
            ObservableList<String> liste_noms = FXCollections.observableArrayList();
            for (com.stock.model.partenaire.Client c : clients) {
                liste_noms.add(c.getNom());
            }
            return liste_noms;
        } catch (Exception e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }
    
    public void supprimerBon(String numeros) {
        System.out.println("je suis la fonction de supprission ");
        try {
            BonLivraisonDAO dao = new BonLivraisonDAO();
            com.stock.model.document.BonLivraison bon = dao.findByNumero(numeros);
            if (bon != null) {
                dao.delete(bon.getIdBonLivraison());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ObservableList<String> getbonsortie(){
        try {
            BonSortieDAO dao = new BonSortieDAO();
            List<com.stock.model.document.BonSortie> bons = dao.readAll();
            ObservableList<String> liste_numeros = FXCollections.observableArrayList();
            for (com.stock.model.document.BonSortie b : bons) {
                liste_numeros.add(b.getNumero());
            }
            return liste_numeros;
        } catch (Exception e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }
    
    public ObservableList<String> list_factures(){
        try {
            FactureDAO dao = new FactureDAO();
            List<com.stock.model.document.Facture> factures = dao.readAll();
            ObservableList<String> list_numeros = FXCollections.observableArrayList();
            for (com.stock.model.document.Facture f : factures) {
                list_numeros.add(f.getNumero());
            }
            return list_numeros;
        } catch (Exception e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }
    
    public ObservableList<String> liste_numero_bonliv(){
        try {
            BonLivraisonDAO dao = new BonLivraisonDAO();
            List<com.stock.model.document.BonLivraison> bons = dao.readAll();
            ObservableList<String> liste_numeros = FXCollections.observableArrayList();
            for (com.stock.model.document.BonLivraison b : bons) {
                liste_numeros.add(b.getNumero());
            }
            return liste_numeros;
        } catch (Exception e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }

    public ObservableList<com.stock.model.document.BonLivraison> getAllBons(){
        try {
            BonLivraisonDAO dao = new BonLivraisonDAO();
            List<com.stock.model.document.BonLivraison> bons = dao.readAll();
            return FXCollections.observableArrayList(bons);
        } catch (Exception e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }
    
    public int getnombrebons(){
        try {
            BonLivraisonDAO dao = new BonLivraisonDAO();
            return dao.readAll().size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
