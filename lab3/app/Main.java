package com.example.app;

import com.example.config.ConfigLoader;
import com.example.factory.Dealer;
import com.example.factory.Logger;
import com.example.factory.Supplier;
import com.example.factory.Worker;
import com.example.model.*;
import com.example.storage.Storage;
import com.example.threadpool.SimpleThreadPool;
import com.example.gui.MainWindow;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ConfigLoader config = new ConfigLoader("config.properties");

        if (args.length > 0 && args[0].equalsIgnoreCase("--console")) {
            runConsoleFactory(config);
        } else {
            SwingUtilities.invokeLater(() -> new MainWindow(config));
        }
    }

    private static void runConsoleFactory(ConfigLoader config) {
        int bodySize = config.getInt("StorageBodySize", 100);
        int motorSize = config.getInt("StorageMotorSize", 100);
        int accessorySize = config.getInt("StorageAccessorySize", 100);
        int autoSize = config.getInt("StorageAutoSize", 100);

        Storage<Body> bodyStorage = new Storage<>(bodySize);
        Storage<Motor> motorStorage = new Storage<>(motorSize);
        Storage<Accessory> accessoryStorage = new Storage<>(accessorySize);
        Storage<Car> carStorage = new Storage<>(autoSize);

        int bodySuppliersCount = config.getInt("BodySuppliers", 1);
        int motorSuppliersCount = config.getInt("MotorSuppliers", 1);
        int accessorySuppliersCount = config.getInt("AccessorySuppliers", 1);
        int dealersCount = config.getInt("Dealers", 1);
        int workersCount = config.getInt("Workers", 2);

        int bodyDelay = config.getInt("BodySupplierDelay", 1000);
        int motorDelay = config.getInt("MotorSupplierDelay", 1000);
        int accessoryDelay = config.getInt("AccessorySupplierDelay", 1000);
        int dealerDelay = config.getInt("DealerDelay", 1500);

        boolean logEnabled = config.getBoolean("LogSale");
        Logger logger = logEnabled ? new Logger("factory_log.txt") : null;

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < bodySuppliersCount; i++) {
            Supplier<Body> s = new Supplier<>(DetailType.BODY, bodyStorage, bodyDelay);
            Thread t = new Thread(s);
            t.start();
            threads.add(t);
        }

        for (int i = 0; i < motorSuppliersCount; i++) {
            Supplier<Motor> s = new Supplier<>(DetailType.MOTOR, motorStorage, motorDelay);
            Thread t = new Thread(s);
            t.start();
            threads.add(t);
        }

        for (int i = 0; i < accessorySuppliersCount; i++) {
            Supplier<Accessory> s = new Supplier<>(DetailType.ACCESSORY, accessoryStorage, accessoryDelay);
            Thread t = new Thread(s);
            t.start();
            threads.add(t);
        }

        for (int i = 0; i < dealersCount; i++) {
            Dealer dealer = new Dealer(carStorage, dealerDelay, i + 1, logger);
            Thread t = new Thread(dealer);
            t.start();
            threads.add(t);
        }

        SimpleThreadPool threadPool = new SimpleThreadPool(workersCount);

        Thread carAssemblyThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    threadPool.execute(new Worker(bodyStorage, motorStorage, accessoryStorage, carStorage));
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        carAssemblyThread.start();
        threads.add(carAssemblyThread);

        System.out.println("console mode. Press Ctrl+C to stop.");
    }
}
