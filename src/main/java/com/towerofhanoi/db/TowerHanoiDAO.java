package com.towerofhanoi.db;

import com.towerofhanoi.models.HanoiSolution;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TowerHanoiDAO {

    public static void savePlayerSolution(HanoiSolution solution) {
        String sql = "INSERT INTO hanoi_results (player_name, num_disks, num_pegs, moves, time_ms, algorithm) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, solution.getPlayerName());
            stmt.setInt(2, solution.getNumDisks());
            stmt.setInt(3, solution.getNumPegs());
            stmt.setString(4, solution.getMovesSequence());
            stmt.setLong(5, solution.getTimeTakenMs());
            stmt.setString(6, solution.getAlgorithm());

            stmt.executeUpdate();
            System.out.println("Player solution saved!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
