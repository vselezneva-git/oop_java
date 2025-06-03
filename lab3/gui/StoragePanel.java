package com.example.gui;

import javax.swing.*;
import java.awt.*;

public class StoragePanel extends JPanel {
    private final JLabel bodyLabel = new JLabel("0");
    private final JLabel motorLabel = new JLabel("0");
    private final JLabel accessoryLabel = new JLabel("0");
    private final JLabel carLabel = new JLabel("0");

    public StoragePanel() {
        setLayout(new GridLayout(4, 2, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Storage Status"));

        add(new JLabel("Body:"));
        add(bodyLabel);

        add(new JLabel("Motor:"));
        add(motorLabel);

        add(new JLabel("Accessory:"));
        add(accessoryLabel);

        add(new JLabel("Car:"));
        add(carLabel);
    }

    public void updateStorage(int bodyCount, int motorCount, int accessoryCount, int carCount) {
        bodyLabel.setText(String.valueOf(bodyCount));
        motorLabel.setText(String.valueOf(motorCount));
        accessoryLabel.setText(String.valueOf(accessoryCount));
        carLabel.setText(String.valueOf(carCount));
    }
}
