package com.stock.util;

/**
 * Classe pour gérer la session utilisateur
 */
public class SessionManager {
    private static SessionManager instance;
    private Object currentUser;
    private String userRole;

    private SessionManager() {
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void createSession(Object user, String role) {
        this.currentUser = user;
        this.userRole = role;
    }

    public Object getCurrentUser() {
        return currentUser;
    }

    public String getUserRole() {
        return userRole;
    }

    public void invalidateSession() {
        this.currentUser = null;
        this.userRole = null;
    }

    public boolean isSessionActive() {
        return currentUser != null;
    }
}

