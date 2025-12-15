package com.traffic.models;

public class TrafficSolution {

    private String playerName;
    private int playerAnswer;
    private int correctFlow;
    private long timeTakenMs;

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getPlayerAnswer() { return playerAnswer; }
    public void setPlayerAnswer(int playerAnswer) { this.playerAnswer = playerAnswer; }

    public int getCorrectFlow() { return correctFlow; }
    public void setCorrectFlow(int correctFlow) { this.correctFlow = correctFlow; }

    public long getTimeTakenMs() { return timeTakenMs; }
    public void setTimeTakenMs(long timeTakenMs) { this.timeTakenMs = timeTakenMs; }
    
}
