package com.example.moview.util.mapper;

import com.example.moview.exception.JsonMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class JsonMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = (Logger) LogManager.getRootLogger();

    public static <E> E readValue(final String json, Class<E> clazz) {
        try {
            log.info(String.format("Converting a json string to %s.", clazz.getName()));
            final E object = objectMapper.readValue(json, clazz);
            log.info(String.format("The conversion of a json string to %s is complete.", clazz.getName()));
            return object;
        } catch (JsonProcessingException e) {
            log.error(String.format("The conversion of a json string to %s failed.", clazz.getName()));
            throw new JsonMappingException(String.format("The conversion of a json string to %s failed.",
                    clazz.getName()));
        }
    }

    public static <E> String writeValueAsString(final E object) {
        try {
            log.info(String.format("Converting  %s to a json string.", object.getClass().getName()));
            final String json = objectMapper.writeValueAsString(object);
            log.info(String.format("Conversion of %s to a json string was successful.", object.getClass().getName()));
            return json;
        } catch (JsonProcessingException e) {
            log.error(String.format("Failed to convert %s to json", object.getClass().getName()));
            throw new JsonMappingException(String.format("Failed to convert %s to json", object.getClass().getName()));
        }
    }
}
