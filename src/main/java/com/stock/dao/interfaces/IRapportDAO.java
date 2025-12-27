package com.stock.dao.interfaces;

import com.stock.model.systeme.Rapport;
import java.util.List;

public interface IRapportDAO {
    void create(Rapport rapport) throws Exception;
    Rapport read(int id) throws Exception;
    List<Rapport> readAll() throws Exception;
    void update(Rapport rapport) throws Exception;
    void delete(int id) throws Exception;
    List<Rapport> findByType(String type) throws Exception;
}

