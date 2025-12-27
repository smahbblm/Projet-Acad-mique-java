package com.stock.dao.interfaces;

import com.stock.model.document.BonRetour;
import java.util.List;

public interface IBonRetourDAO {
    void create(BonRetour bonRetour) throws Exception;
    BonRetour read(int id) throws Exception;
    List<BonRetour> readAll() throws Exception;
    void update(BonRetour bonRetour) throws Exception;
    void delete(int id) throws Exception;
    BonRetour findByNumero(String numero) throws Exception;
    List<BonRetour> findByStatut(String statut) throws Exception;
    List<BonRetour> findByClient(int idClient) throws Exception;
}
