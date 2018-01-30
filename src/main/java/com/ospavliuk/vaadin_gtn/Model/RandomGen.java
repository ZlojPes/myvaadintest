package com.ospavliuk.vaadin_gtn.Model;

class RandomGen {
    static int[] getRandom() {
        int[] output = new int[4];
        output[0] = (int) (Math.random() * 10);
        for (int i = 1; i < 4; i++) {
            output[i] = (int) (Math.random() * 10);
            for (int j = 0; j < i; j++) {
                if (output[i] == output[j]) {
                    i--;
                    break;
                }
            }
        }
        return output;
    }
}
