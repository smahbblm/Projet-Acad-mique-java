package com.stock.dao.interfaces;

import com.stock.model.document.Facture;
import java.util.List;

public interface IFactureDAO {
    void create(Facture facture) throws Exception;
    Facture read(int id) throws Exception;
    List<Facture> readAll() throws Exception;
    void update(Facture facture) throws Exception;
    void delete(int id) throws Exception;
    Facture findByNumero(String numero) throws Exception;
    List<Facture> findByStatut(String statut) throws Exception;
    List<Facture> findByClient(int idClient) throws Exception;
}

