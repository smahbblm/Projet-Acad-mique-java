package com.stock.service;
import  java.lang.System;
import java.util.List;

import com.stock.model.document.Facture;
import com.stock.dao.implementation.FactureDAO;

public class FactureServce {
    public void main(String[] args) {
        System.out.println("je suis la méthode pour récupere les facts");
    }
    
    public List<Facture> recuper_allfactures(){
        System.out.println("l'étape suivant c'est d'appeler la méthode de récupération des données depuis le DAO");
        try {
            FactureDAO factureDAO = new FactureDAO();
            List<Facture> factures = factureDAO.readAll();
            return factures;
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

}