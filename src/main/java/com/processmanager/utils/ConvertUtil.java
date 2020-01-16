package com.processmanager.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ConvertUtil {

    private static final Logger Log = LoggerFactory.getLogger(ConvertUtil.class);

    public static <T> Object convert(Object entityData, Class<T> model) {
        String jsonStringObject;
        try {
            jsonStringObject = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).writeValueAsString(entityData);
        } catch (JsonProcessingException e) {
            Log.error("Cannot write Value as String " + entityData.toString());
            jsonStringObject = entityData.toString();
        }
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS").create().fromJson(jsonStringObject, model);
    }

    public static <T> List convertList(List entityData, Class<T> c) {
        String jsonStringObject;
        List list = new ArrayList<>();

        for (Object entityDat : entityData) {
            try {
                jsonStringObject = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).writeValueAsString(entityDat);
            } catch (JsonProcessingException e) {
                Log.error("Cannot write Value as String " + entityData.toString());
                jsonStringObject = entityData.toString();
            }
            list.add(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS").create().fromJson(jsonStringObject, c));
        }

        return list;
    }

}
