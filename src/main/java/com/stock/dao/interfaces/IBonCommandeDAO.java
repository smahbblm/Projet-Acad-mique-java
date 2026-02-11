package com.stock.dao.interfaces;

import com.stock.model.document.BonCommande;
import java.util.List;

/**
 * Interface pour les opérations DAO sur les bons de commande
 */
public interface IBonCommandeDAO {
    void create(BonCommande bonCommande) throws Exception;
    BonCommande read(int id) throws Exception;
    List<BonCommande> readAll() throws Exception;
    void update(BonCommande bonCommande) throws Exception;
    void delete(int id) throws Exception;
    BonCommande findByNumero(String numero) throws Exception;
    List<BonCommande> findByStatut(String statut) throws Exception;
    List<BonCommande> findByFournisseur(int idFournisseur) throws Exception;
}

