package ru.stepup;

import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args) {
        // Создаем пул потоков
        ThreadPool threadPool = new ThreadPool(5);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 5, 0, null, null);

        // Создаем 10 задач и отправляем их на выполнение
        for (int i = 0; i < 10; i++) {
            int taskNumber = i;
            threadPool.execute(() -> {
                System.out.println("Task " + taskNumber + " is being executed by " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        threadPool.shutdown();

        // Ждем завершения всех задач
        try {
            threadPool.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Все задачи выполнены");
    }
}