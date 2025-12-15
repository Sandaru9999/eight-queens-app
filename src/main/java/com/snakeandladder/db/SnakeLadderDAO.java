package com.snakeandladder.db;

import com.snakeandladder.models.SnakeLadderSolution;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SnakeLadderDAO {

    public SnakeLadderDAO() {}

    /**
     * Save a full solution to the database (all details including times)
     */
    public void saveSolution(SnakeLadderSolution solution) throws SQLException {
        String sql = "INSERT INTO solutions_snake "
                   + "(player_name, board_size, correct_throws, player_answer, bfs_time_nanos, dp_time_nanos) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, solution.getPlayerName());
            pstmt.setInt(2, solution.getBoardSize());
            pstmt.setInt(3, solution.getCorrectMinThrows());
            pstmt.setInt(4, solution.getPlayerAnswer());
            pstmt.setLong(5, solution.getAlgo1TimeNanos());
            pstmt.setLong(6, solution.getAlgo2TimeNanos());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Full solution saved successfully for " + solution.getPlayerName());
            } else {
                System.out.println("❌ Full solution not saved!");
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Failed to save full solution: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Save only the player's answer and correct answer to a separate table
     */
    public void saveCorrectAnswer(SnakeLadderSolution solution) throws SQLException {
        String sql = "INSERT INTO correct_answers_snake "
                   + "(player_name, board_size, player_answer, correct_throws) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, solution.getPlayerName());
            pstmt.setInt(2, solution.getBoardSize());
            pstmt.setInt(3, solution.getPlayerAnswer());
            pstmt.setInt(4, solution.getCorrectMinThrows());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Correct answer saved successfully for " + solution.getPlayerName());
            } else {
                System.out.println("❌ Correct answer not saved!");
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Failed to save correct answer: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Get last 'limit' full solutions from the database
     */
    public List<SnakeLadderSolution> getAllSolutions(int limit) throws SQLException {
        List<SnakeLadderSolution> history = new ArrayList<>();
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

            System.out.println("LOG: Loaded " + history.size() + " records from solutions_snake table.");

        } catch (SQLException e) {
            System.err.println("ERROR: Failed to load history from solutions_snake table: " + e.getMessage());
            throw e;
        }

        return history;
    }

    /**
     * Get last 'limit' correct answers from the database
     */
    public List<SnakeLadderSolution> getAllCorrectAnswers(int limit) throws SQLException {
        List<SnakeLadderSolution> history = new ArrayList<>();
        String sql = "SELECT * FROM correct_answers_snake ORDER BY id DESC LIMIT ?";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SnakeLadderSolution entry = new SnakeLadderSolution();
                    entry.setPlayerName(rs.getString("player_name"));
                    entry.setBoardSize(rs.getInt("board_size"));
                    entry.setPlayerAnswer(rs.getInt("player_answer"));
                    entry.setCorrectMinThrows(rs.getInt("correct_throws"));
                    history.add(entry);
                }
            }

            System.out.println("LOG: Loaded " + history.size() + " records from correct_answers_snake table.");

        } catch (SQLException e) {
            System.err.println("ERROR: Failed to load correct answers: " + e.getMessage());
            throw e;
        }

        return history;
    }
}
