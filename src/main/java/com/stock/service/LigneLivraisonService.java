package com.stock.service;

import com.stock.dao.implementation.LigneLivraisonDAO;
import com.stock.model.document.LigneLivraison;

import java.util.List;

public class LigneLivraisonService {

    private LigneLivraisonDAO ligneLivraisonDAO;

    public LigneLivraisonService() throws Exception {
        this.ligneLivraisonDAO = new LigneLivraisonDAO();
    }

    // Créer une nouvelle ligne de livraison
    public void creerLigneLivraison(LigneLivraison ligne) throws Exception {
        ligneLivraisonDAO.create(ligne);
    }

    // Lire une ligne par son id
    public LigneLivraison getLigneLivraisonById(int id) throws Exception {
        return ligneLivraisonDAO.read(id);
    }

    // Lire toutes les lignes
    public List<LigneLivraison> getToutesLesLignes() throws Exception {
        return ligneLivraisonDAO.readAll();
    }

    // Mettre à jour une ligne
    public void mettreAJourLigne(LigneLivraison ligne) throws Exception {
        ligneLivraisonDAO.update(ligne);
    }

    // Supprimer une ligne
    public void supprimerLigne(int id) throws Exception {
        ligneLivraisonDAO.delete(id);
    }

    // Récupérer toutes les lignes d’un bon de livraison
    public List<LigneLivraison> getLignesByBonLivraison(int idBonLivraison) throws Exception {
        return ligneLivraisonDAO.findByBonLivraison(idBonLivraison);
    }
}
