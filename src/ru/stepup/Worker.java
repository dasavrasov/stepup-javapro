package ru.stepup;

class Worker extends Thread {
    private final ThreadPool pool;

    public Worker(ThreadPool pool) {
        this.pool = pool;
    }

    public void run() {
        try {
            while (!pool.isShutdown()) {
                Runnable task = pool.getTask();
                if (task != null) {
                    task.run();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}