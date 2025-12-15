package com.traffic.algorithms;

import java.util.LinkedList;
import java.util.Queue;

public class EdmondsKarp {

   
    private static boolean bfs(int[][] residualGraph, int[] parent, int s, int t) {
        int V = residualGraph.length;
        boolean[] visited = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();

        queue.add(s);
        visited[s] = true;
        parent[s] = -1;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v = 0; v < V; v++) {
                
                if (!visited[v] && residualGraph[u][v] > 0) {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                    if (v == t) return true; 
                }
            }
        }
        return false;
    }

  
    public static int maxFlow(int[][] capacity, int s, int t) {
        int V = capacity.length;
        
      
        int[][] residualGraph = new int[V][V];
        for (int i = 0; i < V; i++) {
            System.arraycopy(capacity[i], 0, residualGraph[i], 0, V);
        }

        int[] parent = new int[V]; 
        int max_flow = 0;

        
        while (bfs(residualGraph, parent, s, t)) {
           
            int path_flow = Integer.MAX_VALUE;
            for (int v = t; v != s; v = parent[v]) {
                int u = parent[v];
                path_flow = Math.min(path_flow, residualGraph[u][v]);
            }

            
            for (int v = t; v != s; v = parent[v]) {
                int u = parent[v];
                residualGraph[u][v] -= path_flow; 
                residualGraph[v][u] += path_flow; 
            }

           
            max_flow += path_flow;
        }
         
        return max_flow;
    }
}
