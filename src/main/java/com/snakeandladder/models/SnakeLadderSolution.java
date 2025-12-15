// package com.snakeandladder.models;

// import java.util.Map;

// public class SnakeLadderSolution {

//     private String playerName;
//     private int boardSize;
//     private Map<Integer, Integer> snakes;
//     private Map<Integer, Integer> ladders;
//     private int playerAnswer;
//     private int correctThrows;
//     private long timeTakenMs;

//     public String getPlayerName() { return playerName; }
//     public void setPlayerName(String playerName) { this.playerName = playerName; }

//     public int getBoardSize() { return boardSize; }
//     public void setBoardSize(int boardSize) { this.boardSize = boardSize; }

//     public Map<Integer, Integer> getSnakes() { return snakes; }
//     public void setSnakes(Map<Integer, Integer> snakes) { this.snakes = snakes; }

//     public Map<Integer, Integer> getLadders() { return ladders; }
//     public void setLadders(Map<Integer, Integer> ladders) { this.ladders = ladders; }

//     public int getPlayerAnswer() { return playerAnswer; }
//     public void setPlayerAnswer(int playerAnswer) { this.playerAnswer = playerAnswer; }

//     public int getCorrectThrows() { return correctThrows; }
//     public void setCorrectThrows(int correctThrows) { this.correctThrows = correctThrows; }

//     public long getTimeTakenMs() { return timeTakenMs; }
//     public void setTimeTakenMs(long timeTakenMs) { this.timeTakenMs = timeTakenMs; }
// }


// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package com.snakeandladder.models;

import java.util.List;
import java.util.Map;

public class SnakeLadderSolution {
   private String playerName;
   private int boardSize;
   private int correctMinThrows;
   private int playerAnswer;
   private long algo1TimeNanos;
   private long algo2TimeNanos;
   private Map<Integer, Integer> snakes;
   private Map<Integer, Integer> ladders;
   private List<Integer> optimalPath;

   public SnakeLadderSolution() {
   }

   public void setPlayerName(String playerName) {
      this.playerName = playerName;
   }

   public String getPlayerName() {
      return this.playerName;
   }

   public void setBoardSize(int boardSize) {
      this.boardSize = boardSize;
   }

   public int getBoardSize() {
      return this.boardSize;
   }

   public void setCorrectMinThrows(int correctMinThrows) {
      this.correctMinThrows = correctMinThrows;
   }

   public int getCorrectMinThrows() {
      return this.correctMinThrows;
   }

   public void setPlayerAnswer(int playerAnswer) {
      this.playerAnswer = playerAnswer;
   }

   public int getPlayerAnswer() {
      return this.playerAnswer;
   }

   public void setAlgo1TimeNanos(long algo1TimeNanos) {
      this.algo1TimeNanos = algo1TimeNanos;
   }

   public long getAlgo1TimeNanos() {
      return this.algo1TimeNanos;
   }

   public void setAlgo2TimeNanos(long algo2TimeNanos) {
      this.algo2TimeNanos = algo2TimeNanos;
   }

   public long getAlgo2TimeNanos() {
      return this.algo2TimeNanos;
   }

   public void setSnakes(Map<Integer, Integer> snakes) {
      this.snakes = snakes;
   }

   public Map<Integer, Integer> getSnakes() {
      return this.snakes;
   }

   public void setLadders(Map<Integer, Integer> ladders) {
      this.ladders = ladders;
   }

   public Map<Integer, Integer> getLadders() {
      return this.ladders;
   }

   public List<Integer> getOptimalPath() {
      return this.optimalPath;
   }

   public void setOptimalPath(List<Integer> optimalPath) {
      this.optimalPath = optimalPath;
   }
}
