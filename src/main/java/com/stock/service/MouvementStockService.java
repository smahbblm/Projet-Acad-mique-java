package com.stock.service;

import com.stock.dao.interfaces.IMouvementStockDAO;
import com.stock.dao.implementation.MouvementStockDAO;
import com.stock.model.stock.MouvementStock;
import java.util.List;

public class MouvementStockService {
    private IMouvementStockDAO mouvementStockDAO;

    public MouvementStockService() throws Exception {
        this.mouvementStockDAO = new MouvementStockDAO();
    }

    public List<MouvementStock> consulterTousMouvements() throws Exception {
        return mouvementStockDAO.readAll();
    }

    public List<MouvementStock> consulterMouvementsParProduit(int idProduit) throws Exception {
        return mouvementStockDAO.findByProduit(idProduit);
    }

    public List<MouvementStock> consulterMouvementsParType(String type) throws Exception {
        return mouvementStockDAO.findByType(type);
    }
}
