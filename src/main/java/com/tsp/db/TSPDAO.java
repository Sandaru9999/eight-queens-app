package com.tsp.db;

import com.tsp.models.TSPSolution;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TSPDAO {

    public static void savePlayerSolution(TSPSolution solution) {
        // ✅ Save only if solution is correct
        if (!solution.isCorrect()) {
            System.out.println("Solution is not correct. Not saved to database.");
            return;
        }

        String sql = "INSERT INTO tsp_results (player_name, home_city, selected_cities, shortest_route, total_distance, time_ms, algorithm) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, solution.getPlayerName());
            stmt.setString(2, String.valueOf(solution.getHomeCity()));
            stmt.setString(3, solution.getSelectedCities());
            stmt.setString(4, solution.getRoute());
            stmt.setInt(5, solution.getTotalDistance());
            stmt.setLong(6, solution.getTimeTakenMs());
            stmt.setString(7, solution.getAlgorithm());

            stmt.executeUpdate();
            System.out.println("Correct TSP solution saved!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
