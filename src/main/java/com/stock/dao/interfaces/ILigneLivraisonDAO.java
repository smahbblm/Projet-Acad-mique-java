package com.stock.dao.interfaces;

import com.stock.model.document.LigneLivraison;
import java.util.List;

public interface ILigneLivraisonDAO {
    void create(LigneLivraison ligneLivraison) throws Exception;
    LigneLivraison read(int id) throws Exception;
    List<LigneLivraison> readAll() throws Exception;
    void update(LigneLivraison ligneLivraison) throws Exception;
    void delete(int id) throws Exception;
    List<LigneLivraison> findByBonLivraison(int idBonLivraison) throws Exception;
}

