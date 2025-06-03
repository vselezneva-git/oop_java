package com.example.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class SimpleThreadPool {
    private final BlockingQueue<Task> taskQueue;
    private final List<Thread> workers;
    private final List<WorkerThread> workerThreads;

    public SimpleThreadPool(int numThreads) {
        taskQueue = new LinkedBlockingQueue<>();
        workers = new ArrayList<>();
        workerThreads = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            WorkerThread workerThread = new WorkerThread(taskQueue);
            Thread thread = new Thread(workerThread);
            workers.add(thread);
            workerThreads.add(workerThread);
            thread.start();
        }
    }

    public void execute(Task task) {
        taskQueue.offer(task);
    }

    public void shutdown() {
        for (WorkerThread worker : workerThreads) {
            worker.stop();
        }

        for (Thread thread : workers) {
            thread.interrupt();
        }
    }
}
