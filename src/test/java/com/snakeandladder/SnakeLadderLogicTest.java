package com.snakeandladder;

import com.snakeandladder.logic.SnakeLadderBFS;
import com.snakeandladder.logic.SnakeLadderDP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

public class SnakeLadderLogicTest {

    private Map<Integer, Integer> fixedLadders;
    private Map<Integer, Integer> fixedSnakes;
    private final int N = 6;

    @BeforeEach
    void setUp() {
        fixedLadders = new HashMap<>();
        fixedLadders.put(2, 15);
        fixedLadders.put(18, 32);

        fixedSnakes = new HashMap<>();
        fixedSnakes.put(30, 10);
        fixedSnakes.put(24, 4);
    }

    @Test
    void testBFSSolver_FindsCorrectMinThrows() {
        SnakeLadderBFS bfsSolver =
                new SnakeLadderBFS(N, fixedSnakes, fixedLadders);

        int minThrows =
                ((Long) bfsSolver.solveAndMeasureTime().get("minThrows")).intValue();

        Assertions.assertEquals(3, minThrows,
                "BFS should find the minimum 3 throws.");
    }

    @Test
    void testDPSolver_FindsCorrectMinThrows() {
        SnakeLadderDP dpSolver =
                new SnakeLadderDP(N, fixedSnakes, fixedLadders);

        int minThrows =
                ((Long) dpSolver.solveAndMeasureTime().get("minThrows")).intValue();

        Assertions.assertEquals(3, minThrows,
                "DP should find the minimum 3 throws.");
    }

    @Test
    void testUnreachableGoal() {
        Map<Integer, Integer> impossibleSnakes = new HashMap<>();
        impossibleSnakes.put(35, 1);

        SnakeLadderBFS bfsSolver =
                new SnakeLadderBFS(N, impossibleSnakes, new HashMap<>());

        int bfsThrows =
                ((Long) bfsSolver.solveAndMeasureTime().get("minThrows")).intValue();

        SnakeLadderDP dpSolver =
                new SnakeLadderDP(N, impossibleSnakes, new HashMap<>());

        int dpThrows =
                ((Long) dpSolver.solveAndMeasureTime().get("minThrows")).intValue();

        Assertions.assertEquals(6, bfsThrows,
                "BFS should find the minimum 6 throws for this specific board.");

        Assertions.assertEquals(6, dpThrows,
                "DP should find the minimum 6 throws for this specific board.");
    }
}
