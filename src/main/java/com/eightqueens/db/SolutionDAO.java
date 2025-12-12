package com.eightqueens.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SolutionDAO {

    // Save a solution in the database
    public static void saveSolution(String solution, int timeTakenMs) {
        String sql = "INSERT INTO solutions (solution, timeTakenMs) VALUES (?, ?) ON DUPLICATE KEY UPDATE timeTakenMs=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, solution);
            stmt.setInt(2, timeTakenMs);
            stmt.setInt(3, timeTakenMs);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Check if solution is already recognized
    public static boolean isSolutionRecognized(String solution) {
        String sql = "SELECT recognized FROM solutions WHERE solution=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, solution);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getBoolean("recognized");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Mark a solution as recognized
    public static void markSolutionRecognized(String solution) {
        String sql = "UPDATE solutions SET recognized=TRUE WHERE solution=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, solution);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Save player info
    public static void savePlayerSolution(String playerName, String solution) {
        String sql = "INSERT INTO players (name, solution) VALUES (?, ?) ON DUPLICATE KEY UPDATE name=name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, playerName);
            stmt.setString(2, solution);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Save player and solution together
    public static void savePlayerAndSolution(String playerName, String solution, int timeTakenMs) {
        if (!isSolutionRecognized(solution)) {
            saveSolution(solution, timeTakenMs);
            markSolutionRecognized(solution);
        }
        savePlayerSolution(playerName, solution);
    }

    // Convert board array to string
    public static String boardToString(int[] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            sb.append(board[i]);
            if (i < board.length - 1) sb.append(",");
        }
        return sb.toString();
    }

    // Get all recognized solutions
    public static List<String> getAllRecognizedSolutions() {
        List<String> recognized = new ArrayList<>();
        String sql = "SELECT solution FROM solutions WHERE recognized=TRUE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) recognized.add(rs.getString("solution"));
        } catch (Exception e) { e.printStackTrace(); }
        return recognized;
    }

    // Reset recognized flags
    public static void resetRecognizedFlags() {
        String sql = "UPDATE solutions SET recognized=FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
