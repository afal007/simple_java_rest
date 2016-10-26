package ru.nsu.fit.endpoint.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.fit.endpoint.service.database.data.Entity;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class JsonConverter {
    public static String toJSON(Entity<?> entity) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(entity);
    }
}
