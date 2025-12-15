package com.tsp.models;

public class TSPSolution {
    private String playerName;
    private char homeCity;
    private String selectedCities;
    private String route;
    private int totalDistance;
    private long timeTakenMs;
    private String algorithm;
    private boolean correct; // new field

    public TSPSolution(String playerName, char homeCity, String selectedCities,
                       String route, int totalDistance, long timeTakenMs, String algorithm) {
        this.playerName = playerName;
        this.homeCity = homeCity;
        this.selectedCities = selectedCities;
        this.route = route;
        this.totalDistance = totalDistance;
        this.timeTakenMs = timeTakenMs;
        this.algorithm = algorithm;
        this.correct = false;
    }

    // Getters
    public String getPlayerName() { return playerName; }
    public char getHomeCity() { return homeCity; }
    public String getSelectedCities() { return selectedCities; }
    public String getRoute() { return route; }
    public int getTotalDistance() { return totalDistance; }
    public long getTimeTakenMs() { return timeTakenMs; }
    public String getAlgorithm() { return algorithm; }
    public boolean isCorrect() { return correct; }

    // Setters
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setHomeCity(char homeCity) { this.homeCity = homeCity; }
    public void setSelectedCities(String selectedCities) { this.selectedCities = selectedCities; }
    public void setRoute(String route) { this.route = route; }
    public void setTotalDistance(int totalDistance) { this.totalDistance = totalDistance; }
    public void setTimeTakenMs(long timeTakenMs) { this.timeTakenMs = timeTakenMs; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
    public void setCorrect(boolean correct) { this.correct = correct; }

    @Override
    public String toString() {
        return "Route: " + route + " | Distance: " + totalDistance + " km | Algorithm: " + algorithm;
    }
}
