package com.eightqueens.logic;

import com.eightqueens.models.Solution;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class EightQueensThreaded {
    private static final int N = 8;

    public static List<Solution> solve() {
        List<Solution> solutions = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(N);
        List<Future<List<Solution>>> futures = new ArrayList<>();

        for (int col = 0; col < N; col++) {
            final int c = col;
            futures.add(executor.submit(() -> {
                List<Solution> partial = new ArrayList<>();
                int[] board = new int[N];
                board[0] = c;
                solveNQueens(board, 1, partial);
                return partial;
            }));
        }

        for (Future<List<Solution>> f : futures) {
            try {
                solutions.addAll(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
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
