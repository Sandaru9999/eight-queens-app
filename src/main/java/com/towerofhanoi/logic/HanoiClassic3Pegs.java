package com.towerofhanoi.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Two approaches for 3-pegs:
 *  - recursive (classic)
 *  - iterative (stack-simulation / legal-move approach)
 */
public class HanoiClassic3Pegs {

    // Recursive (classic)
    public static List<String> solveRecursive(int n, char from, char to, char aux) {
        List<String> moves = new ArrayList<>();
        solveRecursiveHelper(n, from, to, aux, moves);
        return moves;
    }

    private static void solveRecursiveHelper(int n, char from, char to, char aux, List<String> moves) {
        if (n == 0) return;
        solveRecursiveHelper(n - 1, from, aux, to, moves);
        moves.add(formatMove(n, from, to));
        solveRecursiveHelper(n - 1, aux, to, from, moves);
    }

    // Iterative solver using stacks and legal move rules (produces optimal sequence for 3 pegs)
    public static List<String> solveIterative(int n, char from, char to, char aux) {
        List<String> moves = new ArrayList<>();
        if (n <= 0) return moves;

        // Peg labels mapping
        char[] labels = {from, aux, to};
        // If number of disks is even, swap target and aux to follow minimal move pattern
        if (n % 2 == 0) {
            char tmp = labels[1];
            labels[1] = labels[2];
            labels[2] = tmp;
        }

        // Stacks representing pegs; smallest disk represented by 1, largest by n
        Stack<Integer>[] pegs = new Stack[3];
        for (int i = 0; i < 3; i++) pegs[i] = new Stack<>();

        // initialize source peg with disks n..1 (top is last)
        for (int disk = n; disk >= 1; disk--) pegs[0].push(disk);

        int totalMoves = (1 << n) - 1;
        for (int move = 1; move <= totalMoves; move++) {
            // if move number is odd -> move the smallest disk
            if ((move & 1) == 1) {
                // smallest disk moves cyclically: from peg i to (i+1)%3 according to labels arrangement
                int fromPeg = findPegWithDisk(pegs, 1);
                int toPeg = (fromPeg + 1) % 3;
                // ensure legality: if toPeg has disk smaller than 1 (impossible), else perform
                if (!pegs[toPeg].isEmpty() && pegs[toPeg].peek() < 1) {
                    toPeg = (fromPeg + 2) % 3;
                }
                moveDisk(pegs, fromPeg, toPeg, moves, labels);
            } else {
                // make the only legal move not involving the smallest disk
                int[] candidatePairs = new int[][]{{0,1},{0,2},{1,2}}[0]; // dummy init
                boolean moved = false;
                // try all pairs (i,j)
                for (int i = 0; i < 3 && !moved; i++) {
                    for (int j = i+1; j < 3 && !moved; j++) {
                        if (canMoveTop(pegs, i, j)) {
                            moveDisk(pegs, i, j, moves, labels);
                            moved = true;
                        } else if (canMoveTop(pegs, j, i)) {
                            moveDisk(pegs, j, i, moves, labels);
                            moved = true;
                        }
                    }
                }
                // moved must be true by construction
            }
        }
        return moves;
    }

    private static boolean canMoveTop(Stack<Integer>[] pegs, int from, int to) {
        if (pegs[from].isEmpty()) return false;
        if (pegs[to].isEmpty()) return true;
        return pegs[from].peek() < pegs[to].peek();
    }

    private static void moveDisk(Stack<Integer>[] pegs, int from, int to, List<String> moves, char[] labels) {
        if (pegs[from].isEmpty()) return;
        int disk = pegs[from].pop();
        pegs[to].push(disk);
        moves.add(formatMove(disk, labels[from], labels[to]));
    }

    private static int findPegWithDisk(Stack<Integer>[] pegs, int disk) {
        for (int i = 0; i < pegs.length; i++) {
            if (!pegs[i].isEmpty() && pegs[i].peek() == disk) return i;
        }
        // fallback - search whole stack for disk (shouldn't be necessary for smallest)
        for (int i = 0; i < pegs.length; i++) if (pegs[i].search(disk) != -1) return i;
        return 0;
    }

    private static String formatMove(int disk, char from, char to) {
        return "Move disk " + disk + " from " + from + " to " + to;
    }
}
