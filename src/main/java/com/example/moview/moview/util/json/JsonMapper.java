package com.example.moview.moview.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class JsonMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = (Logger) LogManager.getRootLogger();

    private JsonMapper() {
        throw new UnsupportedOperationException(String.format("Attempt to create %S", this.getClass().getSimpleName()));
    }

    public static <E> E readValue(final String json, Class<E> clazz) throws JsonProcessingException {
        log.info(String.format("Converting a json string to %s.", clazz.getName()));
        return objectMapper.readValue(json, clazz);
    }

    public static <E> String writeValueAsString(final E object) throws JsonProcessingException {
        log.info(String.format("Converting  %s to a json string.", object.getClass().getName()));
        return objectMapper.writeValueAsString(object);
    }
}
