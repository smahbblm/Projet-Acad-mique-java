package com.stock.util;

/**
 * Classe pour les utilitaires de validation
 */
public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }

    public static boolean isValidPhone(String phone) {
        String phoneRegex = "^[0-9\\s\\-\\+\\(\\)]+$";
        return phone != null && phone.matches(phoneRegex) && phone.length() >= 10;
    }

    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }

    public static boolean isValidPrice(float price) {
        return price >= 0;
    }

    public static boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }

    public static boolean isValidReference(String reference) {
        return reference != null && !reference.trim().isEmpty() && reference.length() <= 50;
    }
}

