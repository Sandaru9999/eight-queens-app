package com.snakeandladder.models;

import java.util.Map;

public class SnakeLadderSolution {

    private String playerName;
    private int boardSize;
    private Map<Integer, Integer> snakes;
    private Map<Integer, Integer> ladders;
    private int playerAnswer;
    private int correctThrows;
    private long timeTakenMs;

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getBoardSize() { return boardSize; }
    public void setBoardSize(int boardSize) { this.boardSize = boardSize; }

    public Map<Integer, Integer> getSnakes() { return snakes; }
    public void setSnakes(Map<Integer, Integer> snakes) { this.snakes = snakes; }

    public Map<Integer, Integer> getLadders() { return ladders; }
    public void setLadders(Map<Integer, Integer> ladders) { this.ladders = ladders; }

    public int getPlayerAnswer() { return playerAnswer; }
    public void setPlayerAnswer(int playerAnswer) { this.playerAnswer = playerAnswer; }

    public int getCorrectThrows() { return correctThrows; }
    public void setCorrectThrows(int correctThrows) { this.correctThrows = correctThrows; }

    public long getTimeTakenMs() { return timeTakenMs; }
    public void setTimeTakenMs(long timeTakenMs) { this.timeTakenMs = timeTakenMs; }
}
