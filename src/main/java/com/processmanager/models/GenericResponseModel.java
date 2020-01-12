package com.processmanager.models;

public class GenericResponseModel {
    private int status;
    private String message;
    private boolean success;
    private Object responseObject;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResponseObject() {
        return responseObject;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setResponseObject(Object responseObject) {
        this.responseObject = responseObject;
    }
}
