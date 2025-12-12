package com.towerofhanoi.logic;

import java.util.ArrayList;
import java.util.List;

public class HanoiClassic4Pegs {

    public static List<String> solve(int n, char from, char to, char aux1, char aux2) {
        List<String> moves = new ArrayList<>();
        solve4Pegs(n, from, to, aux1, aux2, moves);
        return moves;
    }

    private static void solve4Pegs(int n, char from, char to, char aux1, char aux2, List<String> moves) {
        if (n == 0) return;
        if (n == 1) {
            moves.add("Move disk 1 from " + from + " to " + to);
            return;
        }
        int k = n - (int) Math.round(Math.sqrt(2 * n + 1)) + 1; // Frame-Stewart heuristic
        solve4Pegs(k, from, aux1, aux2, to, moves);
        HanoiClassic3Pegs.solve(n - k, from, to, aux2).forEach(moves::add);
        solve4Pegs(k, aux1, to, from, aux2, moves);
    }
}
