package com.stock.dao.interfaces;

import com.stock.model.produit.Produit;
import java.util.List;

/**
 * Interface pour les opérations DAO sur les produits
 */
public interface IProduitDAO {
    void create(Produit produit) throws Exception;
    Produit read(int id) throws Exception;
    List<Produit> readAll() throws Exception;
    void update(Produit produit) throws Exception;
    void delete(int id) throws Exception;
    Produit findByReference(String reference) throws Exception;
    List<Produit> findByCategorie(String categorie) throws Exception;
    List<Produit> findStockBas() throws Exception;
    List<String> getCategories() throws Exception;
}
