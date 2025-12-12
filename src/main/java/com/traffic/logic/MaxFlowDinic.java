package com.traffic.logic;

import java.util.*;

public class MaxFlowDinic {

    private int[][] capacity;
    private int n;

    public MaxFlowDinic(int[][] capacity) {
        this.capacity = capacity;
        this.n = capacity.length;
    }

    public int maxFlow(int s, int t) {
        int flow = 0;
        int[][] residual = new int[n][n];
        for (int i = 0; i < n; i++)
            System.arraycopy(capacity[i], 0, residual[i], 0, n);

        while (true) {
            int[] level = new int[n];
            Arrays.fill(level, -1);
            if (!bfs(residual, s, t, level)) break;

            int[] start = new int[n];
            int pushed;
            while ((pushed = dfs(residual, s, t, Integer.MAX_VALUE, start, level)) > 0) {
                flow += pushed;
            }
        }
        return flow;
    }

    private boolean bfs(int[][] residual, int s, int t, int[] level) {
        Queue<Integer> q = new LinkedList<>();
        q.add(s);
        level[s] = 0;

        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v = 0; v < n; v++) {
                if (level[v] < 0 && residual[u][v] > 0) {
                    level[v] = level[u] + 1;
                    q.add(v);
                }
            }
        }
        return level[t] >= 0;
    }

    private int dfs(int[][] residual, int u, int t, int flow, int[] start, int[] level) {
        if (u == t) return flow;
        for (; start[u] < n; start[u]++) {
            int v = start[u];
            if (level[v] == level[u] + 1 && residual[u][v] > 0) {
                int currFlow = Math.min(flow, residual[u][v]);
                int tempFlow = dfs(residual, v, t, currFlow, start, level);
                if (tempFlow > 0) {
                    residual[u][v] -= tempFlow;
                    residual[v][u] += tempFlow;
                    return tempFlow;
                }
            }
        }
        return 0;
    }
}
