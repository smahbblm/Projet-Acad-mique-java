package com.stock.service;

import com.stock.dao.implementation.UtilisateurDAO;
import com.stock.model.utilisateur.Utilisateur;

import java.util.List;

public class UtilisateurService {

    private final UtilisateurDAO utilisateurDAO;

    public UtilisateurService() throws Exception {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    public List<Utilisateur> getAllUsers() throws Exception {
        return utilisateurDAO.readAll();
    }

    public void addUser(Utilisateur utilisateur) throws Exception {
        utilisateurDAO.create(utilisateur);
    }

    public void updateUser(Utilisateur utilisateur) throws Exception {
        utilisateurDAO.update(utilisateur);
    }

    public void deleteUser(int id) throws Exception {
        utilisateurDAO.delete(id);
    }

    public Utilisateur findByEmail(String email) throws Exception {
        return utilisateurDAO.findByEmail(email);
    }
}
