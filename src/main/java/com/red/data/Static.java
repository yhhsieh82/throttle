package com.red.data;

public class Static {
    private static int successCase = 0;
    private static int failCase = 0;

    public synchronized static void incrementSuccess() {
        successCase ++;
    }

    public synchronized static void incrementFail() {
        failCase ++;
    }

    public synchronized static int recordAndClearSuccess() {
        int result = successCase;
        successCase = 0;
        return result;
    }

    public synchronized static int recordAndClearFail() {
        int result = failCase;
        failCase = 0;
        return result;
    }
}
