package com.traffic.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/traffic_game";
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 

    private Connection getConnection() throws SQLException {
        try {
           
            System.out.println("Attempting to load MySQL JDBC Driver..."); 
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully. Attempting connection...");
            return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            
            System.err.println("❌ DRIVER ERROR: MySQL Driver not found.");
            System.err.println("  Check your project dependencies (pom.xml/build.gradle) for the mysql-connector-java JAR.");
            throw new SQLException("MySQL Driver not found. Check if the connector JAR is included.", e);
        }
    }

    public void saveGameRecord(String playerName, int correctFlow, double timeEK, double timeDinic) { 
        if (playerName == null || playerName.trim().isEmpty()) {
            System.err.println("Validation Error: Player name cannot be empty.");
            return;
        }
        
       
        long ekTimeUs = Math.round(timeEK * 1000.0); 
        long dinicTimeUs = Math.round(timeDinic * 1000.0);

      
        String sql = "INSERT INTO game_records (player_name, correct_flow, edmonds_time_ms, dinic_time_ms) VALUES (?, ?, ?, ?)"; 
        
        System.out.println("Attempting to save record: " + playerName + " | EK Time (us): " + ekTimeUs); 

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, playerName);
            pstmt.setInt(2, correctFlow);
           
            pstmt.setLong(3, ekTimeUs); 
            pstmt.setLong(4, dinicTimeUs); 

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Game record saved successfully for " + playerName);
            } else {
                System.err.println("Database Error: Failed to insert record (0 affected rows). Check SQL and table schema.");
            }

        } catch (SQLException e) {
          
            System.err.println("❌ CRITICAL DB ERROR: Could not connect or execute query.");
            System.err.println("  Details: SQL State " + e.getSQLState() + ", Error Code " + e.getErrorCode() + ", Message: " + e.getMessage());
            e.printStackTrace(); 
        }
    }
}
