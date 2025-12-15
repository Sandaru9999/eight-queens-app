package com.eightqueens.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SolutionDAO {

    // ---------------- SAVE SOLUTION ----------------
    public static void saveSolution(String solution, int timeTakenMs) {
        String sql = "INSERT INTO solutions (solution, timeTakenMs) " +
                     "VALUES (?, ?) ON DUPLICATE KEY UPDATE timeTakenMs=?";
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

    // ---------------- CHECK IF SOLUTION IS RECOGNIZED ----------------
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

    // ---------------- MARK SOLUTION AS RECOGNIZED ----------------
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

    // ---------------- SAVE PLAYER + SOLUTION WITH ALGORITHM AND TIME ----------------
    public static void savePlayerWithAlgorithm(String playerName, String solution, String algorithm, int timeTakenMs) {

        try {
            // Save solution if not recognized yet
            if (!isSolutionRecognized(solution)) {
                saveSolution(solution, timeTakenMs);
                markSolutionRecognized(solution);
            }

            // Save player info
            String sql = "INSERT INTO players (name, solution, algorithm, timeTakenMs) " +
                         "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE algorithm=?, timeTakenMs=?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, playerName);
                stmt.setString(2, solution);
                stmt.setString(3, algorithm);
                stmt.setInt(4, timeTakenMs);
                stmt.setString(5, algorithm);
                stmt.setInt(6, timeTakenMs);
                stmt.executeUpdate();
            }

            // Auto-reset recognized flags if all 92 solutions are found
            if (getRecognizedSolutionCount() >= 92) {
                resetRecognizedFlags();
                System.out.println("✔ All 92 solutions identified. Flags reset.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- COUNT RECOGNIZED SOLUTIONS ----------------
    public static int getRecognizedSolutionCount() {
        String sql = "SELECT COUNT(*) FROM solutions WHERE recognized=TRUE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ---------------- RESET ALL RECOGNIZED FLAGS ----------------
    public static void resetRecognizedFlags() {
        String sql = "UPDATE solutions SET recognized=FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- BOARD TO STRING ----------------
    public static String boardToString(int[] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            sb.append(board[i]);
            if (i < board.length - 1) sb.append(",");
        }
        return sb.toString();
    }

    // ---------------- GET ALL RECOGNIZED SOLUTIONS ----------------
    public static List<String> getAllRecognizedSolutions() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT solution FROM solutions WHERE recognized=TRUE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) list.add(rs.getString("solution"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
