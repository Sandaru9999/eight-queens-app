package com.traffic.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/pdsa?serverTimezone=UTC";
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 

    // ---------------- GET CONNECTION ----------------
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // load driver
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                "MySQL JDBC Driver not found. Ensure mysql-connector-java is in your classpath.", e
            );
        }

        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    // ---------------- SAVE GAME RECORD ----------------
    public void saveGameRecord(String playerName, int correctFlow, double timeEK, double timeDinic) { 
        if (playerName == null || playerName.trim().isEmpty()) {
            System.err.println("Validation Error: Player name cannot be empty.");
            return;
        }

        long ekTimeMs = Math.round(timeEK * 1000.0);     // convert to ms
        long dinicTimeMs = Math.round(timeDinic * 1000.0);

        String sql = "INSERT INTO game_records " +
                     "(player_name, correct_flow, edmonds_time_ms, dinic_time_ms) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, playerName);
            pstmt.setInt(2, correctFlow);
            pstmt.setLong(3, ekTimeMs);
            pstmt.setLong(4, dinicTimeMs);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Game record saved successfully for " + playerName);
            } else {
                System.err.println("Database Error: 0 rows affected. Check table schema.");
            }

        } catch (SQLException e) {
            System.err.println("❌ CRITICAL DB ERROR: Could not connect or execute query.");
            System.err.println("  SQL State: " + e.getSQLState() +
                               ", Error Code: " + e.getErrorCode() +
                               ", Message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
