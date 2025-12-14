package com.snakeandladder.logic;

import java.util.Map;

public class SnakeLadderDP {

    private int N;
    private Map<Integer, Integer> snakes;
    private Map<Integer, Integer> ladders;

    public SnakeLadderDP(int N, Map<Integer, Integer> snakes, Map<Integer, Integer> ladders) {
        this.N = N;
        this.snakes = snakes;
        this.ladders = ladders;
    }

    public int minDiceThrows() {
        int size = N * N;
        int[] dp = new int[size + 1];
        for (int i = 1; i <= size; i++) dp[i] = Integer.MAX_VALUE;
        dp[1] = 0;

        for (int i = 1; i <= size; i++) {
            for (int dice = 1; dice <= 6; dice++) {
                int next = i + dice;
                if (next > size) continue;
                if (ladders.containsKey(next)) next = ladders.get(next);
                if (snakes.containsKey(next)) next = snakes.get(next);
                dp[next] = Math.min(dp[next], dp[i] + 1);
            }
        }
        return dp[size];
    }
}
