package com.snakeandladder.db;

import com.snakeandladder.models.SnakeLadderSolution;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SnakeLadderDAO {

    public static void savePlayerSolution(SnakeLadderSolution solution) {
        String sql = "INSERT INTO snake_ladder_results " +
                     "(player_name, board_size, player_answer, correct_throws, time_taken_ms) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, solution.getPlayerName());
            stmt.setInt(2, solution.getBoardSize());
            stmt.setInt(3, solution.getPlayerAnswer());
            stmt.setInt(4, solution.getCorrectThrows());
            stmt.setLong(5, solution.getTimeTakenMs());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
