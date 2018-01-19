package com.ospavliuk.myvaadintest;

import com.vaadin.ui.Button;
import com.vaadin.event.ShortcutAction.KeyCode;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Controller extends MyUIDesigner {
    private static ArrayList<Controller> list = new ArrayList<>();

    public Controller() {
        list.add(this);
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
            int length = numberField.getValue().length();
            if (length > 0) {
                numberField.setValue(numberField.getValue().substring(0, length - 1));
            }
        });
        enter.addClickListener(click -> {
            String num = numberField.getValue();
            if (num.length() == 4) {
                yourArea.setValue(yourArea.getValue() + num + "  4:4\n");
                numberField.clear();
            }
        });
        enter.setClickShortcut(KeyCode.ENTER);
        numberField.addValueChangeListener(changes -> {
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
        gameCombo.addValueChangeListener(valueChangeEvent -> {
            numberCombo.setEnabled(gameCombo.getValue().equals("Option1"));
        });
        startButton.addClickListener(clickEvent -> {
            if (startButton.getCaption().equals("Start game")) {
                startButton.setCaption("Stop game");
                startGame();
            } else {
                startButton.setCaption("Start game");
                stopGame();
            }
            testMessage();
        });
    }

    private void testMessage() {
        for (Controller c : list) {
            if (c != this) {
                c.getMessage("Hello!");
//                c.infoArea.focus();
            }
        }
    }

    private void getMessage(String message) {
        System.out.println("message received");
        infoArea.setValue(infoArea.getValue() + message + "\n");
        System.out.println("after printing");
//        infoArea.focus();
    }

    private void stopGame() {

    }

    private void startGame() {

    }

    class ButtonListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent click) {
            numberField.setValue(numberField.getValue() + click.getButton().getCaption());
//            numberField.focus();
        }
    }
}
