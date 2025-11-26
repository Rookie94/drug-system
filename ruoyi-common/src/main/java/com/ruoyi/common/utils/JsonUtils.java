package com.ruoyi.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 配置ObjectMapper
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public JsonUtils() {
    }

    public static List listToJsonField(List lists) {
        try {
            String jsonStr = objectMapper.writeValueAsString(lists);
            return objectMapper.readValue(jsonStr, List.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    public static Map<String, Object> entityToMap(Object object) {
        try {
            String jsonStr = objectMapper.writeValueAsString(object);
            return objectMapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    public static Map<String, String> entityToMaps(Object object) {
        try {
            String jsonStr = objectMapper.writeValueAsString(object);
            return objectMapper.readValue(jsonStr, new TypeReference<Map<String, String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    public static Map<String, Object> stringToMap(String object) {
        try {
            return objectMapper.readValue(object, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    public static <T> T getJsonToBean(String jsonData, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonData, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    public static com.fasterxml.jackson.databind.JsonNode getJsonToJsonArray(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    public static com.fasterxml.jackson.databind.JsonNode getListToJsonArray(List<?> list) {
        try {
            String jsonStr = objectMapper.writeValueAsString(list);
            return objectMapper.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    public static String getObjectToString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    public static String getObjectToStringDateFormat(Object object, String dateFormat) {
        try {
            ObjectMapper mapper = objectMapper.copy();
            mapper.setDateFormat(new java.text.SimpleDateFormat(dateFormat));
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    public static <T> List<T> getJsonToList(String jsonData, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonData, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    public static List<Map<String, Object>> getJsonToListMap(String jsonData) {
        try {
            return objectMapper.readValue(jsonData, new TypeReference<List<Map<String, Object>>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    public static List<Map<String, Object>> getJsonToList(com.fasterxml.jackson.databind.JsonNode jsonArray) {
        try {
            String jsonStr = objectMapper.writeValueAsString(jsonArray);
            return objectMapper.readValue(jsonStr, new TypeReference<List<Map<String, Object>>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    public static <T> T getJsonToBean(Object dto, Class<T> clazz) {
        try {
            String jsonStr = objectMapper.writeValueAsString(dto);
            return objectMapper.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    public static <T> List<T> getJsonToList(Object dto, Class<T> clazz) {
        try {
            String jsonStr = objectMapper.writeValueAsString(dto);
            return objectMapper.readValue(jsonStr, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }
}