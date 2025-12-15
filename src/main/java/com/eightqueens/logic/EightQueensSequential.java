package com.eightqueens.logic;

import com.eightqueens.models.Solution;
import java.util.ArrayList;
import java.util.List;

public class EightQueensSequential {
    private static final int N = 8;

    public static List<Solution> solve() {
        List<Solution> solutions = new ArrayList<>();
        int[] board = new int[N];
        solveNQueens(board, 0, solutions);
        return solutions;
    }

    private static void solveNQueens(int[] board, int row, List<Solution> solutions) {
        if (row == N) {
            solutions.add(new Solution(board.clone()));
            return;
        }
        for (int col = 0; col < N; col++) {
            if (isSafe(board, row, col)) {
                board[row] = col;
                solveNQueens(board, row + 1, solutions);
            }
        }
    }

    private static boolean isSafe(int[] board, int row, int col) {
        for (int i = 0; i < row; i++) {
            if (board[i] == col || Math.abs(board[i] - col) == row - i) return false;
        }
        return true;
    }

    // ---------- NEW METHOD TO CHECK PLAYER BOARD ----------
    public static boolean checkSolution(int[] board) {
        for (int row = 0; row < N; row++) {
            if (!isSafe(board, row, board[row])) return false;
        }
        return true;
    }
}
