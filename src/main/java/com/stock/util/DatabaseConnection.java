package com.stock.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe pour la connexion à la base de données
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // Configuration à charger depuis database.properties
    private static final String URL = "jdbc:mysql://localhost:3306/stock_management";
    private static final String USER = "root";
    private static final String PASSWORD = "oumnia 2004";

    private DatabaseConnection() throws SQLException {
        try {
            // Essayer de charger le driver explicitement pour les environnements modulaires
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Si Class.forName échoue, essayer de charger via réflexion
            try {
                Class<?> driverClass = Class.forName("com.mysql.cj.jdbc.Driver");
                java.sql.Driver driver = (java.sql.Driver) driverClass.getDeclaredConstructor().newInstance();
                DriverManager.registerDriver(driver);
            } catch (Exception ex) {
                System.err.println("⚠️ Impossible de charger le driver MySQL explicitement.");
                System.err.println("Le driver devrait être chargé automatiquement via SPI...");
            }
        }
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static synchronized DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

