package com.example.demo;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RunInfo {
    private static String runtimeMessage = "";

    public static String printInfo() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();


        Future<String> future = executor.schedule(() -> {
            runtimeMessage = "5 seconds passed since the launch!";
            System.out.println(runtimeMessage);
            return runtimeMessage;
        }, 5, TimeUnit.SECONDS);

        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException("Error waiting for task completion", e);
        } finally {
            executor.shutdown();
        }
    }

    public static String getRuntimeMessage() {
        return runtimeMessage;
    }
}
