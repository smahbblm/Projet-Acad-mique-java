package com.stock.dao.interfaces;

import com.stock.model.document.LigneSortie;
import java.util.List;

public interface ILigneSortieDAO {
    void create(LigneSortie ligneSortie) throws Exception;
    LigneSortie read(int id) throws Exception;
    List<LigneSortie> readAll() throws Exception;
    void update(LigneSortie ligneSortie) throws Exception;
    void delete(int id) throws Exception;
    List<LigneSortie> findByBonSortie(int idBonSortie) throws Exception;
}

