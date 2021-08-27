package com.red.service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CounterThrottle implements Throttle {
    // the com.throttle.service load capability in unit time
    private final int rateLimit;

    private int counter;

    public CounterThrottle(final int rateLimit, final int unitTime, final ScheduledExecutorService scheduler) {
        this.rateLimit = rateLimit;
        // unit time in ms, clean the counter per unit time
        scheduler.scheduleAtFixedRate(new CleanCounter(), 0, unitTime, TimeUnit.MILLISECONDS);
    }

    private class CleanCounter implements Runnable {
        @Override
        public synchronized void run() {
            counter = 0;
        }
    }

    @Override
    public synchronized Boolean processRequest() {
        if (counter < rateLimit) {
            counter ++;
            return true;
        }
        return false;
    }
}
