package com.stock.service;

import com.stock.dao.implementation.BonSortieDAO;
import com.stock.model.document.BonSortie;

import java.util.List;

public class BonSortieService {
    private BonSortieDAO bonSortieDAO;

    public BonSortieService() throws Exception {
        this.bonSortieDAO = new BonSortieDAO();
    }

    // Créer un nouveau bon de sortie
    public void creerBonSortie(BonSortie bonSortie) throws Exception {
        bonSortieDAO.create(bonSortie);
    }

    // Lire un bon par son id
    public BonSortie getBonSortieById(int id) throws Exception {
        return bonSortieDAO.read(id);
    }

    // Lire tous les bons
    public List<BonSortie> getTousLesBons() throws Exception {
        return bonSortieDAO.readAll();
    }

    // Mettre à jour un bon
    public void mettreAJourBon(BonSortie bonSortie) throws Exception {
        bonSortieDAO.update(bonSortie);
    }

    // Supprimer un bon
    public void supprimerBon(int id) throws Exception {
        bonSortieDAO.delete(id);
    }

    // Rechercher par numéro
    public BonSortie getBonParNumero(String numero) throws Exception {
        return bonSortieDAO.findByNumero(numero);
    }

    // Rechercher par statut
    public List<BonSortie> getBonsParStatut(String statut) throws Exception {
        return bonSortieDAO.findByStatut(statut);
    }
}
