package com.red.service;

public class DummyThrottle implements Throttle{
    @Override
    public Boolean processRequest() {
//        System.out.println("dummy throttle.");
        return false;
    }
}
