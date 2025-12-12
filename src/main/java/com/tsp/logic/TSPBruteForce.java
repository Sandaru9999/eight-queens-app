package com.tsp.logic;

import com.tsp.models.TSPSolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TSPBruteForce {

    private int[][] distMatrix;
    private char home;
    private List<Character> selectedCities;
    private char[] cityMap = {'A','B','C','D','E','F','G','H','I','J'};

    public TSPBruteForce(int[][] distMatrix, char home, List<Character> selectedCities) {
        this.distMatrix = distMatrix;
        this.home = home;
        this.selectedCities = new ArrayList<>(selectedCities);
    }

    public TSPSolution solve() {
        List<List<Character>> permutations = permute(selectedCities);
        int minDist = Integer.MAX_VALUE;
        List<Character> bestRoute = null;

        for (List<Character> perm : permutations) {
            int dist = distance(home, perm);
            if (dist < minDist) {
                minDist = dist;
                bestRoute = perm;
            }
        }

        // Build route string
        StringBuilder sb = new StringBuilder();
        sb.append(home).append("→");
        for (char c : bestRoute) sb.append(c).append("→");
        sb.append(home);

        return new TSPSolution("Player", home, listToString(selectedCities), sb.toString(), minDist, 0, "Brute Force");
    }

    private int distance(char start, List<Character> route) {
        int total = 0;
        char prev = start;
        for (char c : route) {
            total += distMatrix[prev - 'A'][c - 'A'];
            prev = c;
        }
        total += distMatrix[prev - 'A'][start - 'A'];
        return total;
    }

    private List<List<Character>> permute(List<Character> cities) {
        List<List<Character>> result = new ArrayList<>();
        permuteHelper(cities, 0, result);
        return result;
    }

    private void permuteHelper(List<Character> arr, int index, List<List<Character>> result) {
        if (index == arr.size()) {
            result.add(new ArrayList<>(arr));
        } else {
            for (int i = index; i < arr.size(); i++) {
                Collections.swap(arr, i, index);
                permuteHelper(arr, index + 1, result);
                Collections.swap(arr, i, index);
            }
        }
    }

    private String listToString(List<Character> list) {
        StringBuilder sb = new StringBuilder();
        for (char c : list) sb.append(c);
        return sb.toString();
    }
}
