package ru.stepup;

import java.util.LinkedList;
import java.util.Queue;

public class ThreadPool {
    private Queue<Runnable> tasks = new LinkedList<>();
    private Thread[] workers;
    private final Object lock = new Object(); // New lock object
    private volatile boolean isShutdown = false; // New field

    public ThreadPool(int size) {
        workers = new Thread[size];
        for (int i = 0; i < size; i++) {
            workers[i] = new Worker(this);
            workers[i].start();
        }
    }

    public void execute(Runnable task) {
        synchronized (lock) { // Use lock object for synchronization
            if (isShutdown) {
                throw new IllegalStateException("ThreadPool has been shut down, no new tasks are accepted");
            }
            tasks.add(task);
            lock.notify(); // Call notify on lock object
        }
    }

    public void shutdown() {
        synchronized (lock) {
            isShutdown = true;
        }
    }

    public void awaitTermination() {
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public Queue<Runnable> getTasks() {
        return tasks;
    }

    public Thread[] getWorkers() {
        return workers;
    }

    public Object getLock() {
        return lock;
    }

    public boolean isShutdown() {
        return isShutdown;
    }
}