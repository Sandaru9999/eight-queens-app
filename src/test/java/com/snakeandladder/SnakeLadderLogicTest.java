package com.snakeandladder; 

import com.snakeandladder.logic.SnakeLadderBFS; 
import com.snakeandladder.logic.SnakeLadderDP; 

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class SnakeLadderLogicTest {

    private Map<Integer, Integer> fixedLadders;
    private Map<Integer, Integer> fixedSnakes;
    private final int N = 6; 

    @Before
    public void setUp() {
        
        fixedLadders = new HashMap<>();
        fixedLadders.put(2, 15);
        fixedLadders.put(18, 32);

        
        fixedSnakes = new HashMap<>();
        fixedSnakes.put(30, 10);
        fixedSnakes.put(24, 4);
        
        
    }

    @Test
    public void testBFSSolver_FindsCorrectMinThrows() {
        SnakeLadderBFS bfsSolver = new SnakeLadderBFS(N, fixedSnakes, fixedLadders);
        int minThrows = bfsSolver.solveAndMeasureTime().get("minThrows").intValue();
        
        
        assertEquals("BFS should find the minimum 3 throws.", 3, minThrows);
    }
    
    @Test
    public void testDPSolver_FindsCorrectMinThrows() {
        SnakeLadderDP dpSolver = new SnakeLadderDP(N, fixedSnakes, fixedLadders);
        int minThrows = dpSolver.solveAndMeasureTime().get("minThrows").intValue();

        
        assertEquals("DP should find the minimum 3 throws.", 3, minThrows);
    }

    @Test
    public void testUnreachableGoal() {
        
        
        Map<Integer, Integer> impossibleSnakes = new HashMap<>();
        impossibleSnakes.put(35, 1);
        
        
        SnakeLadderBFS bfsSolver = new SnakeLadderBFS(N, impossibleSnakes, new HashMap<>());
        int bfsThrows = bfsSolver.solveAndMeasureTime().get("minThrows").intValue();
        
        
        SnakeLadderDP dpSolver = new SnakeLadderDP(N, impossibleSnakes, new HashMap<>());
        int dpThrows = dpSolver.solveAndMeasureTime().get("minThrows").intValue();

        
        
        assertEquals("BFS should find the minimum 6 throws for this specific board.", 6, bfsThrows);
        
        
        assertEquals("DP should find the minimum 6 throws for this specific board.", 6, dpThrows);
    }
}