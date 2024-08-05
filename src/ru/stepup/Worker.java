package ru.stepup;

class Worker extends Thread {
    private final ThreadPool threadPool;

    public Worker(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Runnable task;
            synchronized (threadPool.getLock()) { // Use lock object for synchronization
                while (threadPool.getTasks().isEmpty() && !threadPool.isShutdown()) {
                    try {
                        threadPool.getLock().wait(); // Call wait on lock object
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                if (threadPool.getTasks().isEmpty() && threadPool.isShutdown()) {
                    return;
                }
                task = threadPool.getTasks().poll();
            }
            try {
                task.run();
            } catch (RuntimeException e) {
                // Handle or log exception
            }
        }
    }
}
