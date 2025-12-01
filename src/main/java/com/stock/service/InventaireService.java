package com.stock.service;

import com.stock.dao.interfaces.IInventaireDAO;
import com.stock.dao.implementation.InventaireDAO;
import com.stock.model.stock.Inventaire;
import java.util.List;

/**
 * Service pour la gestion des inventaires
 */
public class InventaireService {
    private IInventaireDAO inventaireDAO;

    public InventaireService() throws Exception {
        this.inventaireDAO = new InventaireDAO();
    }

    public void creerInventaire(Inventaire inventaire) throws Exception {
        inventaireDAO.create(inventaire);
    }

    public void modifierInventaire(Inventaire inventaire) throws Exception {
        inventaireDAO.update(inventaire);
    }

    public void supprimerInventaire(int idInventaire) throws Exception {
        inventaireDAO.delete(idInventaire);
    }

    public Inventaire consulterInventaire(int idInventaire) throws Exception {
        return inventaireDAO.read(idInventaire);
    }

    public List<Inventaire> consulterTousLesInventaires() throws Exception {
        return inventaireDAO.readAll();
    }

    public List<Inventaire> consulterInventairesParStatut(String statut) throws Exception {
        return inventaireDAO.findByStatut(statut);
    }
}

