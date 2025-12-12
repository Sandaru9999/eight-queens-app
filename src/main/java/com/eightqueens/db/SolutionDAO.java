package com.eightqueens.db;
import java.sql.Connection;
import java.sql.PreparedStatement;
public class SolutionDAO {
 // Save to players table
 public static void savePlayerSolution(String playerName, String solution) {
 String sql = "INSERT INTO players (name, solution) VALUES (?, ?)";
 try (Connection conn = DatabaseConnection.getConnection();
 PreparedStatement stmt = conn.prepareStatement(sql)) {
 stmt.setString(1, playerName);
 stmt.setString(2, solution);
 stmt.executeUpdate();
 System.out.println("Player solution saved!");
 } catch (Exception e) {
 e.printStackTrace();
 }
 }
 // Save to solutions table
 public static void saveSolution(String solution, int timeTakenMs) {
 String sql = "INSERT INTO solutions (solution, timeTakenMs) VALUES (?, ?)";
 try (Connection conn = DatabaseConnection.getConnection();
 PreparedStatement stmt = conn.prepareStatement(sql)) {
 stmt.setString(1, solution);
 stmt.setInt(2, timeTakenMs);
 stmt.executeUpdate();
 System.out.println("Solution saved!");
 } catch (Exception e) {
 e.printStackTrace();
 }
 }
 // Convenience method: save both solution and player at the same time
 public static void savePlayerAndSolution(String playerName, String solution, int timeTakenMs) {
 saveSolution(solution, timeTakenMs); // Save to solutions table
 savePlayerSolution(playerName, solution); // Save to players table
 }
 // Helper to convert board array to string
 public static String boardToString(int[] board) {
 StringBuilder sb = new StringBuilder();
 for (int i = 0; i < board.length; i++) {
 sb.append(board[i]);
 if (i < board.length - 1) sb.append(",");
 }
 return sb.toString();
 }
}