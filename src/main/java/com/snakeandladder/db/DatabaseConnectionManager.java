package com.snakeandladder.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnectionManager {
    
    
    private static final String URL = "jdbc:mysql://localhost:3306/pdsa_snake_ladder";
    private static final String USER = ""; 
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
           
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch (ClassNotFoundException e) {
            
            throw new RuntimeException("MySQL JDBC Driver not found. Please ensure the 'mysql-connector-j' dependency is correctly configured in your pom.xml.", e);
        }
        
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}