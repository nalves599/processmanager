package com.processmanager.enums;

public enum EnumComputerStatus {

    SHUTDOWN("-1"),
    WARNING("0"),
    ACTIVE("1");

    private final String computerStatus;

    EnumComputerStatus(String computerStatus) {
        this.computerStatus = computerStatus;
    }

    public String getValue() {
        return computerStatus;
    }

}
