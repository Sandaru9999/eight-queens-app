
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


public void setPlayerName(String playerName) { this.playerName = playerName; }

public String getPlayerName() { return playerName; }



public void setBoardSize(int boardSize) { this.boardSize = boardSize; }

public int getBoardSize() { return boardSize; }



public void setCorrectMinThrows(int correctMinThrows) { this.correctMinThrows = correctMinThrows; }

public int getCorrectMinThrows() { return correctMinThrows; }



public void setPlayerAnswer(int playerAnswer) { this.playerAnswer = playerAnswer; }

public int getPlayerAnswer() { return playerAnswer; }



public void setAlgo1TimeNanos(long algo1TimeNanos) { this.algo1TimeNanos = algo1TimeNanos; }

public long getAlgo1TimeNanos() { return algo1TimeNanos; }



public void setAlgo2TimeNanos(long algo2TimeNanos) { this.algo2TimeNanos = algo2TimeNanos; }

public long getAlgo2TimeNanos() { return algo2TimeNanos; }



public void setSnakes(Map<Integer, Integer> snakes) { this.snakes = snakes; }

public Map<Integer, Integer> getSnakes() { return snakes; }



public void setLadders(Map<Integer, Integer> ladders) { this.ladders = ladders; }

public Map<Integer, Integer> getLadders() { return ladders; }



public List<Integer> getOptimalPath() { return optimalPath; }

public void setOptimalPath(List<Integer> optimalPath) { this.optimalPath = optimalPath; }

}