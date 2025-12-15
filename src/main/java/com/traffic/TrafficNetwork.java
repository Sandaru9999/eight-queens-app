package com.traffic;

import java.util.Random;

public class TrafficNetwork {
    private final int V = 10; 
    private int[][] capacity; 
    public static final String[] NODE_NAMES = {"A", "B", "C", "D", "E", "F", "G", "H", "T", "X"}; 
    
   
    private static final int[][] EDGES = {
        {0, 1}, {0, 2}, {0, 3}, 
        {1, 4}, {1, 5},         
        {2, 4}, {2, 5},         
        {3, 5},                 
        {4, 6}, {4, 7},         
        {5, 7},                 
        {6, 8},                 
        {7, 8}                  
    };

   
    public TrafficNetwork() {
        capacity = new int[V][V];
        Random rand = new Random();

        for (int[] edge : EDGES) {
            int u = edge[0];
            int v = edge[1];
           
            int cap = rand.nextInt(11) + 5; 
            capacity[u][v] = cap;
        }
    }
    
    public int[][] getCapacity() {
        return capacity;
    }
    
    
    public static int nameToIndex(char name) {
        return switch (name) {
            case 'A' -> 0; case 'B' -> 1; case 'C' -> 2; case 'D' -> 3;
            case 'E' -> 4; case 'F' -> 5; case 'G' -> 6; case 'H' -> 7;
            case 'T' -> 8; default -> -1;
        };
    }
}
