package com.example.threadpool;

import java.util.concurrent.BlockingQueue;

public class WorkerThread implements Runnable {
    private final BlockingQueue<Task> taskQueue;
    private volatile boolean running = true;

    public WorkerThread(BlockingQueue<Task> taskQueue) {
        this.taskQueue = taskQueue;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        try {
            while (running) {
                Task task = taskQueue.take();
                task.run();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
