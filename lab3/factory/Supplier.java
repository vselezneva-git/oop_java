package com.example.factory;

import com.example.model.Detail;
import com.example.model.DetailType;
import com.example.storage.Storage;

public class Supplier<T extends Detail> implements Runnable {
    private final DetailType type;
    private final Storage<T> storage;
    private int delay;
    private volatile boolean running = true;

    public Supplier(DetailType type, Storage<T> storage, int delay) {
        this.type = type;
        this.storage = storage;
        this.delay = delay;
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
                T detail = createDetailByType();
                storage.put(detail);
                System.out.println("Created: " + detail);
                Thread.sleep(delay);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @SuppressWarnings("unchecked")
    private T createDetailByType() {
        switch (type) {
            case BODY:
                return (T) new com.example.model.Body();
            case MOTOR:
                return (T) new com.example.model.Motor();
            case ACCESSORY:
                return (T) new com.example.model.Accessory();
            default:
                throw new IllegalStateException("Unknown detail type: " + type);
        }
    }
}
