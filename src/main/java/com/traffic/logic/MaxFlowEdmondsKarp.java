package com.traffic.logic;

import java.util.LinkedList;
import java.util.Queue;

public class MaxFlowEdmondsKarp {

    private int[][] capacity;
    private int n;

    public MaxFlowEdmondsKarp(int[][] capacity) {
        this.capacity = capacity;
        this.n = capacity.length;
    }

    public int maxFlow(int s, int t) {
        int flow = 0;
        int[][] residual = new int[n][n];
        for (int i = 0; i < n; i++)
            System.arraycopy(capacity[i], 0, residual[i], 0, n);

        int[] parent = new int[n];

        while (bfs(residual, s, t, parent)) {
            int pathFlow = Integer.MAX_VALUE;
            int v = t;
            while (v != s) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residual[u][v]);
                v = u;
            }

            v = t;
            while (v != s) {
                int u = parent[v];
                residual[u][v] -= pathFlow;
                residual[v][u] += pathFlow;
                v = u;
            }

            flow += pathFlow;
        }
        return flow;
    }

    private boolean bfs(int[][] residual, int s, int t, int[] parent) {
        boolean[] visited = new boolean[n];
        Queue<Integer> q = new LinkedList<>();
        q.add(s);
        visited[s] = true;
        parent[s] = -1;

        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v = 0; v < n; v++) {
                if (!visited[v] && residual[u][v] > 0) {
                    q.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }
        return visited[t];
    }
}
