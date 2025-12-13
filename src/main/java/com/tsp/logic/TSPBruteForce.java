package com.tsp.logic;

import com.tsp.models.TSPSolution;
import java.util.*;

public class TSPBruteForce {

    private int[][] distMatrix;
    private char home;
    private List<Character> selectedCities;

    public TSPBruteForce(int[][] distMatrix, char home, List<Character> selectedCities) {
        this.distMatrix = distMatrix;
        this.home = home;
        this.selectedCities = new ArrayList<>(selectedCities);
    }

    public TSPSolution solve() {

        if (selectedCities.isEmpty())
            throw new IllegalArgumentException("No cities selected");

        List<List<Character>> perms = new ArrayList<>();
        permute(selectedCities, 0, perms);

        int min = Integer.MAX_VALUE;
        List<Character> best = null;

        for (List<Character> p : perms) {
            int d = distance(p);
            if (d < min) {
                min = d;
                best = new ArrayList<>(p);
            }
        }

        if (best == null)
            throw new RuntimeException("No route found");

        StringBuilder route = new StringBuilder();
        route.append(home).append("→");
        for (char c : best) route.append(c).append("→");
        route.append(home);

        return new TSPSolution(
                "Player",
                home,
                listToString(selectedCities),
                route.toString(),
                min,
                0,
                "Brute Force"
        );
    }

    private int distance(List<Character> route) {
        int total = 0;
        char prev = home;
        for (char c : route) {
            total += distMatrix[prev - 'A'][c - 'A'];
            prev = c;
        }
        return total + distMatrix[prev - 'A'][home - 'A'];
    }

    private void permute(List<Character> arr, int k, List<List<Character>> res) {
        if (k == arr.size()) res.add(new ArrayList<>(arr));
        else {
            for (int i = k; i < arr.size(); i++) {
                Collections.swap(arr, i, k);
                permute(arr, k + 1, res);
                Collections.swap(arr, i, k);
            }
        }
    }

    private String listToString(List<Character> list) {
        StringBuilder sb = new StringBuilder();
        for (char c : list) sb.append(c);
        return sb.toString();
    }
}
