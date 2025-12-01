package com.stock.exception;

/**
 * Exception levée en cas d'erreur de gestion de stock
 */
public class StockException extends Exception {
    public StockException(String message) {
        super(message);
    }

    public StockException(String message, Throwable cause) {
        super(message, cause);
    }
}

