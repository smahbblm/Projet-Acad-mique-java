package com.stock.exception;

/**
 * Exception levée en cas d'erreur de validation
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

