package com.stock.dao.interfaces;

import com.stock.model.stock.Inventaire;
import java.util.List;

public interface IInventaireDAO {
    void create(Inventaire inventaire) throws Exception;
    Inventaire read(int id) throws Exception;
    List<Inventaire> readAll() throws Exception;
    void update(Inventaire inventaire) throws Exception;
    void delete(int id) throws Exception;
    List<Inventaire> findByStatut(String statut) throws Exception;
}

