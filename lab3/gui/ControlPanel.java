package com.example.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {
    private final JButton startButton = new JButton("Start");
    private final JButton stopButton = new JButton("Stop");
    private final JCheckBox logCheckbox = new JCheckBox("Enable Logging");

    public ControlPanel(boolean initialLogEnabled) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        setBorder(BorderFactory.createTitledBorder("Controls"));

        logCheckbox.setSelected(initialLogEnabled);

        add(startButton);
        add(stopButton);
        add(logCheckbox);
    }

    public void setStartAction(ActionListener listener) {
        startButton.addActionListener(listener);
    }

    public void setStopAction(ActionListener listener) {
        stopButton.addActionListener(listener);
    }

    public void setLogToggleAction(ActionListener listener) {
        logCheckbox.addActionListener(listener);
    }

    public boolean isLoggingEnabled() {
        return logCheckbox.isSelected();
    }
}
