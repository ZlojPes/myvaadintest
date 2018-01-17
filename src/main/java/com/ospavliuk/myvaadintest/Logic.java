package com.ospavliuk.myvaadintest;

import com.vaadin.ui.Button;

public class Logic extends MyUIDesigner {
    public Logic() {
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
    }

    class ButtonListener implements Button.ClickListener {
        public void buttonClick(Button.ClickEvent click) {
            numberField.setValue(numberField.getValue() + click.getButton().getCaption());
        }
    }
}
