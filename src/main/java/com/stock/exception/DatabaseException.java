package com.stock.exception;

/**
 * Exception levée en cas d'erreur d'accès à la base de données
 */
public class DatabaseException extends Exception {
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

