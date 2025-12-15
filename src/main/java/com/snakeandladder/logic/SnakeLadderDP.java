// package com.snakeandladder.logic;

// import java.util.Map;

// public class SnakeLadderDP {

//     private int N;
//     private Map<Integer, Integer> snakes;
//     private Map<Integer, Integer> ladders;

//     public SnakeLadderDP(int N, Map<Integer, Integer> snakes, Map<Integer, Integer> ladders) {
//         this.N = N;
//         this.snakes = snakes;
//         this.ladders = ladders;
//     }

//     public int minDiceThrows() {
//         int size = N * N;
//         int[] dp = new int[size + 1];
//         for (int i = 1; i <= size; i++) dp[i] = Integer.MAX_VALUE;
//         dp[1] = 0;

//         for (int i = 1; i <= size; i++) {
//             for (int dice = 1; dice <= 6; dice++) {
//                 int next = i + dice;
//                 if (next > size) continue;
//                 if (ladders.containsKey(next)) next = ladders.get(next);
//                 if (snakes.containsKey(next)) next = snakes.get(next);
//                 dp[next] = Math.min(dp[next], dp[i] + 1);
//             }
//         }
//         return dp[size];
//     }
// }


package com.snakeandladder.logic;

import java.util.Arrays;
import java.util.HashMap;
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

   public Map<String, Long> solveAndMeasureTime() {
      long startTime = System.nanoTime();
      int minThrows = this.minDiceThrows();
      long endTime = System.nanoTime();
      long timeNanos = endTime - startTime;
      Map<String, Long> result = new HashMap();
      result.put("minThrows", (long)minThrows);
      result.put("timeNanos", timeNanos);
      return result;
   }

   private int minDiceThrows() {
      int size = this.N * this.N;
      int[] dp = new int[size + 1];
      Arrays.fill(dp, Integer.MAX_VALUE);
      dp[1] = 0;

      for(int i = 1; i < size; ++i) {
         if (dp[i] != Integer.MAX_VALUE) {
            for(int dice = 1; dice <= 6; ++dice) {
               int next = i + dice;
               if (next <= size) {
                  if (this.ladders.containsKey(next)) {
                     next = (Integer)this.ladders.get(next);
                  } else if (this.snakes.containsKey(next)) {
                     next = (Integer)this.snakes.get(next);
                  }

                  if (dp[i] + 1 < dp[next]) {
                     dp[next] = dp[i] + 1;
                  }
               }
            }
         }
      }

      return dp[size];
   }
}

