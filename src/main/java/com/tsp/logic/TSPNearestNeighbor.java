package com.tsp.logic;

import com.tsp.models.TSPSolution;
import com.tsp.db.TSPDAO;

import java.util.*;

public class TSPNearestNeighbor {

    private int[][] distMatrix;
    private char home;
    private List<Character> selectedCities;

    public TSPNearestNeighbor(int[][] distMatrix, char home, List<Character> selectedCities) {
        this.distMatrix = distMatrix;
        this.home = home;
        this.selectedCities = new ArrayList<>(selectedCities);
    }

    public TSPSolution solve() {
        Set<Character> unvisited = new HashSet<>(selectedCities);
        List<Character> route = new ArrayList<>();
        char current = home;
        int totalDist = 0;

        while (!unvisited.isEmpty()) {
            char nextCity = '-';
            int minDist = Integer.MAX_VALUE;

            for (char city : unvisited) {
                int dist = distMatrix[current - 'A'][city - 'A'];
                if (dist < minDist) {
                    minDist = dist;
                    nextCity = city;
                }
            }

            route.add(nextCity);
            totalDist += minDist;
            unvisited.remove(nextCity);
            current = nextCity;
        }

        totalDist += distMatrix[current - 'A'][home - 'A'];

        StringBuilder sb = new StringBuilder();
        sb.append(home).append("→");
        for (char c : route) sb.append(c).append("→");
        sb.append(home);

        return new TSPSolution("Player", home, listToString(selectedCities), sb.toString(), totalDist, 0, "Nearest Neighbor");
    }

    public TSPSolution solveAndSave(String playerName, int correctDistance) {
        long start = System.nanoTime();
        TSPSolution solution = solve();
        long end = System.nanoTime();
        solution.setTimeTakenMs((end - start) / 1_000_000);

        solution.setPlayerName(playerName);
        solution.setHomeCity(home);
        solution.setSelectedCities(listToString(selectedCities));
        solution.setAlgorithm("Nearest Neighbor");

        solution.setCorrect(solution.getTotalDistance() == correctDistance);
        if (solution.isCorrect()) TSPDAO.savePlayerSolution(solution);

        return solution;
    }

    private String listToString(List<Character> list) {
        StringBuilder sb = new StringBuilder();
        for (char c : list) sb.append(c);
        return sb.toString();
    }
}
