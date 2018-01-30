package com.ospavliuk.vaadin_gtn.Model;

import java.util.List;

public interface Model {
    void start();

    void stop();

    int[] processUserMove(String move);

    int[] getRobotsMove() throws WrongScoreException;

    List<int[]> getRobotMovesList();

    int[] processRobotScore(int[] score) throws WrongScoreException;

    int[] getGlobalScore();

    Result checkWinners();

    void userForfeit();

    int[] getInventedNumber();
}
