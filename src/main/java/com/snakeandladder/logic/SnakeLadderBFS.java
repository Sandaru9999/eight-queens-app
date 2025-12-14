package com.snakeandladder.logic;

import java.util.*;

public class SnakeLadderBFS {

    private int N;
    private Map<Integer, Integer> snakes;
    private Map<Integer, Integer> ladders;
    
    private List<Integer> optimalPath; 

    public SnakeLadderBFS(int N, Map<Integer, Integer> snakes, Map<Integer, Integer> ladders) {
        this.N = N;
        this.snakes = snakes;
        this.ladders = ladders;
        this.optimalPath = new ArrayList<>();
    }

    
    public Map<String, Long> solveAndMeasureTime() {
    
        long startTime = System.nanoTime(); 
        int minThrows = minDiceThrows();
        long endTime = System.nanoTime();
    
        long timeNanos = endTime - startTime; 

        Map<String, Long> result = new HashMap<>();
        result.put("minThrows", (long) minThrows);
        result.put("timeNanos", timeNanos);
        return result;
    }

    
    private int minDiceThrows() {
        int size = N * N;
        boolean[] visited = new boolean[size + 1];
        
       
        Map<Integer, Integer> predecessorMap = new HashMap<>(); 

        
        Queue<int[]> q = new LinkedList<>(); 
        q.add(new int[]{1, 0}); 
        visited[1] = true;
        predecessorMap.put(1, 0); 

        while (!q.isEmpty()) {
            int[] curr = q.poll();
            int pos = curr[0];
            int dist = curr[1];
            
            if (pos == size) {
                
                reconstructPath(predecessorMap, size); 
                return dist; 
            } 

            
            for (int dice = 1; dice <= 6; dice++) {
                int next = pos + dice;
                if (next > size) continue;
                
                
                if (ladders.containsKey(next)) {
                    next = ladders.get(next); 
                } else if (snakes.containsKey(next)) {
                    next = snakes.get(next); 
                }
                
                
                if (!visited[next]) {
                    visited[next] = true;
                    
                    predecessorMap.put(next, pos); 
                    
                    q.add(new int[]{next, dist + 1});
                }
            }
        }
        
        return -1; 
    }

    private void reconstructPath(Map<Integer, Integer> predecessorMap, int target) {
        LinkedList<Integer> path = new LinkedList<>();
        Integer current = target;
        
        while (current != null && current != 0) {
            path.addFirst(current);
            current = predecessorMap.get(current);
        }
        
        this.optimalPath = path;
    }

    public List<Integer> getOptimalPath() {
        return optimalPath;
    }
}