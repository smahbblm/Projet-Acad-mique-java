package com.stock.dao.interfaces;

import com.stock.model.stock.MouvementStock;
import java.util.List;

public interface IMouvementStockDAO {
    void create(MouvementStock mouvementStock) throws Exception;
    MouvementStock read(int id) throws Exception;
    List<MouvementStock> readAll() throws Exception;
    void update(MouvementStock mouvementStock) throws Exception;
    void delete(int id) throws Exception;
    List<MouvementStock> findByProduit(int idProduit) throws Exception;
    List<MouvementStock> findByType(String type) throws Exception;
}

