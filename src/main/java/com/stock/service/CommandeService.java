package com.stock.service;

import com.stock.dao.interfaces.IBonCommandeDAO;
import com.stock.dao.implementation.BonCommandeDAO;
import com.stock.model.document.BonCommande;
import java.util.List;

/**
 * Service pour la gestion des commandes
 */
public class CommandeService {
    private IBonCommandeDAO bonCommandeDAO;

    public CommandeService() throws Exception {
        this.bonCommandeDAO = new BonCommandeDAO();
    }

    public void creerBonCommande(BonCommande bonCommande) throws Exception {
        bonCommandeDAO.create(bonCommande);
    }

    public void modifierBonCommande(BonCommande bonCommande) throws Exception {
        bonCommandeDAO.update(bonCommande);
    }

    public void supprimerBonCommande(int idBonCommande) throws Exception {
        bonCommandeDAO.delete(idBonCommande);
    }

    public BonCommande consulterBonCommande(int idBonCommande) throws Exception {
        return bonCommandeDAO.read(idBonCommande);
    }

    public List<BonCommande> consulterTousLesBonsCommande() throws Exception {
        return bonCommandeDAO.readAll();
    }

    public List<BonCommande> consulterBonsCommandeParStatut(String statut) throws Exception {
        return bonCommandeDAO.findByStatut(statut);
    }

    public List<BonCommande> consulterBonsCommandeParFournisseur(int idFournisseur) throws Exception {
        return bonCommandeDAO.findByFournisseur(idFournisseur);
    }
}

