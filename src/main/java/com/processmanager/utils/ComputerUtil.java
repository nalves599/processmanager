package com.processmanager.utils;

public class ComputerUtil {
    private static Long computerId;
    private static final int TIMEOUT_TIME = 10;
    private static final int OFFSET = 5000; // Tempo de compensação para inserir na base de dados - Milliseconds

    public static int getTimeoutTime() {
        return TIMEOUT_TIME;
    }

    public static int getOFFSET() {
        return OFFSET;
    }


    public static Long getComputerId() {
        return computerId;
    }

    public static void setComputerId(Long computerId) {
        ComputerUtil.computerId = computerId;
    }
}
