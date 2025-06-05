package com.example.model;

public abstract class Detail {
    private static int nextId = 0;
    private final int id;

    public Detail() {
        this.id = nextId++;
    }

    public int getId() {
        return id;
    }
}
