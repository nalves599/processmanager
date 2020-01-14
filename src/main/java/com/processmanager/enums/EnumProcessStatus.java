package com.processmanager.enums;

public enum EnumProcessStatus {

    RUNNING("1"),
    WARNING("0"),
    STOPPED("-1");

    private final String processStatus;

    EnumProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getValue() {
        return processStatus;
    }
}
