package com.processmanager.utils;

import com.processmanager.models.GenericResponseModel;
import org.springframework.http.HttpStatus;

public class GenericResponseUtil {
    
    public static GenericResponseModel ok(Object object) {
        GenericResponseModel genericResponseModel = new GenericResponseModel();
        genericResponseModel.setStatus(HttpStatus.OK.value());
        genericResponseModel.setMessage(HttpStatus.OK.getReasonPhrase());
        genericResponseModel.setSuccess(true);
        genericResponseModel.setResponseObject(object);
        return genericResponseModel;
    }

    public static GenericResponseModel ok(String message, Object object) {
        GenericResponseModel genericResponseModel = new GenericResponseModel();
        genericResponseModel.setStatus(HttpStatus.OK.value());
        genericResponseModel.setMessage(HttpStatus.OK.getReasonPhrase());
        genericResponseModel.setSuccess(true);
        genericResponseModel.setMessage(message);
        genericResponseModel.setResponseObject(object);
        return genericResponseModel;
    }

    public static GenericResponseModel okNewInstance() {
        GenericResponseModel genericResponseModel = new GenericResponseModel();
        genericResponseModel.setStatus(HttpStatus.OK.value());
        genericResponseModel.setMessage(HttpStatus.OK.getReasonPhrase());
        genericResponseModel.setSuccess(true);
        return genericResponseModel;
    }

    public static GenericResponseModel setValues(int status, String message, Boolean success, Object object) {
        GenericResponseModel genericResponseModel = new GenericResponseModel();
        genericResponseModel.setStatus(status);
        genericResponseModel.setMessage(message);
        genericResponseModel.setSuccess(success);
        genericResponseModel.setResponseObject(object);
        return genericResponseModel;
    }

    public static GenericResponseModel setError(int status, String message) {
        GenericResponseModel genericResponseModel = new GenericResponseModel();
        genericResponseModel.setStatus(status);
        genericResponseModel.setMessage(message);
        genericResponseModel.setSuccess(false);
        genericResponseModel.setResponseObject(null);
        return genericResponseModel;
    }

    public static GenericResponseModel setErrorNotFound(String message) {
        return setError(HttpStatus.NOT_FOUND.value(), message);
    }

    public static GenericResponseModel setErrorBadRequest(String message) {
        return setError(HttpStatus.BAD_REQUEST.value(), message);
    }

    public static GenericResponseModel setErrorNoContent(String message) {
        return setError(HttpStatus.NO_CONTENT.value(), message);
    }

    public static GenericResponseModel setErrorFailedDependency(String message) {
        return setError(HttpStatus.FAILED_DEPENDENCY.value(), message);
    }

    public static GenericResponseModel setErrorNotAcceptable(String message) {
        return setError(HttpStatus.NOT_ACCEPTABLE.value(), message);
    }

    public static GenericResponseModel setErrorInternalServerError(String message){
        return setError(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }
}
