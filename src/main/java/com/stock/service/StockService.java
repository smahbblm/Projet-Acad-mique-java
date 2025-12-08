package com.stock.service;

import com.stock.dao.interfaces.IMouvementStockDAO;
import com.stock.dao.implementation.MouvementStockDAO;
import com.stock.model.stock.MouvementStock;
import java.util.List;

/**
 * Service pour la gestion des mouvements de stock
 */
public class StockService {
    private IMouvementStockDAO mouvementStockDAO;

    public StockService() throws Exception {
        this.mouvementStockDAO = new MouvementStockDAO();
    }

    public void enregistrerEntree(MouvementStock mouvement) throws Exception {
        mouvement.setTypeMouvement("ENTREE");
        mouvementStockDAO.create(mouvement);
    }

    public void enregistrerSortie(MouvementStock mouvement) throws Exception {
        mouvement.setTypeMouvement("SORTIE");
        mouvementStockDAO.create(mouvement);
    }

    public List<MouvementStock> consulterMouvementsProduit(int idProduit) throws Exception {
        return mouvementStockDAO.findByProduit(idProduit);
    }

    public List<MouvementStock> consulterMouvementsParType(String type) throws Exception {
        return mouvementStockDAO.findByType(type);
    }

    public List<MouvementStock> consulterTousMouvements() throws Exception {
        return mouvementStockDAO.readAll();
    }
}

