package com.towerofhanoi;

import com.towerofhanoi.logic.HanoiClassic3Pegs;
import com.towerofhanoi.logic.HanoiClassic4Pegs;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HanoiAlgorithmTest {

    @Test
    void test3PegRecursiveMoveCount() {
        int n = 5;
        assertEquals((1 << n) - 1,
                HanoiClassic3Pegs.solveRecursive(n,'A','C','B').size());
    }

    @Test
    void test3PegIterativeEqualsRecursive() {
        int n = 6;
        assertEquals(
                HanoiClassic3Pegs.solveRecursive(n,'A','C','B'),
                HanoiClassic3Pegs.solveIterative(n,'A','C','B')
        );
    }

    @Test
    void test4PegFrameStewartIsBetterThan3Peg() {
        int n = 8;
        int fsMoves = HanoiClassic4Pegs.solveFrameStewart(n,'A','D','B','C').size();
        int classicMoves = HanoiClassic3Pegs.solveRecursive(n,'A','D','B').size();
        assertTrue(fsMoves < classicMoves);
    }
}
