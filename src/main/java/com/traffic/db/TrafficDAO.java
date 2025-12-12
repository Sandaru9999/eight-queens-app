package com.traffic.db;

import com.traffic.models.TrafficSolution;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TrafficDAO {

    public static void savePlayerSolution(TrafficSolution solution) {
        String sql = "INSERT INTO traffic_results (player_name, player_answer, correct_flow, time_taken_ms) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, solution.getPlayerName());
            stmt.setInt(2, solution.getPlayerAnswer());
            stmt.setInt(3, solution.getCorrectFlow());
            stmt.setLong(4, solution.getTimeTakenMs());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
