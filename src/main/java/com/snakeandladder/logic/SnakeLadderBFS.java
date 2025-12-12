package com.snakeandladder.logic;

import java.util.*;

public class SnakeLadderBFS {

    private int N;
    private Map<Integer, Integer> snakes;
    private Map<Integer, Integer> ladders;

    public SnakeLadderBFS(int N, Map<Integer, Integer> snakes, Map<Integer, Integer> ladders) {
        this.N = N;
        this.snakes = snakes;
        this.ladders = ladders;
    }

    public int minDiceThrows() {
        int size = N * N;
        boolean[] visited = new boolean[size + 1];
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{1, 0});
        visited[1] = true;

        while (!q.isEmpty()) {
            int[] curr = q.poll();
            int pos = curr[0], dist = curr[1];
            if (pos == size) return dist;

            for (int dice = 1; dice <= 6; dice++) {
                int next = pos + dice;
                if (next > size) continue;
                if (ladders.containsKey(next)) next = ladders.get(next);
                if (snakes.containsKey(next)) next = snakes.get(next);
                if (!visited[next]) {
                    visited[next] = true;
                    q.add(new int[]{next, dist + 1});
                }
            }
        }
        return -1;
    }
}
