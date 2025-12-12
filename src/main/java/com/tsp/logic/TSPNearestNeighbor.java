package com.tsp.logic;

import com.tsp.models.TSPSolution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        // Return to home
        totalDist += distMatrix[current - 'A'][home - 'A'];

        // Build route string
        StringBuilder sb = new StringBuilder();
        sb.append(home).append("→");
        for (char c : route) sb.append(c).append("→");
        sb.append(home);

        return new TSPSolution("Player", home, listToString(selectedCities), sb.toString(), totalDist, 0, "Nearest Neighbor");
    }

    private String listToString(List<Character> list) {
        StringBuilder sb = new StringBuilder();
        for (char c : list) sb.append(c);
        return sb.toString();
    }
}
