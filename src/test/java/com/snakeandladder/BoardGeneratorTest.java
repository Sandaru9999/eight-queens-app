package com.snakeandladder;

import com.snakeandladder.logic.BoardGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BoardGeneratorTest {

    private static final int TEST_N = 9;
    private static final int EXPECTED_TRANSFERS = 7;
    private static final int BOARD_SIZE = 81;

    @Test
    void testCorrectNumberOfLadders() {
        BoardGenerator generator = new BoardGenerator(9);
        Map<Integer, Integer> ladders = generator.generateLadders();
        Assertions.assertEquals(7, ladders.size(), "Should generate N-2 ladders.");
    }

    @Test
    void testCorrectNumberOfSnakes() {
        BoardGenerator generator = new BoardGenerator(9);
        Map<Integer, Integer> snakes = generator.generateSnakes();
        Assertions.assertEquals(7, snakes.size(), "Should generate N-2 snakes.");
    }

    @Test
    void testNoOverlapBetweenTransfers() {
        BoardGenerator generator = new BoardGenerator(9);
        Map<Integer, Integer> ladders = generator.generateLadders();
        Map<Integer, Integer> snakes = generator.generateSnakes();

        Set<Integer> allEndpoints = new HashSet<>();
        allEndpoints.addAll(ladders.keySet());
        allEndpoints.addAll(ladders.values());

        for (Integer cell : snakes.keySet()) {
            Assertions.assertFalse(allEndpoints.contains(cell),
                    "Snake start point should not overlap.");
        }

        for (Integer cell : snakes.values()) {
            Assertions.assertFalse(allEndpoints.contains(cell),
                    "Snake end point should not overlap.");
        }
    }

    @Test
    void testTransfersAreWithinBoundsAndDirectional() {
        BoardGenerator generator = new BoardGenerator(9);
        Map<Integer, Integer> ladders = generator.generateLadders();
        Map<Integer, Integer> snakes = generator.generateSnakes();

        for (Map.Entry<Integer, Integer> entry : ladders.entrySet()) {
            Assertions.assertTrue(entry.getKey() < entry.getValue(), "Ladder must go up.");
            Assertions.assertTrue(entry.getKey() > 1, "Ladder must start after 1.");
            Assertions.assertTrue(entry.getValue() < BOARD_SIZE, "Ladder must end before 81.");
        }

        for (Map.Entry<Integer, Integer> entry : snakes.entrySet()) {
            Assertions.assertTrue(entry.getKey() > entry.getValue(), "Snake must go down.");
            Assertions.assertTrue(entry.getKey() < BOARD_SIZE, "Snake must start before 81.");
            Assertions.assertTrue(entry.getValue() > 1, "Snake must end after 1.");
        }
    }
}
