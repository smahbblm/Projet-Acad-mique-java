package com.stock.dao.interfaces;

import com.stock.model.stock.LigneInventaire;
import java.util.List;

public interface ILigneInventaireDAO {
    void create(LigneInventaire ligneInventaire) throws Exception;
    LigneInventaire read(int id) throws Exception;
    List<LigneInventaire> readAll() throws Exception;
    void update(LigneInventaire ligneInventaire) throws Exception;
    void delete(int id) throws Exception;
    List<LigneInventaire> findByInventaire(int idInventaire) throws Exception;
}

