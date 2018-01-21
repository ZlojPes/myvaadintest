package com.ospavliuk.myvaadintest.Model;

import com.ospavliuk.myvaadintest.Controller;

import java.util.Arrays;

public class ModelImpl implements Model {
    private Controller controller;
    private ArtInt artInt;
    private Mixer mixer;
    private int[] secretNumber;
    private int[] currentRobotsMove;

    public ModelImpl(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void start() {
        artInt = new ArtInt();
        mixer = new Mixer();
        secretNumber = RandomGen.getRandom();
    }

    @Override
    public void stop() {
        artInt = null;
        mixer = null;
    }

    @Override
    public int[] processUserMove(String move) {
        char[] c = move.toCharArray();
        int[] in = new int[4];
        for (int i = 0; i < 4; i++) {
            in[i] = c[i] - 48;
        }
        int[] score = Score.getScore(secretNumber, in);
        int[] out = new int[6];
        System.arraycopy(in, 0, out, 0, 4);
        System.arraycopy(score, 0, out, 4, 2);
        return out;
    }

    @Override
    public int[] getRobotsMove() throws WrongScoreException {
        currentRobotsMove = artInt.nextMove();
        return mixer.getMix(currentRobotsMove);
    }

    @Override
    public int[] processScore(int[] score) {
        if (score[0] < 0) {
            score = Score.getScore(secretNumber, currentRobotsMove);
        }
        int[] out = new int[6];
        System.arraycopy(currentRobotsMove, 0, out, 0, 4);
        System.arraycopy(score, 0, out, 4, 2);
        artInt.writeToLog(out);
        return score;
    }
}
