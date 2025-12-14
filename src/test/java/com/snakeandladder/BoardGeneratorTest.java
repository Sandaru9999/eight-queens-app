package com.snakeandladder; 

import com.snakeandladder.logic.BoardGenerator; 

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class BoardGeneratorTest {

    
    private static final int TEST_N = 9;
    private static final int EXPECTED_TRANSFERS = TEST_N - 2;
    private static final int BOARD_SIZE = TEST_N * TEST_N;

    @Test
    public void testCorrectNumberOfLadders() {
        BoardGenerator generator = new BoardGenerator(TEST_N);
        Map<Integer, Integer> ladders = generator.generateLadders();
        
        
        assertEquals("Should generate N-2 ladders.", EXPECTED_TRANSFERS, ladders.size());
    }

    @Test
    public void testCorrectNumberOfSnakes() {
        BoardGenerator generator = new BoardGenerator(TEST_N);
        Map<Integer, Integer> snakes = generator.generateSnakes();

        
        assertEquals("Should generate N-2 snakes.", EXPECTED_TRANSFERS, snakes.size());
    }

    @Test
    public void testNoOverlapBetweenTransfers() {
        BoardGenerator generator = new BoardGenerator(TEST_N);
        Map<Integer, Integer> ladders = generator.generateLadders();
        Map<Integer, Integer> snakes = generator.generateSnakes();

        Set<Integer> allEndpoints = new HashSet<>();
        allEndpoints.addAll(ladders.keySet()); 
        allEndpoints.addAll(ladders.values());
        
        
        for (int cell : snakes.keySet()) {
            assertFalse("Snake start point should not overlap.", allEndpoints.contains(cell));
        }
        for (int cell : snakes.values()) {
            assertFalse("Snake end point should not overlap.", allEndpoints.contains(cell));
        }
    }
    
    @Test
    public void testTransfersAreWithinBoundsAndDirectional() {
        BoardGenerator generator = new BoardGenerator(TEST_N);
        Map<Integer, Integer> ladders = generator.generateLadders();
        Map<Integer, Integer> snakes = generator.generateSnakes();

        for (Map.Entry<Integer, Integer> entry : ladders.entrySet()) {
            
            assertTrue("Ladder must go up.", entry.getKey() < entry.getValue());
            assertTrue("Ladder must start after 1.", entry.getKey() > 1);
            assertTrue("Ladder must end before " + BOARD_SIZE + ".", entry.getValue() < BOARD_SIZE);
        }

        for (Map.Entry<Integer, Integer> entry : snakes.entrySet()) {
            
            assertTrue("Snake must go down.", entry.getKey() > entry.getValue());
            assertTrue("Snake must start before " + BOARD_SIZE + ".", entry.getKey() < BOARD_SIZE);
            assertTrue("Snake must end after 1.", entry.getValue() > 1);
        }
    }
}