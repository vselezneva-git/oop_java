package com.example.factory;

import com.example.model.*;
import com.example.storage.Storage;
import com.example.threadpool.Task;

import java.awt.*;


public class Worker implements Task {
    private final Storage<Body> bodyStorage;
    private final Storage<Motor> motorStorage;
    private final Storage<Accessory> accessoryStorage;
    private final Storage<Car> carStorage;

    public Worker(Storage<Body> bodyStorage, Storage<Motor> motorStorage, Storage<Accessory> accessoryStorage, Storage<Car> carStorage) {
        this.bodyStorage = bodyStorage;
        this.motorStorage = motorStorage;
        this.accessoryStorage = accessoryStorage;
        this.carStorage = carStorage;
    }

    @Override
    public void run() {
        try {
            Body body = bodyStorage.take();
            Motor motor = motorStorage.take();
            Accessory accessory = accessoryStorage.take();

            Car car = new Car(body, motor, accessory);

            carStorage.put(car);

            System.out.println("Worker created car: +" + car.getId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
