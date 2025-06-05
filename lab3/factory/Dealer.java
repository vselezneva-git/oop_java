package com.example.factory;

import com.example.model.Car;
import com.example.storage.Storage;

public class Dealer implements Runnable {
    private final int dealerId;
    private final Storage<Car> carStorage;
    private int delay;
    private final Logger logger;
    private volatile boolean running = true;

    public Dealer(Storage<Car> carStorage, int delay, int dealerId, Logger logger) {
        this.dealerId = dealerId;
        this.carStorage = carStorage;
        this.delay = delay;
        this.logger = logger;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        try {
            while (running) {
                Car car = carStorage.take();
                logSale(car);
                Thread.sleep(delay);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void logSale(Car car) {
        String log = System.currentTimeMillis() + ": Dealer " + dealerId + ": " + car;
        System.out.println(log);
        if (logger != null) {
            logger.logSale(car, dealerId);
        }
    }
}
