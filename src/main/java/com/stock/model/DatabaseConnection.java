package com.stock.model;
// DatabaseConnection.java

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Informations de connexion (UNE SEULE FOIS)
    private static final String URL = "jdbc:mysql://localhost:3307/stock_management";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    // Instance unique (Singleton)
    private static Connection connection = null;

    // Méthode pour obtenir la connexion
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connexion établie !");
            }
        } catch (SQLException e) {
            System.err.println("Erreur de connexion !");
            e.printStackTrace();
        }
        return connection;
    }

    // Méthode pour fermer la connexion
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion fermée !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
