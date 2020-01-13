package com.processmanager.enums;

public enum EnumError {

    INVALID("Empty or Invalid");

    private final String error;

    EnumError(String computerStatus) {
        this.error = computerStatus;
    }

    public String getValue() {
        return error;
    }
}
