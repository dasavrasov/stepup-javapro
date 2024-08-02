package ru.stepup;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class ThreadPool {
    private final List<Worker> workers = new LinkedList<>();
    private final LinkedList<Runnable> tasks = new LinkedList<>();
    private boolean isShutdown = false;

    public ThreadPool(int numThreads) {
        for (int i = 0; i < numThreads; i++) {
            Worker worker = new Worker(this);
            worker.start();
            workers.add(worker);
        }
    }

    public synchronized void execute(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("ThreadPool has been shut down");
        }
        tasks.add(task);
        notifyAll();
    }

    public synchronized void shutdown() {
        isShutdown = true;
        for (Worker worker : workers) {
            worker.interrupt();
        }
    }

    public synchronized Runnable getTask() throws InterruptedException {
        while (tasks.isEmpty() && !isShutdown) {
            wait();
        }
        return tasks.isEmpty() ? null : tasks.removeFirst();
    }

    public boolean isShutdown() {
        return isShutdown;
    }

    public synchronized void awaitTermination() throws InterruptedException {
        while (!isShutdown || !tasks.isEmpty()) {
            wait();
        }
    }
}