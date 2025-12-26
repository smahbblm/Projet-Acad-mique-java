package com.stock.service;

import com.stock.dao.interfaces.IBonLivraisonDAO;
import com.stock.dao.implementation.BonLivraisonDAO;
import com.stock.model.document.BonLivraison;
import java.util.List;

/**
 * Service pour la gestion des ventes et livraisons
 */
public class VenteService {
    private IBonLivraisonDAO bonLivraisonDAO;

    public VenteService() throws Exception {
        this.bonLivraisonDAO = new BonLivraisonDAO();
    }

    public void creerBonLivraison(BonLivraison bonLivraison) throws Exception {
        bonLivraisonDAO.create(bonLivraison);
    }

    public void modifierBonLivraison(BonLivraison bonLivraison) throws Exception {
        bonLivraisonDAO.update(bonLivraison);
    }

    public void supprimerBonLivraison(int idBonLivraison) throws Exception {
        bonLivraisonDAO.delete(idBonLivraison);
    }

    public BonLivraison consulterBonLivraison(int idBonLivraison) throws Exception {
        return bonLivraisonDAO.read(idBonLivraison);
    }

    public List<BonLivraison> consulterTousLesBonsLivraison() throws Exception {
        return bonLivraisonDAO.readAll();
    }

    public List<BonLivraison> consulterBonsLivraisonParStatut(String statut) throws Exception {
        return bonLivraisonDAO.findByStatut(statut);
    }

    public List<BonLivraison> consulterBonsLivraisonParClient(int idClient) throws Exception {
        return bonLivraisonDAO.findByClient(idClient);
    }
    public void mettreAJourStatut(BonLivraison bonLivraison) throws Exception {
        // On suppose que tu veux juste mettre à jour le statut
        bonLivraisonDAO.update(bonLivraison);
    }
}

