package com.ospavliuk.myvaadintest.Model;

import java.util.ArrayList;

public class ArtInt {
    private static final int[][] LEGAL_COMBINATIONS = new int[5040][];
    private final ArrayList<int[]> prevMoves = new java.util.ArrayList<>();

    static {
        int counter = 0;
        for (int a = 0; a < 10; a++) {
            for (int b = 0; b < 10; b++) {
                if (b != a) {
                    for (int c = 0; c < 10; c++) {
                        if (c != b && c != a) {
                            for (int d = 0; d < 10; d++) {
                                if (d != c && d != b && d != a) {
                                    LEGAL_COMBINATIONS[counter++] = new int[]{a, b, c, d};
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public int[] nextMove() throws WrongScoreException {
        int[] attempt = null;
        for (int[] current : LEGAL_COMBINATIONS) {
            if (matchToAllPrevious(current)) {
                attempt = current;
                break;
            }
        }
        if (attempt == null) {
            throw new WrongScoreException();
        }
        return attempt;
    }

    private boolean matchToAllPrevious(int[] attempt) {
        for (int[] move : prevMoves) {
            int[] score = Score.getScore(attempt, move);
            if (score[0] != move[4] || score[1] != move[5]) {
                return false;
            }
        }
        return true;
    }

    public void writeToLog(int[] a) {
        prevMoves.add(a);
    }

    public ArrayList<int[]> getPrevMoves() {
        return prevMoves;
    }
}
