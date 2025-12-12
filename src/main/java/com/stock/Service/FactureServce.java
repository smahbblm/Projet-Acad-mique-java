package com.stock.service;
import  java.lang.System;
import java.util.List;

//import com.stock.api.ApisFactures;
import com.stock.model.Facture;
import com.stock.model.FactureDAO;

public class FactureServce {
    public void main(String[] args) {
        //dans cette service on doit mettre le code  de la méthode qui  envois  la requtte vers le backend pour récuper les données
        System.out.println("je suis la méthode pour récupere les facts");
    }
    /*ApisFactures apisFactures = new ApisFactures();
    public List<Object> allfacture(){
        System.out.println("je suis la méthode pour faire appel à la méthode des apis ");
        return apisFactures.getAllFactures();
    }*/
    // c'est la définition de la méthode  affiche_factures.
    public List<Facture> recuper_allfactures(){
        //phase 1 : récupération de ces factures.
        System.out.println("l'étape suivant c'est d'appeler la méthode de récupération des données depuis le DAO");
        FactureDAO factureDAO = new FactureDAO();
        //J'AI L'OBJET de la classe DAO et puiseque j'ai besoin d'une méthode pour récuperer les factures je dois instancier le dao.
        List<Facture> factures = factureDAO.getAllFactures();
        // returne la liste des factures
        return factures;

    }

}