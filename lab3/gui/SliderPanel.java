package com.example.gui;

import javax.swing.*;
import java.awt.*;
import java.util.function.IntConsumer;

public class SliderPanel extends JPanel {
    private final JSlider bodySlider;
    private final JSlider motorSlider;
    private final JSlider accessorySlider;
    private final JSlider dealerSlider;

    public SliderPanel() {
        setLayout(new GridLayout(4, 2, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Delays (ms)"));

        bodySlider = createSlider(100, 3000, 1000);
        motorSlider = createSlider(100, 3000, 1000);
        accessorySlider = createSlider(100, 3000, 1000);
        dealerSlider = createSlider(100, 3000, 1000);

        add(new JLabel("Body Supplier:"));
        add(bodySlider);

        add(new JLabel("Motor Supplier:"));
        add(motorSlider);

        add(new JLabel("Accessory Supplier:"));
        add(accessorySlider);

        add(new JLabel("Dealer:"));
        add(dealerSlider);
    }

    private JSlider createSlider(int min, int max, int initial) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, initial);
        slider.setMajorTickSpacing(500);
        slider.setMinorTickSpacing(100);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return slider;
    }

    public void setBodyDelayListener(IntConsumer onChange) {
        bodySlider.addChangeListener(e -> {
            if (!bodySlider.getValueIsAdjusting()) {
                onChange.accept(bodySlider.getValue());
            }
        });
    }

    public void setMotorDelayListener(IntConsumer onChange) {
        motorSlider.addChangeListener(e -> {
            if (!motorSlider.getValueIsAdjusting()) {
                onChange.accept(motorSlider.getValue());
            }
        });
    }

    public void setAccessoryDelayListener(IntConsumer onChange) {
        accessorySlider.addChangeListener(e -> {
            if (!accessorySlider.getValueIsAdjusting()) {
                onChange.accept(accessorySlider.getValue());
            }
        });
    }

    public void setDealerDelayListener(IntConsumer onChange) {
        dealerSlider.addChangeListener(e -> {
            if (!dealerSlider.getValueIsAdjusting()) {
                onChange.accept(dealerSlider.getValue());
            }
        });
    }
}
