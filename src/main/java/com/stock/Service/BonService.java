package com.stock.service;
import com.stock.model.DaoBon;
import javafx.collections.ObservableList;

import java.net.ConnectException;
import java.time.LocalDate;
import java.util.List;

public class BonService {

    public int ajouterbons(String nombons , LocalDate dateLivraison, String status  ,String adresseLivraison , String notes){
        System.out.println("l'appel de la méthode ajouterbons avec  paramètres du formulaire ");
        DaoBon daoBon = new DaoBon();
        int res = daoBon.ajouterbon(nombons, dateLivraison,status,adresseLivraison, notes);
        return res;

    }
    public ObservableList<String> getclient(){
        DaoBon daoBon = new DaoBon();
        ObservableList<String> liste_noms = daoBon.getallclients();
        return liste_noms;
    }
    // la méthode de supprission d'un bon de  bon dans la base de données
    public void supprimerBon(String numeros) {
        System.out.println("je suis la fonction de supprission ");
        DaoBon daoBon = new DaoBon();
        // on doit connaitre le numeros  de bon qu'on dois supprimer.
        daoBon.supprimerbon(numeros);


    }
    // méthode pour la récupération de la liste des bons de sortie
    public ObservableList<String> getbonsortie(){
        DaoBon daoBon = new DaoBon();
        ObservableList<String> liste_numeros = daoBon.getbonsortie();
        return liste_numeros;
    }
    //méthode pour la récuperation de la liste des factures
    public ObservableList<String> list_factures(){
        DaoBon daobon = new DaoBon();
        return daobon.getlistefactures();
    }
    //la récupération de la liste de bons de livraison
    public ObservableList<String> liste_numero_bonliv(){
        DaoBon daobon = new DaoBon();
        // cette fonction ça va me récuperre la liste de bon de livraison
        return daobon.getbonslivraison();
    }

    // Méthode pour récupérer tous les bons de livraison
    public ObservableList<com.stock.model.document.BonLivraison> getAllBons(){
        DaoBon daobon = new DaoBon();
        return daobon.getallbons();
    }
    //méthode pour la récuperation de nombre total de bons
    public int getnombrebons(){
        DaoBon daobon = new DaoBon();
        return daobon.gettotalbons();
    }

}
