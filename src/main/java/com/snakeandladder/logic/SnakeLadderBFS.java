// package com.snakeandladder.logic;

// import java.util.*;

// public class SnakeLadderBFS {

//     private int N;
//     private Map<Integer, Integer> snakes;
//     private Map<Integer, Integer> ladders;

//     public SnakeLadderBFS(int N, Map<Integer, Integer> snakes, Map<Integer, Integer> ladders) {
//         this.N = N;
//         this.snakes = snakes;
//         this.ladders = ladders;
//     }

//     public int minDiceThrows() {
//         int size = N * N;
//         boolean[] visited = new boolean[size + 1];
//         Queue<int[]> q = new LinkedList<>();
//         q.add(new int[]{1, 0});
//         visited[1] = true;

//         while (!q.isEmpty()) {
//             int[] curr = q.poll();
//             int pos = curr[0], dist = curr[1];
//             if (pos == size) return dist;

//             for (int dice = 1; dice <= 6; dice++) {
//                 int next = pos + dice;
//                 if (next > size) continue;
//                 if (ladders.containsKey(next)) next = ladders.get(next);
//                 if (snakes.containsKey(next)) next = snakes.get(next);
//                 if (!visited[next]) {
//                     visited[next] = true;
//                     q.add(new int[]{next, dist + 1});
//                 }
//             }
//         }
//         return -1;
//     }
// }

package com.snakeandladder.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class SnakeLadderBFS {
   private int N;
   private Map<Integer, Integer> snakes;
   private Map<Integer, Integer> ladders;
   private List<Integer> optimalPath;

   public SnakeLadderBFS(int N, Map<Integer, Integer> snakes, Map<Integer, Integer> ladders) {
      this.N = N;
      this.snakes = snakes;
      this.ladders = ladders;
      this.optimalPath = new ArrayList();
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
      boolean[] visited = new boolean[size + 1];
      Map<Integer, Integer> predecessorMap = new HashMap();
      Queue<int[]> q = new LinkedList();
      q.add(new int[]{1, 0});
      visited[1] = true;
      predecessorMap.put(1, 0);

      while(!q.isEmpty()) {
         int[] curr = (int[])q.poll();
         int pos = curr[0];
         int dist = curr[1];
         if (pos == size) {
            this.reconstructPath(predecessorMap, size);
            return dist;
         }

         for(int dice = 1; dice <= 6; ++dice) {
            int next = pos + dice;
            if (next <= size) {
               if (this.ladders.containsKey(next)) {
                  next = (Integer)this.ladders.get(next);
               } else if (this.snakes.containsKey(next)) {
                  next = (Integer)this.snakes.get(next);
               }

               if (!visited[next]) {
                  visited[next] = true;
                  predecessorMap.put(next, pos);
                  q.add(new int[]{next, dist + 1});
               }
            }
         }
      }

      return -1;
   }

   private void reconstructPath(Map<Integer, Integer> predecessorMap, int target) {
      LinkedList<Integer> path = new LinkedList();

      for(Integer current = target; current != null && current != 0; current = (Integer)predecessorMap.get(current)) {
         path.addFirst(current);
      }

      this.optimalPath = path;
   }

   public List<Integer> getOptimalPath() {
      return this.optimalPath;
   }
}
