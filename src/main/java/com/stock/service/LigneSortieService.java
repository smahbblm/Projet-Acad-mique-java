package com.stock.service;

import com.stock.dao.implementation.LigneSortieDAO;
import com.stock.model.document.LigneSortie;

import java.util.List;

public class LigneSortieService {
    private LigneSortieDAO ligneSortieDAO;

    public LigneSortieService() throws Exception {
        this.ligneSortieDAO = new LigneSortieDAO();
    }

    // Créer une nouvelle ligne de sortie
    public void creerLigneSortie(LigneSortie ligne) throws Exception {
        ligneSortieDAO.create(ligne);
    }

    // Lire une ligne par son id
    public LigneSortie getLigneSortieById(int id) throws Exception {
        return ligneSortieDAO.read(id);
    }

    // Lire toutes les lignes
    public List<LigneSortie> getToutesLesLignes() throws Exception {
        return ligneSortieDAO.readAll();
    }

    // Mettre à jour une ligne
    public void mettreAJourLigne(LigneSortie ligne) throws Exception {
        ligneSortieDAO.update(ligne);
    }

    // Supprimer une ligne
    public void supprimerLigne(int id) throws Exception {
        ligneSortieDAO.delete(id);
    }

    // Récupérer toutes les lignes d’un bon de sortie
    public List<LigneSortie> getLignesByBonSortie(int idBonSortie) throws Exception {
        return ligneSortieDAO.findByBonSortie(idBonSortie);
    }
}
