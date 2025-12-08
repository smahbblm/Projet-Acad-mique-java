package com.stock.dao.interfaces;

import com.stock.model.utilisateur.Utilisateur;
import java.util.List;

/**
 * Interface pour les opérations DAO sur les utilisateurs
 */
public interface IUtilisateurDAO {
    void create(Utilisateur utilisateur) throws Exception;
    Utilisateur read(int id) throws Exception;
    List<Utilisateur> readAll() throws Exception;
    void update(Utilisateur utilisateur) throws Exception;
    void delete(int id) throws Exception;
    Utilisateur findByEmail(String email) throws Exception;
}

