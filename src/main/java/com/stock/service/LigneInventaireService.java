package com.stock.service;

import com.stock.dao.implementation.LigneInventaireDAO;
import com.stock.model.stock.LigneInventaire;

import java.util.List;

public class LigneInventaireService {
    private LigneInventaireDAO ligneDao;
    public LigneInventaireService() throws Exception{
        this.ligneDao=new LigneInventaireDAO();
    }
    public void enregistrerLigneInvetor(LigneInventaire ligne) throws  Exception{
                  ligneDao.create(ligne);

    }
    public List<LigneInventaire> findLigne(int inventaireId) throws  Exception{
        return ligneDao.findByInventaire(inventaireId);

    }
}
