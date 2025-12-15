package com.snakeandladder.db;

import com.snakeandladder.models.SnakeLadderSolution;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SnakeLadderDAO {
    
    public void saveSolution(SnakeLadderSolution solution) throws SQLException {
        // Updated table name
        String sql = "INSERT INTO solutions_snake (player_name, board_size, correct_throws, player_answer, bfs_time_nanos, dp_time_nanos) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, solution.getPlayerName());
            pstmt.setInt(2, solution.getBoardSize());
            pstmt.setInt(3, solution.getCorrectMinThrows());
            pstmt.setInt(4, solution.getPlayerAnswer());
            pstmt.setLong(5, solution.getAlgo1TimeNanos());
            pstmt.setLong(6, solution.getAlgo2TimeNanos());
            
            pstmt.executeUpdate();
            
            System.out.println("LOG: Correct solution saved successfully to solutions_snake for " + solution.getPlayerName());
            
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to save solution to solutions_snake: " + e.getMessage());
            throw e;
        }
    }

    public List<SnakeLadderSolution> getAllSolutions(int limit) throws SQLException {
        List<SnakeLadderSolution> history = new ArrayList<>();
        
        // Updated table name
        String sql = "SELECT * FROM solutions_snake ORDER BY id DESC LIMIT ?";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SnakeLadderSolution entry = new SnakeLadderSolution();
                    
                    entry.setPlayerName(rs.getString("player_name"));
                    entry.setBoardSize(rs.getInt("board_size"));
                    entry.setCorrectMinThrows(rs.getInt("correct_throws"));
                    entry.setPlayerAnswer(rs.getInt("player_answer"));
                    entry.setAlgo1TimeNanos(rs.getLong("bfs_time_nanos"));
                    entry.setAlgo2TimeNanos(rs.getLong("dp_time_nanos"));
                    
                    history.add(entry);
                }
            }
            System.out.println("LOG: Loaded " + history.size() + " records from solutions_snake.");
            
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to load history from solutions_snake: " + e.getMessage());
            throw e;
        }
        return history;
    }
}
