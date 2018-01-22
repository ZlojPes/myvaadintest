package com.ospavliuk.myvaadintest.Model;

import com.ospavliuk.myvaadintest.Controller;

import java.util.Arrays;

public class ModelImpl implements Model {
    private Controller controller;
    private ArtInt artInt;
    private Mixer mixer;
    private int[] secretNumber;
    private int[] currentRobotsMove;
    private Result result;
    private int userGlobalScore, robotGlobalScore;

    public ModelImpl(Controller controller) {
        this.controller = controller;
        result = Result.NO_WINNERS;
    }

    @Override
    public void start() {
        artInt = new ArtInt();
        mixer = new Mixer();
        secretNumber = RandomGen.getRandom();
        System.out.println(Arrays.toString(secretNumber));
    }

    @Override
    public Result checkWinners() {
        return result;
    }

    @Override
    public int[] getGlobalScore() {
        return new int[]{userGlobalScore, robotGlobalScore};
    }

    @Override
    public void stop() {
        artInt = null;
        mixer = null;
        result = Result.NO_WINNERS;
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
        if (score[1] == 4) {
            result = Result.USER_WINS;
            userGlobalScore++;
        }
        return out;
    }

    @Override
    public int[] getRobotsMove() throws WrongScoreException {
        currentRobotsMove = artInt.nextMove();
        return mixer.getMix(currentRobotsMove);
    }

    @Override
    public int[] processRobotScore(int[] score) throws WrongScoreException {
        if (score[0] < 0) {
            score = Score.getScore(secretNumber, mixer.getMix(currentRobotsMove));
        }
        int[] out = new int[6];
        System.arraycopy(currentRobotsMove, 0, out, 0, 4);
        System.arraycopy(score, 0, out, 4, 2);
        artInt.writeToLog(out);
        artInt.nextMove(); //check if at least one possible combination left, otherwise exception would be thrown
        if (score[1] == 4) {
            if (result == Result.USER_WINS) {
                result = Result.IN_A_DRAW;
            } else {
                result = Result.ROBOT_WINS;
            }
            robotGlobalScore++;
        }
        return score;
    }

    public void userForfeit() {
        robotGlobalScore++;
    }
}
