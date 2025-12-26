package com.stock.dao.interfaces;

import com.stock.model.document.BonLivraison;
import java.util.List;

public interface IBonLivraisonDAO {
    void create(BonLivraison bonLivraison) throws Exception;
    BonLivraison read(int id) throws Exception;
    List<BonLivraison> readAll() throws Exception;
    void update(BonLivraison bonLivraison) throws Exception;
    void delete(int id) throws Exception;
    BonLivraison findByNumero(String numero) throws Exception;
    List<BonLivraison> findByStatut(String statut) throws Exception;
    List<BonLivraison> findByClient(int idClient) throws Exception;
    void updateStatut(BonLivraison bonLivraison) throws Exception;
}

