package com.stock.dao.interfaces;

import com.stock.model.document.BonSortie;
import java.util.List;

public interface IBonSortieDAO {
    void create(BonSortie bonSortie) throws Exception;
    BonSortie read(int id) throws Exception;
    List<BonSortie> readAll() throws Exception;
    void update(BonSortie bonSortie) throws Exception;
    void delete(int id) throws Exception;
    BonSortie findByNumero(String numero) throws Exception;
    List<BonSortie> findByStatut(String statut) throws Exception;
}

