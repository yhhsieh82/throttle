package com.red;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.red.data.Static;
import com.red.service.CSVService;
import com.red.service.CounterThrottle;
import com.red.service.Throttle;

public class Main {
    private static final int EXPERIMENT_TIME = 20 * 1000;
    private static Boolean stop = false;
    // open a thread to do the scheduled job. ex: clean the counter for CounterThrottle
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final CSVService CSV_SERVICE = new CSVService();

    public static void main(String[] args) throws InterruptedException, IOException {
        // create throttle
        Throttle throttle = new CounterThrottle(100, 1000, scheduler);

        // simulate a 600 rps loading by creating task in 50ms in average per thread
        for (int i = 0; i < 30; i++) {
            Thread thread = new Thread(() -> {
                while (!Main.stop) {
                    // test throttle service
                    if (throttle.processRequest()) {
                        Static.incrementSuccess();
                    } else {
                        Static.incrementFail();
                    }

                    // random sleep to create request loading
                    Random random = new Random();
                    int randomStop = random.nextInt(100);
                    try {
                        Thread.sleep(randomStop);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

        // experiment static recording
        Thread recordingThread = new Thread(() -> {
            System.out.println("Success# Fail# Total#");
            System.out.println("-------- ----- ------");


            while (!stop) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int success = Static.recordAndClearSuccess();
                int fail = Static.recordAndClearFail();
                System.out.println(String.format("% 8d % 5d % 6d", success, fail, success + fail));
                CSV_SERVICE.addOneRecord(new Integer[]{success, fail, success + fail});
            }
        });
        recordingThread.start();

        Thread.sleep(EXPERIMENT_TIME);
        Main.stop = true;
        CSV_SERVICE.printRecords();
        recordingThread.join();
        scheduler.shutdown();
    }
}
