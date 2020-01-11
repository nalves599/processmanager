package com.processmanager.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelUtil {

    private static final Logger Log = LoggerFactory.getLogger(ModelUtil.class);

    public static Object convertEntityToModel(Object entityData, Object model) {
        String jsonStringObject;
        try {
            jsonStringObject = new ObjectMapper().writeValueAsString(entityData);
        } catch (JsonProcessingException e) {
            Log.error("Cannot write Value as String " + entityData.toString());
            jsonStringObject = entityData.toString();
        }
        return new Gson().fromJson(jsonStringObject, model.getClass());
    }

}
