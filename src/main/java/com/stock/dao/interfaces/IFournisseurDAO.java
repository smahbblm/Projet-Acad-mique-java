package com.stock.dao.interfaces;

import com.stock.model.partenaire.Fournisseur;
import java.util.List;

/**
 * Interface pour les opérations DAO sur les fournisseurs
 */
public interface IFournisseurDAO {
    void create(Fournisseur fournisseur) throws Exception;
    Fournisseur read(int id) throws Exception;
    List<Fournisseur> readAll() throws Exception;
    void update(Fournisseur fournisseur) throws Exception;
    void delete(int id) throws Exception;
    Fournisseur findByEmail(String email) throws Exception;
    List<Fournisseur> findByRaisonSociale(String raisonSociale) throws Exception;
}

