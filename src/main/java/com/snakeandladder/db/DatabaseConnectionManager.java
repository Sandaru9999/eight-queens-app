package com.snakeandladder.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {
   private static final String URL = "jdbc:mysql://localhost:3306/pdsa_snake_ladder";
   private static final String USER = "";
   private static final String PASSWORD = "";

   public DatabaseConnectionManager() {
   }

   public static Connection getConnection() throws SQLException {
      try {
         Class.forName("com.mysql.cj.jdbc.Driver");
      } catch (ClassNotFoundException var1) {
         throw new RuntimeException("MySQL JDBC Driver not found. Please ensure the 'mysql-connector-j' dependency is correctly configured in your pom.xml.", var1);
      }

      return DriverManager.getConnection("jdbc:mysql://localhost:3306/pdsa_snake_ladder", "", "");
   }
}

