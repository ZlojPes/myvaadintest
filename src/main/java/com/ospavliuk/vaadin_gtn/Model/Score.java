package com.ospavliuk.vaadin_gtn.Model;

public class Score {

    public static int[] getScore(int[] matrix, int[] attempt) {
        int score1 = 0, score2 = 0;
        for (int k = 0; k < 4; k++) {
            for (int l = 0; l < 4; l++) {
                if (matrix[k] == attempt[l]) {
                    score1++;
                    if (k == l) {
                        score2++;
                    }
                }
            }
        }
        return new int[]{score1, score2};
    }
}
