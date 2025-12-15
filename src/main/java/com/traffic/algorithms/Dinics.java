package com.traffic.algorithms;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Arrays;

public class Dinics {
    private static final int INF = Integer.MAX_VALUE;
    private int V; 
    private int[][] capacity; 
    private int[] level; 

    public Dinics(int V, int[][] initialCapacity) {
        this.V = V;
        this.level = new int[V];
        
       
        this.capacity = new int[V][V];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                this.capacity[i][j] = initialCapacity[i][j]; 
            }
        }
    }

    private boolean bfs(int s, int t) {
       
        Arrays.fill(level, -1);
        level[s] = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v = 0; v < V; v++) {
               
                if (level[v] == -1 && capacity[u][v] > 0) {
                    level[v] = level[u] + 1;
                    queue.add(v);
                }
            }
        }
        
        return level[t] != -1;
    }

    
    private int dfs(int u, int t, int flow) {
        if (flow == 0 || u == t) {
            return flow;
        }
        
       
        int pushedOutFlow = 0; 

        
        for (int v = 0; v < V; v++) { 
           
            if (level[v] == level[u] + 1 && capacity[u][v] > 0) {
                
                
                int currentPushed = dfs(v, t, Math.min(flow - pushedOutFlow, capacity[u][v]));
                
                if (currentPushed > 0) {
                    
                    capacity[u][v] -= currentPushed; 
                    capacity[v][u] += currentPushed; 
                    
                    
                    pushedOutFlow += currentPushed;
                    
                    
                    if (pushedOutFlow == flow) {
                        break; 
                    }
                }
            }
        }
        
        
        if (pushedOutFlow == 0) {
            level[u] = -1;
        }

        return pushedOutFlow;
    }

   
    public static int maxFlow(int[][] initialCapacity, int s, int t) {
        int V = initialCapacity.length;
        
       
        Dinics dinics = new Dinics(V, initialCapacity); 
        
        int maxFlow = 0;
        
        
        while (dinics.bfs(s, t)) {
            
            
            int pushed;
            while ((pushed = dinics.dfs(s, t, INF)) > 0) { 
                maxFlow += pushed;
            }
        }
        return maxFlow;
    }
}
