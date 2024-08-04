package ru.stepup;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadPool {
    private final List<Worker> workers = new LinkedList<>();
    private final ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<>();
    private volatile boolean isShutdown = false;

    public ThreadPool(int numThreads) {
        for (int i = 0; i < numThreads; i++) {
            Worker worker = new Worker(this);
            worker.start();
            workers.add(worker);
        }
    }

    public void execute(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("ThreadPool has been shut down");
        }
        tasks.add(task);
    }

    public void shutdown() {
        isShutdown = true;
        for (Worker worker : workers) {
            worker.interrupt();
        }
    }

    public Runnable getTask() {
        return tasks.poll();
    }

    public boolean isShutdown() {
        return isShutdown;
    }

    public void awaitTermination() throws InterruptedException {
        while (!isShutdown || !tasks.isEmpty()) {
            Thread.sleep(100);
        }
    }
}