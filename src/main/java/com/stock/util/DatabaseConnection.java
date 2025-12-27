<<<<<<< HEAD

=======
>>>>>>> d44fc21 (fin  de code)
package com.stock.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

<<<<<<< HEAD
    private static DatabaseConnection instance;

    private static final String URL = "jdbc:mysql://localhost:3306/stock_management?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "latifa19.";

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
=======
    private static final String URL = "jdbc:mysql://localhost:3307/stock_management";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static Connection connection = null;

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
>>>>>>> d44fc21 (fin  de code)
