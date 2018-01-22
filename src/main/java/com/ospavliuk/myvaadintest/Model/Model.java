package com.ospavliuk.myvaadintest.Model;

public interface Model {
    void start();

    void stop();

    int[] processUserMove(String move);

    int[] getRobotsMove() throws WrongScoreException;

    int[] processRobotScore(int[] score) throws WrongScoreException;

    int[] getGlobalScore();

    Result checkWinners();

    void userForfeit();
}
