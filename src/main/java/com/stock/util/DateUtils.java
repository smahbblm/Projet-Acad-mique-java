package com.stock.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe pour les utilitaires de date
 */
public class DateUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatDate(LocalDate date) {
        return date != null ? date.format(formatter) : "";
    }

    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, formatter);
    }

    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static boolean isValidDate(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }

    public static long getDaysBetween(LocalDate startDate, LocalDate endDate) {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    }
}

