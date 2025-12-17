package com.stock.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;

    private static final String URL = "jdbc:mysql://localhost:3306/stock_management?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private DatabaseConnection() {}

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        // Chaque appel crée une NOUVELLE connexion
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
