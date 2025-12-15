package com.snakeandladder.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class BoardGenerator {
   private final int N;
   private final int size;
   private final Random rand = new Random();
   private final Set<Integer> occupiedCells = new HashSet();

   public BoardGenerator(int N) {
      this.N = N;
      this.size = N * N;
      this.occupiedCells.add(1);
      this.occupiedCells.add(this.size);
   }

   public Map<Integer, Integer> generateLadders() {
      Map<Integer, Integer> ladders = new HashMap();
      int numLadders = this.N - 2;

      while(ladders.size() < numLadders) {
         int start;
         int end;
         do {
            start = 2 + this.rand.nextInt(this.size - 3);
            end = start + 1 + this.rand.nextInt(this.size - start - 1);
         } while(this.occupiedCells.contains(start) || this.occupiedCells.contains(end));

         ladders.put(start, end);
         this.occupiedCells.add(start);
         this.occupiedCells.add(end);
      }

      return ladders;
   }

   public Map<Integer, Integer> generateSnakes() {
      Map<Integer, Integer> snakes = new HashMap();
      int numSnakes = this.N - 2;

      while(snakes.size() < numSnakes) {
         int start;
         int end;
         do {
            start = 3 + this.rand.nextInt(this.size - 3);
            end = 1 + this.rand.nextInt(start - 2);
         } while(this.occupiedCells.contains(start) || this.occupiedCells.contains(end));

         snakes.put(start, end);
         this.occupiedCells.add(start);
         this.occupiedCells.add(end);
      }

      return snakes;
   }
}

