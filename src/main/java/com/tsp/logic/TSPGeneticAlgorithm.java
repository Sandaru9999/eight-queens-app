package com.tsp.logic;

import com.tsp.models.TSPSolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TSPGeneticAlgorithm {

    private int[][] distMatrix;
    private char home;
    private List<Character> selectedCities;
    private int populationSize = 50;
    private int generations = 100;
    private double mutationRate = 0.1;
    private Random rand = new Random();

    public TSPGeneticAlgorithm(int[][] distMatrix, char home, List<Character> selectedCities) {
        this.distMatrix = distMatrix;
        this.home = home;
        this.selectedCities = new ArrayList<>(selectedCities);
    }

    public TSPSolution solve() {
        // Initialize population
        List<List<Character>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            List<Character> individual = new ArrayList<>(selectedCities);
            Collections.shuffle(individual);
            population.add(individual);
        }

        List<Character> bestIndividual = null;
        int bestDistance = Integer.MAX_VALUE;

        for (int gen = 0; gen < generations; gen++) {
            // Evaluate fitness
            List<Integer> fitness = new ArrayList<>();
            for (List<Character> ind : population) {
                fitness.add(totalDistance(ind));
            }

            // Track best
            for (int i = 0; i < populationSize; i++) {
                if (fitness.get(i) < bestDistance) {
                    bestDistance = fitness.get(i);
                    bestIndividual = new ArrayList<>(population.get(i));
                }
            }

            // Selection & crossover
            List<List<Character>> newPopulation = new ArrayList<>();
            for (int i = 0; i < populationSize; i++) {
                List<Character> parent1 = population.get(rand.nextInt(populationSize));
                List<Character> parent2 = population.get(rand.nextInt(populationSize));
                newPopulation.add(crossover(parent1, parent2));
            }

            // Mutation
            for (List<Character> ind : newPopulation) {
                if (rand.nextDouble() < mutationRate) {
                    int idx1 = rand.nextInt(ind.size());
                    int idx2 = rand.nextInt(ind.size());
                    Collections.swap(ind, idx1, idx2);
                }
            }

            population = newPopulation;
        }

        // Build route string
        StringBuilder sb = new StringBuilder();
        sb.append(home).append("→");
        for (char c : bestIndividual) sb.append(c).append("→");
        sb.append(home);

        return new TSPSolution("Player", home, listToString(selectedCities), sb.toString(), bestDistance, 0, "Genetic Algorithm");
    }

    private int totalDistance(List<Character> route) {
        int total = 0;
        char prev = home;
        for (char c : route) {
            total += distMatrix[prev - 'A'][c - 'A'];
            prev = c;
        }
        total += distMatrix[prev - 'A'][home - 'A'];
        return total;
    }

    private List<Character> crossover(List<Character> p1, List<Character> p2) {
        int cut = rand.nextInt(p1.size());
        List<Character> child = new ArrayList<>(p1.subList(0, cut));
        for (char c : p2) if (!child.contains(c)) child.add(c);
        return child;
    }

    private String listToString(List<Character> list) {
        StringBuilder sb = new StringBuilder();
        for (char c : list) sb.append(c);
        return sb.toString();
    }
}
