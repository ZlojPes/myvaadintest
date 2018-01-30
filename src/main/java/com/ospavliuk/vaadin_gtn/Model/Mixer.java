package com.ospavliuk.vaadin_gtn.Model;

class Mixer {
    private final int[] mixer;

    Mixer() {
        mixer = new int[10];
        int y;
        for (int i = 1; i < 10; i++) {
            y = (int) (Math.random() * 10);
            if (mixer[y] == 0) mixer[y] = i;
            else i--;
        }
    }

    int[] getMix(int[] input) {
        int[] output = new int[input.length];
        for (int i = 0; i < output.length; i++)
            output[i] = mixer[input[i]];
        return output;
    }
}
