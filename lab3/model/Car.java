package com.example.model;

public class Car {
    private static int nextId = 0;
    private final int id;

    private final Body body;
    private final Motor motor;
    private final Accessory accessory;

    public Car(Body body, Motor motor, Accessory accessory) {
        this.id = nextId++;
        this.body = body;
        this.motor = motor;
        this.accessory = accessory;
    }

    public int getId() {
        return id;
    }

    public Body getBody() {
        return body;
    }

    public Motor getMotor() {
        return motor;
    }

    public Accessory getAccessory() {
        return accessory;
    }

    @Override
    public String toString() {
        return "Car " + id + " [body: " + body.getId() + " motor: " + motor.getId() + " acces: " + accessory.getId() + "]";
    }
}
