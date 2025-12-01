package com.stock.dao.interfaces;

import com.stock.model.document.LigneCommande;
import java.util.List;

public interface ILigneCommandeDAO {
    void create(LigneCommande ligneCommande) throws Exception;
    LigneCommande read(int id) throws Exception;
    List<LigneCommande> readAll() throws Exception;
    void update(LigneCommande ligneCommande) throws Exception;
    void delete(int id) throws Exception;
    List<LigneCommande> findByBonCommande(int idBonCommande) throws Exception;
}

