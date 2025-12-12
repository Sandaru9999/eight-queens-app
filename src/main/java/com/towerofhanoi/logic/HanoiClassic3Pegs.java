package com.towerofhanoi.logic;

import java.util.ArrayList;
import java.util.List;

public class HanoiClassic3Pegs {

    public static List<String> solve(int n, char from, char to, char aux) {
        List<String> moves = new ArrayList<>();
        solveRecursive(n, from, to, aux, moves);
        return moves;
    }

    private static void solveRecursive(int n, char from, char to, char aux, List<String> moves) {
        if (n == 0) return;
        solveRecursive(n - 1, from, aux, to, moves);
        moves.add("Move disk " + n + " from " + from + " to " + to);
        solveRecursive(n - 1, aux, to, from, moves);
    }
}
