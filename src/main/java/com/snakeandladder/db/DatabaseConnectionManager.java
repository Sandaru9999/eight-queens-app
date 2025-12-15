package com.snakeandladder.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private static final String URL = "jdbc:mysql://localhost:3306/pdsa?serverTimezone=UTC";
    private static final String USER = "root"; // Your DB username
    private static final String PASSWORD = ""; // Your DB password

    public DatabaseConnectionManager() {
        // empty constructor
    }

    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found. Please ensure the connector is in your classpath.", e);
        }

        // Return connection to pdsa database
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
