package com.stock.service;

import com.stock.dao.interfaces.IUtilisateurDAO;
import com.stock.dao.implementation.UtilisateurDAO;
import com.stock.model.utilisateur.Utilisateur;
import com.stock.util.PasswordHasher;
import com.stock.util.SessionManager;
import com.stock.exception.AuthenticationException;

/**
 * Service pour l'authentification et gestion des sessions
 */
public class AuthService {
    private IUtilisateurDAO utilisateurDAO;


    public AuthService(IUtilisateurDAO utilisateurDAO) {
        this.utilisateurDAO = utilisateurDAO;
    }


    public void login(String email, String motDePasse) throws Exception {
        Utilisateur utilisateur = utilisateurDAO.findByEmail(email);

        if (utilisateur == null) {
            throw new AuthenticationException("Utilisateur non trouvé");
        }

        // Vérification du mot de passe
        // Pour le développement avec schema.sql, les mots de passe sont en clair
        // TODO: Utiliser PasswordHasher.verifyPassword() en production
        if (!motDePasse.equals(utilisateur.getMotDePasse())) {
            throw new AuthenticationException("Mot de passe incorrect");
        }

        if (!utilisateur.isActif()) {
            throw new AuthenticationException("Utilisateur inactif");
        }

        SessionManager.getInstance().createSession(utilisateur, utilisateur.getRole());
    }

    public void logout() {
        SessionManager.getInstance().invalidateSession();
    }

    public Utilisateur getCurrentUser() {
        Object user = SessionManager.getInstance().getCurrentUser();
        return (Utilisateur) user;
    }

    public boolean isAuthenticated() {
        return SessionManager.getInstance().isSessionActive();
    }

    public String getUserRole() {
        return SessionManager.getInstance().getUserRole();
    }
}



