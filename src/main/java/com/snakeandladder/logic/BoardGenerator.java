package com.snakeandladder.logic;

import java.util.*;


public class BoardGenerator {

    private final int N;
    private final int size;
    private final Random rand = new Random();
    
    private final Set<Integer> occupiedCells = new HashSet<>(); 

    public BoardGenerator(int N) {
        this.N = N;
        this.size = N * N;
        
        occupiedCells.add(1); 
        occupiedCells.add(size);
    }

    
    public Map<Integer, Integer> generateLadders() {
        Map<Integer, Integer> ladders = new HashMap<>();
       
        int numLadders = N - 2; 

        while (ladders.size() < numLadders) {
            int start, end;
            do {
                
                start = 2 + rand.nextInt(size - 3); 
                
                end = start + 1 + rand.nextInt(size - start - 1); 
            } while (occupiedCells.contains(start) || occupiedCells.contains(end));

            ladders.put(start, end);
            occupiedCells.add(start);
            occupiedCells.add(end);
        }
        return ladders;
    }

    
    public Map<Integer, Integer> generateSnakes() {
        Map<Integer, Integer> snakes = new HashMap<>();
        
        int numSnakes = N - 2;

        while (snakes.size() < numSnakes) {
            int start, end;
            do {
                
                start = 3 + rand.nextInt(size - 3);
                
                end = 1 + rand.nextInt(start - 2); 
            } while (occupiedCells.contains(start) || occupiedCells.contains(end));
            
            snakes.put(start, end);
            occupiedCells.add(start);
            occupiedCells.add(end);
        }
        return snakes;
    }
}