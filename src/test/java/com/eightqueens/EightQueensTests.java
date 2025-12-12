package com.eightqueens;

import com.eightqueens.logic.EightQueensSequential;
import com.eightqueens.logic.EightQueensThreaded;
import com.eightqueens.models.Solution;
import com.eightqueens.db.SolutionDAO;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EightQueensTests {

    private static int[] sampleValidBoard = {0, 4, 7, 5, 2, 6, 1, 3};
    private static int[] sampleInvalidBoard = {0, 1, 2, 3, 4, 5, 6, 7};
    private static String samplePlayer = "TestPlayer";

    @BeforeAll
    static void setup() {
        // Reset recognized flags in DB before tests
        SolutionDAO.resetRecognizedFlags();
    }

    @Test
    @Order(1)
    @DisplayName("Test Sequential Solver produces 92 solutions")
    void testSequentialSolver() {
        List<Solution> solutions = EightQueensSequential.solve();
        assertEquals(92, solutions.size(), "Sequential solver should produce 92 solutions");
    }

    @Test
    @Order(2)
    @DisplayName("Test Threaded Solver produces 92 solutions")
    void testThreadedSolver() {
        List<Solution> solutions = EightQueensThreaded.solve();
        assertEquals(92, solutions.size(), "Threaded solver should produce 92 solutions");
    }

    @Test
    @Order(3)
    @DisplayName("Test board to string conversion")
    void testBoardToString() {
        String result = SolutionDAO.boardToString(sampleValidBoard);
        assertEquals("0,4,7,5,2,6,1,3", result);
    }

    @Test
    @Order(4)
    @DisplayName("Test saving a solution and checking recognized flag")
    void testSaveAndRecognizeSolution() {
        String solStr = SolutionDAO.boardToString(sampleValidBoard);
        SolutionDAO.saveSolution(solStr, 1000);
        SolutionDAO.markSolutionRecognized(solStr);

        assertTrue(SolutionDAO.isSolutionRecognized(solStr), "Solution should be recognized in DB");
    }

    @Test
    @Order(5)
    @DisplayName("Test submitting a player solution")
    void testSubmitPlayerSolution() {
        String solStr = SolutionDAO.boardToString(sampleValidBoard);
        SolutionDAO.savePlayerAndSolution(samplePlayer, solStr, 1234);

        List<String> recognized = SolutionDAO.getAllRecognizedSolutions();
        assertTrue(recognized.contains(solStr), "Submitted solution should be recognized in DB");
    }

    @Test
    @Order(6)
    @DisplayName("Test invalid board is not recognized")
    void testInvalidBoardRecognition() {
        String invalidSol = SolutionDAO.boardToString(sampleInvalidBoard);
        SolutionDAO.saveSolution(invalidSol, 500);
        assertFalse(SolutionDAO.isSolutionRecognized(invalidSol), "Invalid board should not be recognized");
    }
}
