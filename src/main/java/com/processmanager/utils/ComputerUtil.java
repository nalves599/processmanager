package com.processmanager.utils;

public class ComputerUtil {
    private static Long computerId;
    private static String ip;
    private static int port;
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

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        ComputerUtil.ip = ip;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        ComputerUtil.port = port;
    }
}
