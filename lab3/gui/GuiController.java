package com.example.gui;

import com.example.config.ConfigLoader;
import com.example.factory.*;
import com.example.model.*;
import com.example.storage.Storage;
import com.example.threadpool.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GuiController {

    private final Storage<Body> bodyStorage;
    private final Storage<Motor> motorStorage;
    private final Storage<Accessory> accessoryStorage;
    private final Storage<Car> carStorage;

    private final List<Supplier<Body>> bodySuppliers = new ArrayList<>();
    private final List<Supplier<Motor>> motorSuppliers = new ArrayList<>();
    private final List<Supplier<Accessory>> accessorySuppliers = new ArrayList<>();
    private final List<Dealer> dealers = new ArrayList<>();

    private final SimpleThreadPool threadPool;
    private final Timer guiTimer;

    private final StoragePanel storagePanel;
    private final ControlPanel controlPanel;
    private final SliderPanel sliderPanel;

    private Logger logger;

    public GuiController(ConfigLoader config,
                         StoragePanel storagePanel,
                         ControlPanel controlPanel,
                         SliderPanel sliderPanel) {

        this.storagePanel = storagePanel;
        this.controlPanel = controlPanel;
        this.sliderPanel = sliderPanel;

        bodyStorage = new Storage<>(config.getInt("StorageBodySize"));
        motorStorage = new Storage<>(config.getInt("StorageMotorSize"));
        accessoryStorage = new Storage<>(config.getInt("StorageAccessorySize"));
        carStorage = new Storage<>(config.getInt("StorageAutoSize"));

        threadPool = new SimpleThreadPool(config.getInt("Workers"));

        if (config.getBoolean("LogSale")) {
            logger = new Logger("factory_log.txt");
        }

        guiTimer = new Timer(500, e -> updateStoragePanel());

        createSuppliers(config);
        createDealers(config);
        connectSliderPanel();
        connectControlPanel();
    }

    private void createSuppliers(ConfigLoader config) {
        for (int i = 0; i < config.getInt("BodySuppliers"); i++) {
            Supplier<Body> supplier = new Supplier<>(DetailType.BODY, bodyStorage, 1000);
            bodySuppliers.add(supplier);
            new Thread(supplier).start();
        }

        for (int i = 0; i < config.getInt("MotorSuppliers"); i++) {
            Supplier<Motor> supplier = new Supplier<>(DetailType.MOTOR, motorStorage, 1000);
            motorSuppliers.add(supplier);
            new Thread(supplier).start();
        }

        for (int i = 0; i < config.getInt("AccessorySuppliers"); i++) {
            Supplier<Accessory> supplier = new Supplier<>(DetailType.ACCESSORY, accessoryStorage, 1000);
            accessorySuppliers.add(supplier);
            new Thread(supplier).start();
        }
    }

    private void createDealers(ConfigLoader config) {
        for (int i = 0; i < config.getInt("Dealers"); i++) {
            Dealer dealer = new Dealer(carStorage, 1000, i + 1, logger);
            dealers.add(dealer);
            new Thread(dealer).start();
        }
    }

    private void connectSliderPanel() {
        sliderPanel.setBodyDelayListener(delay -> bodySuppliers.forEach(s -> s.setDelay(delay)));
        sliderPanel.setMotorDelayListener(delay -> motorSuppliers.forEach(s -> s.setDelay(delay)));
        sliderPanel.setAccessoryDelayListener(delay -> accessorySuppliers.forEach(s -> s.setDelay(delay)));
        sliderPanel.setDealerDelayListener(delay -> dealers.forEach(d -> d.setDelay(delay)));
    }

    private void connectControlPanel() {
        controlPanel.setStartAction(e -> startFactory());
        controlPanel.setStopAction(e -> stopFactory());
        controlPanel.setLogToggleAction(e -> {
            if (logger != null) {
                logger.setEnabled(controlPanel.isLoggingEnabled());
            }
        });
    }

    private void updateStoragePanel() {
        storagePanel.updateStorage(
                bodyStorage.size(),
                motorStorage.size(),
                accessoryStorage.size(),
                carStorage.size()
        );
    }

    public void startFactory() {
        guiTimer.start();
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    threadPool.execute(new Worker(bodyStorage, motorStorage, accessoryStorage, carStorage));
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public void stopFactory() {
        guiTimer.stop();
        threadPool.shutdown();
    }

    public void shutdown() {
        stopFactory();
        if (logger != null) {
            logger.close();
        }
    }
}
