package com.towerofhanoi.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Two approaches for 4-pegs:
 *  - Frame-Stewart heuristic (select k via sqrt heuristic)
 *  - Naive split (move n-1 to aux, move last, move n-1 to target)
 */
public class HanoiClassic4Pegs {

    // Frame-Stewart heuristic
    public static List<String> solveFrameStewart(int n, char from, char to, char aux1, char aux2) {
        List<String> moves = new ArrayList<>();
        solveFS(n, from, to, aux1, aux2, moves);
        return moves;
    }

    private static void solveFS(int n, char from, char to, char aux1, char aux2, List<String> moves) {
        if (n == 0) return;
        if (n == 1) {
            moves.add(formatMove(1, from, to));
            return;
        }
        // Heuristic for k (Frame–Stewart): often near n - round(sqrt(2*n+1)) + 1
        int k = n - (int) Math.round(Math.sqrt(2.0 * n + 1)) + 1;
        if (k < 1) k = 1;
        if (k >= n) k = n - 1;

        // Move top k to aux1 using 4 pegs recursively
        solveFS(k, from, aux1, aux2, to, moves);
        // Move remaining n-k using classical 3-peg algorithm (from, to, aux2)
        List<String> threeMoves = HanoiClassic3Pegs.solveRecursive(n - k, from, to, aux2);
        moves.addAll(threeMoves);
        // Move k from aux1 to to using 4 pegs
        solveFS(k, aux1, to, from, aux2, moves);
    }

    // Naive: move n-1 with 4 pegs recursively to aux1, move last disk, move n-1 from aux1 to target
    public static List<String> solveNaive(int n, char from, char to, char aux1, char aux2) {
        List<String> moves = new ArrayList<>();
        solveNaiveHelper(n, from, to, aux1, aux2, moves);
        return moves;
    }

    private static void solveNaiveHelper(int n, char from, char to, char aux1, char aux2, List<String> moves) {
        if (n == 0) return;
        if (n == 1) {
            moves.add(formatMove(1, from, to));
            return;
        }
        // naive: move n-1 to aux1 (using 4 pegs)
        solveNaiveHelper(n - 1, from, aux1, to, aux2, moves);
        moves.add(formatMove(n, from, to));
        solveNaiveHelper(n - 1, aux1, to, from, aux2, moves);
    }

    private static String formatMove(int disk, char from, char to) {
        return "Move disk " + disk + " from " + from + " to " + to;
    }
}
