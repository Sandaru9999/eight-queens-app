package com.towerofhanoi.models;

public class HanoiSolution {
    private String playerName;
    private int numDisks;
    private int numPegs;
    private String movesSequence;
    private long timeTakenMs;
    private String algorithm;

    public HanoiSolution() {}

    public HanoiSolution(String playerName, int numDisks, int numPegs, String movesSequence, long timeTakenMs, String algorithm) {
        this.playerName = playerName;
        this.numDisks = numDisks;
        this.numPegs = numPegs;
        this.movesSequence = movesSequence;
        this.timeTakenMs = timeTakenMs;
        this.algorithm = algorithm;
    }

    // getters & setters...
    public String getPlayerName() { return playerName; }
    public int getNumDisks() { return numDisks; }
    public int getNumPegs() { return numPegs; }
    public String getMovesSequence() { return movesSequence; }
    public long getTimeTakenMs() { return timeTakenMs; }
    public String getAlgorithm() { return algorithm; }

    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setNumDisks(int numDisks) { this.numDisks = numDisks; }
    public void setNumPegs(int numPegs) { this.numPegs = numPegs; }
    public void setMovesSequence(String movesSequence) { this.movesSequence = movesSequence; }
    public void setTimeTakenMs(long timeTakenMs) { this.timeTakenMs = timeTakenMs; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
}
