package com.ospavliuk.myvaadintest;

import com.vaadin.ui.Button;
import com.vaadin.event.ShortcutAction.KeyCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Logic extends MyUIDesigner {

    public Logic() {
        initComponents();
    }

    private void initComponents(){
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
                youArea.setValue(youArea.getValue() + num + "\n");
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
        gameCombo.addValueChangeListener(listener ->{
            numberCombo.setEnabled(gameCombo.getValue().equals("Option1"));
        });
    }

    class ButtonListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent click) {
            numberField.setValue(numberField.getValue() + click.getButton().getCaption());
            numberField.focus();
        }
    }
}
