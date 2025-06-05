package com.example.gui;

import com.example.config.ConfigLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame {
    public MainWindow(ConfigLoader config) {
        super("Factory Simulator");

        StoragePanel storagePanel = new StoragePanel();
        ControlPanel controlPanel = new ControlPanel(config.getBoolean("LogSale"));
        SliderPanel sliderPanel = new SliderPanel();

        GuiController controller = new GuiController(
                config,
                storagePanel,
                controlPanel,
                sliderPanel
        );


        setLayout(new BorderLayout());
        add(storagePanel, BorderLayout.NORTH);
        add(sliderPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.shutdown();
                dispose();
                System.exit(0);
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
