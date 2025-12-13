package com.stock.service;

import com.stock.dao.implementation.LigneCommandeDAO;
import com.stock.model.document.LigneCommande;

import java.util.List;

public class LigneCommandeService {
    private LigneCommandeDAO ligneCommandedao;
    public  LigneCommandeService()throws  Exception{
        this.ligneCommandedao=new LigneCommandeDAO();
    }
    public List<LigneCommande> getLignesByBonCommande(int IdCommande) throws Exception{
         return ligneCommandedao.findByBonCommande(IdCommande);
    }
}
