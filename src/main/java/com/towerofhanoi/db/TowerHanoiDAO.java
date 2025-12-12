package com.towerofhanoi.db;

import com.towerofhanoi.models.HanoiSolution;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TowerHanoiDAO {

    private static final String INSERT_SQL = "INSERT INTO hanoi_results (player_name, num_disks, num_pegs, moves, time_ms, algorithm, algorithm_time_ms, is_correct) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * Save a player's solution or an algorithm run. Use HanoiSolution fields.
     */
    public static void savePlayerSolution(HanoiSolution solution, boolean isCorrect, Long algorithmTimeMs) {
        if (solution == null) throw new IllegalArgumentException("solution must not be null");
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {

            stmt.setString(1, solution.getPlayerName());
            stmt.setInt(2, solution.getNumDisks());
            stmt.setInt(3, solution.getNumPegs());
            stmt.setString(4, solution.getMovesSequence());
            stmt.setLong(5, solution.getTimeTakenMs());
            stmt.setString(6, solution.getAlgorithm());
            if (algorithmTimeMs != null) stmt.setLong(7, algorithmTimeMs);
            else stmt.setNull(7, java.sql.Types.BIGINT);
            stmt.setInt(8, isCorrect ? 1 : 0);

            stmt.executeUpdate();
            System.out.println("Saved hanoi_results row: player=" + solution.getPlayerName() + " algorithm=" + solution.getAlgorithm());
        } catch (SQLException e) {
            System.err.println("Failed to save solution: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
