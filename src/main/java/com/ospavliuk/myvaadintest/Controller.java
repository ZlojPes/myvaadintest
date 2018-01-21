package com.ospavliuk.myvaadintest;

import com.ospavliuk.myvaadintest.Model.Model;
import com.ospavliuk.myvaadintest.Model.ModelImpl;
import com.ospavliuk.myvaadintest.Model.WrongScoreException;
import com.vaadin.ui.Button;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Controller extends MyUIDesigner {
    private static int counter;
    private boolean gameStarted, scoreNeeded, twoPlayers, isRobotMove = true;
    private Model model;
    private TextField currentInputField;


    public Controller() {
        model = new ModelImpl(this);
        counter++;
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
            String num = isRobotMove ? scoreField.getValue() : numberField.getValue();
            if (isRobotMove) {
                char[] scoreInput = scoreField.getValue().toCharArray();
                print(model.processScore(new int[]{scoreInput[0] - 48, scoreInput[4] - 48}));
                scoreField.setEnabled(false);
                numberField.setEnabled(true);
                scoreField.clear();
                currentInputField = numberField;
                numberField.focus();
                isRobotMove = false;
            } else {
                print(model.processUserMove(num));
                if (twoPlayers) {
                    isRobotMove = true;
                    if (scoreNeeded) {
                        scoreField.setEnabled(true);
                        numberField.setEnabled(false);
                        numberField.clear();
                        currentInputField = scoreField;
                        scoreField.focus();
                    }
                    try {
                        print(model.getRobotsMove());
                    } catch (WrongScoreException e) {
                        infoArea.setValue("You've provided wrong score at least one time during this game. You lose.");
                        model.stop();
                        stopGame();
                    }
                    if (!scoreNeeded) {
                        print(model.processScore(new int[]{-1}));
                        isRobotMove = false;
                    }
                }
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
            robotArea.setEnabled(isEnabled);
        });

        startButton.addClickListener(clickEvent -> {
            if (gameStarted) {
                infoArea.setValue(infoArea.getValue() + "Game interrupted" + "\n");
                stopGame();
            } else {
                startGame();
            }
            gameCombo.setEnabled(!gameStarted);
            numberCombo.setEnabled(!gameStarted);
            keyboardLayout.setEnabled(gameStarted);
            twoPlayers = gameCombo.getValue().equals("Option1");
            scoreNeeded = gameCombo.getValue().equals("Option1") && numberCombo.getValue().equals("Option1");
        });
    }

    private void stopGame() {
        startButton.setCaption("Start game");
        gameStarted = false;gameCombo.setEnabled(!gameStarted);
        numberCombo.setEnabled(!gameStarted);
        keyboardLayout.setEnabled(gameStarted);
        twoPlayers = gameCombo.getValue().equals("Option1");
        scoreNeeded = gameCombo.getValue().equals("Option1") && numberCombo.getValue().equals("Option1");

    }

    private void startGame() {
        startButton.setCaption("Stop game");
        gameStarted = true;
        model.start();
        isRobotMove = false;
        currentInputField = numberField;
        numberField.clear();
        numberField.setEnabled(true);
        scoreField.clear();
        scoreField.setEnabled(false);
        userArea.clear();
        robotArea.clear();
        infoArea.clear();
    }

    private void print(int[] data) {
        TextArea area = isRobotMove ? robotArea : userArea;
        int length = data.length;
        String out;
        switch (length) {
            case 2:
                out = "  " + data[0] + " : " + data[1] + "\n";
                break;
            case 4:
                out = "" + data[0] + data[1] + data[2] + data[3];
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
