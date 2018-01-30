package com.ospavliuk.vaadin_gtn;

import com.ospavliuk.vaadin_gtn.Model.Model;
import com.ospavliuk.vaadin_gtn.Model.ModelImpl;
import com.ospavliuk.vaadin_gtn.Model.WrongScoreException;
import com.vaadin.ui.Button;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


class Controller extends MyUIDesigner {
    private static int counter;
    private boolean gameStarted, scoreNeeded, twoPlayers, isRobotMove = true;
    private Model model;
    private TextField currentInputField;

    Controller() {
        model = new ModelImpl();
        System.out.println(++counter);
        initComponents();
    }

    private void initComponents() {
        _1.addClickListener(new ButtonListener());
        _2.addClickListener(new ButtonListener());
        _3.addClickListener(new ButtonListener());
        _4.addClickListener(new ButtonListener());
        _5.addClickListener(new ButtonListener());
        _6.addClickListener(new ButtonListener());
        _7.addClickListener(new ButtonListener());
        _8.addClickListener(new ButtonListener());
        _9.addClickListener(new ButtonListener());
        _0.addClickListener(new ButtonListener());

        back.addClickListener(click -> {
            int length = currentInputField.getValue().length();
            if (length > 0) {
                currentInputField.setValue(currentInputField.getValue().substring(0, length - 1));
            }
        });

        enter.addClickListener(click -> {
            try {
                if (isRobotMove) {
                    char[] scoreInput = scoreField.getValue().toCharArray();
                    print(model.processRobotScore(new int[]{scoreInput[0] - 48, scoreInput[4] - 48}));
                    scoreField.setEnabled(false);
                    numberField.setEnabled(true);
                    scoreField.clear();
                    currentInputField = numberField;
                    numberField.focus();
                    isRobotMove = false;
                } else {
                    print(model.processUserMove(numberField.getValue()));
                    numberField.clear();
                    if (twoPlayers) {
                        isRobotMove = true;
                        print(model.getRobotsMove());
                        if (scoreNeeded) {
                            scoreField.setEnabled(true);
                            numberField.setEnabled(false);
                            currentInputField = scoreField;
                            scoreField.focus();
                        } else {
                            print(model.processRobotScore(new int[]{-1}));
                            isRobotMove = false;
                        }
                    }
                }
            } catch (WrongScoreException e) {
                infoArea.setValue("You've provided wrong score at least one time during this game. You lose.");
                int[] score = model.getGlobalScore();
                score[1]++;
                finishAndSetScore(score);
                model.userForfeit();
            }
            if (!isRobotMove) {
                checkWinners();
            }
        });

        enter.setClickShortcut(KeyCode.ENTER);

        numberField.addValueChangeListener(changeEvent -> {
            Pattern pattern = Pattern.compile("\\d");
            Matcher m = pattern.matcher(numberField.getValue());
            StringBuilder sb = new StringBuilder();
            while (m.find() && sb.length() < 4) {
                String digit = m.group();
                char[] chars = sb.toString().toCharArray();
                for (char aChar : chars) {
                    if (aChar == digit.charAt(0)) {
                        digit = "";
                        break;
                    }
                }
                sb.append(digit);
            }
            numberField.setValue(sb.toString());
            enter.setEnabled(sb.length() == 4);
            back.setEnabled(sb.length() > 0);
        });

        scoreField.addValueChangeListener(changeEvent -> {
            Pattern pattern = Pattern.compile("\\d");
            Matcher m = pattern.matcher(scoreField.getValue());
            StringBuilder sb = new StringBuilder();
            while (m.find() && sb.length() < 2) {
                String digit = m.group();
                if (sb.length() == 1) {
                    sb.append(" : ");
                }
                if (digit.charAt(0) - 48 < 5) {
                    sb.append(digit);
                }
            }
            scoreField.setValue(sb.toString());
            enter.setEnabled(sb.length() == 5);
            back.setEnabled(sb.length() > 0);
        });

        gameCombo.addValueChangeListener(changeEvent -> {
            boolean isEnabled = gameCombo.getValue().equals("Option1");
            numberCombo.setEnabled(isEnabled);
        });

        startButton.addClickListener(clickEvent -> {
            if (gameStarted) {
                infoArea.setValue(infoArea.getValue() + "Game interrupted" + "\n");
                int[] score = model.getGlobalScore();
                score[1]++;
                finishAndSetScore(score);
                model.userForfeit();
            } else {
                startGame();
            }
        });
    }

    private void checkWinners() {
        switch (model.checkWinners()) {
            case NO_WINNERS:
                return;
            case IN_A_DRAW:
                infoArea.setValue("You played in a draw!\nGood result!");
                break;
            case USER_WINS:
                infoArea.setValue("Congratulations!\nYou win!!!!");
                break;
            case ROBOT_WINS:
                int[] num = model.getInventedNumber();

                infoArea.setValue("Unfortunately, you lose :( Invented number was " + num[0] + num[1] + num[2] + num[3] + ". Try again and good luck!");
        }
        if (!scoreNeeded) {
            printVisibleRobotList();
        }
        finishAndSetScore(model.getGlobalScore());
    }

    private void finishAndSetScore(int[] score) {
        stopGame();
        overallScore.setValue(score[0] + " : " + score[1]);
    }

    private void stopGame() {
        startButton.setCaption("Start game");
        gameStarted = false;
        model.stop();
        generalStartStopActions();
    }

    private void startGame() {
        startButton.setCaption("Stop game");
        model.start();
        gameStarted = true;
        isRobotMove = false;
        currentInputField = numberField;
        numberField.clear();
        scoreField.clear();
        userArea.clear();
        robotArea.clear();
        infoArea.clear();
        twoPlayers = gameCombo.getValue().equals("Option1");
        scoreNeeded = gameCombo.getValue().equals("Option1") && numberCombo.getValue().equals("Option1");
        generalStartStopActions();
    }

    private void generalStartStopActions() {
        gameCombo.setEnabled(!gameStarted);
        numberCombo.setEnabled(!gameStarted);
        keyboardLayout.setEnabled(gameStarted);
        scoreField.setEnabled(false);
        numberField.setEnabled(gameStarted);
    }

    private void printVisibleRobotList() {
        isRobotMove = true;
        robotArea.clear();
        for (int[] move : model.getRobotMovesList()) {
            print(move);
        }
    }

    private void print(int[] data) {
        TextArea area = isRobotMove ? robotArea : userArea;
        int length = data.length;
        String out;
        switch (length) {
            case 2:
                out = String.format("%3s%2s%2s%n", data[0], ":", data[1]);
                break;
            case 4:
                out = scoreNeeded ? "" + data[0] + data[1] + data[2] + data[3] : "****";
                break;
            default:
                out = "" + data[0] + data[1] + data[2] + data[3] + "  " + data[4] + " : " + data[5] + "\n";
        }
        area.setValue(area.getValue() + out);
    }

    class ButtonListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent click) {
            currentInputField.setValue(currentInputField.getValue() + click.getButton().getCaption());
        }
    }
}
