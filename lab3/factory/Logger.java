package com.example.factory;

import com.example.model.Car;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final PrintWriter writer;
    private boolean enabled = true;

    public Logger(String fileName) {
        try {
            writer = new PrintWriter(new FileWriter(fileName, true));
        } catch (IOException e) {
            throw new RuntimeException("Failed to open log file: " + fileName, e);
        }
    }

    public synchronized void logSale(Car car, int dealerId) {
        if (!enabled) return;

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String line = String.format("%s: Dealer %d: %s", time, dealerId, car);
        writer.println(line);
        writer.flush();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void close() {
        writer.close();
    }
}
