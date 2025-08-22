package org.bigBrotherBooks.infra.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.json.JsonException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class JsonUtils {

    // Thread-safe ObjectMapper instance
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    // Private constructor to prevent instantiation
    private JsonUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Creates and configures the ObjectMapper instance
     *
     * @return Configured ObjectMapper
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Configure for better handling of various JSON formats
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // Add support for Java 8+ time types
        mapper.registerModule(new JavaTimeModule());

        // Use snake_case for JSON property names (common in APIs)
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);

        return mapper;
    }

    /**
     * Get the configured ObjectMapper instance
     * Useful when you need direct access to ObjectMapper
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    // ==================== SERIALIZATION METHODS ====================

    /**
     * Converts object to JSON string
     *
     * @param object Object to serialize
     * @return JSON string representation
     * @throws JsonException if serialization fails
     */
    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonException("Failed to serialize object to JSON", e);
        }
    }

    /**
     * Converts object to pretty-printed JSON string
     *
     * @param object Object to serialize
     * @return Formatted JSON string
     * @throws JsonException if serialization fails
     */
    public static String toPrettyJson(Object object) {
        if (object == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonException("Failed to serialize object to pretty JSON", e);
        }
    }

    /**
     * Converts object to JSON bytes
     *
     * @param object Object to serialize
     * @return JSON as byte array
     * @throws JsonException if serialization fails
     */
    public static byte[] toJsonBytes(Object object) {
        if (object == null) {
            return new byte[0];
        }

        try {
            return OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new JsonException("Failed to serialize object to JSON bytes", e);
        }
    }

    // ==================== DESERIALIZATION METHODS ====================

    /**
     * Converts JSON string to specified type
     *
     * @param json  JSON string
     * @param clazz Target class type
     * @param <T>   Type parameter
     * @return Deserialized object
     * @throws JsonException if deserialization fails
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new JsonException("Failed to deserialize JSON to " + clazz.getSimpleName(), e);
        }
    }

    /**
     * Converts JSON string to specified type using TypeReference
     * Useful for generic types like List<Book>, Map<String, Object>, etc.
     *
     * @param json          JSON string
     * @param typeReference TypeReference for complex types
     * @param <T>           Type parameter
     * @return Deserialized object
     * @throws JsonException if deserialization fails
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new JsonException("Failed to deserialize JSON using TypeReference", e);
        }
    }

    /**
     * Converts JSON bytes to specified type
     *
     * @param jsonBytes JSON as byte array
     * @param clazz     Target class type
     * @param <T>       Type parameter
     * @return Deserialized object
     * @throws JsonException if deserialization fails
     */
    public static <T> T fromJsonBytes(byte[] jsonBytes, Class<T> clazz) {
        if (jsonBytes == null || jsonBytes.length == 0) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(jsonBytes, clazz);
        } catch (IOException e) {
            throw new JsonException("Failed to deserialize JSON bytes to " + clazz.getSimpleName(), e);
        }
    }

    /**
     * Converts InputStream to specified type
     *
     * @param inputStream JSON input stream
     * @param clazz       Target class type
     * @param <T>         Type parameter
     * @return Deserialized object
     * @throws JsonException if deserialization fails
     */
    public static <T> T fromJson(InputStream inputStream, Class<T> clazz) {
        if (inputStream == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(inputStream, clazz);
        } catch (IOException e) {
            throw new JsonException("Failed to deserialize JSON from InputStream to " + clazz.getSimpleName(), e);
        }
    }

    /**
     * Reads JSON from file
     *
     * @param file  JSON file
     * @param clazz Target class type
     * @param <T>   Type parameter
     * @return Deserialized object
     * @throws JsonException if deserialization fails
     */
    public static <T> T fromJsonFile(File file, Class<T> clazz) {
        if (file == null || !file.exists()) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(file, clazz);
        } catch (IOException e) {
            throw new JsonException("Failed to deserialize JSON from file to " + clazz.getSimpleName(), e);
        }
    }

    // ==================== JSON MANIPULATION METHODS ====================

    /**
     * Converts JSON string to JsonNode for manipulation
     *
     * @param json JSON string
     * @return JsonNode representation
     * @throws JsonException if parsing fails
     */
    public static JsonNode parseJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new JsonException("Failed to parse JSON string", e);
        }
    }


    /**
     * Extracts value from JSON string using JSON path
     *
     * @param json JSON string
     * @param path JSON path (e.g., "user.name", "items[0].id")
     * @return String value or null if not found
     */
    public static String extractValue(String json, String path) {
        JsonNode rootNode = parseJson(json);
        if (rootNode == null) {
            return null;
        }

        JsonNode valueNode = findNodeByPath(rootNode, path);
        return valueNode != null && !valueNode.isNull() ? valueNode.asText() : null;
    }

    /**
     * Helper method to find node by path
     *
     * @param rootNode Root JSON node
     * @param path     Dot-separated path
     * @return JsonNode at path or null if not found
     */
    private static JsonNode findNodeByPath(JsonNode rootNode, String path) {
        String[] pathParts = path.split("\\.");
        JsonNode currentNode = rootNode;

        for (String part : pathParts) {
            if (currentNode == null) {
                return null;
            }

            // Handle array access like "items[0]"
            if (part.contains("[") && part.contains("]")) {
                String arrayField = part.substring(0, part.indexOf('['));
                int arrayIndex = Integer.parseInt(part.substring(part.indexOf('[') + 1, part.indexOf(']')));

                currentNode = currentNode.get(arrayField);
                if (currentNode != null && currentNode.isArray() && arrayIndex < currentNode.size()) {
                    currentNode = currentNode.get(arrayIndex);
                } else {
                    return null;
                }
            } else {
                currentNode = currentNode.get(part);
            }
        }

        return currentNode;
    }

    // ==================== CONVENIENCE METHODS ====================

    /**
     * Converts object to Map representation
     *
     * @param object Object to convert
     * @return Map representation
     */
    public static Map<String, Object> toMap(Object object) {
        if (object == null) {
            return null;
        }

        return OBJECT_MAPPER.convertValue(object, new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * Converts Map to specified object type
     *
     * @param map   Map to convert
     * @param clazz Target class type
     * @param <T>   Type parameter
     * @return Converted object
     */
    public static <T> T fromMap(Map<String, Object> map, Class<T> clazz) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        return OBJECT_MAPPER.convertValue(map, clazz);
    }


    /**
     * Deep copies an object using JSON serialization/deserialization
     *
     * @param object Object to copy
     * @param <T>    Type parameter
     * @return Deep copy of the object
     */
    public static <T> T deepCopy(T object) {
        if (object == null) {
            return null;
        }

        //noinspection unchecked
        Class<T> clazz = (Class<T>) object.getClass();

        String json = toJson(object);
        return fromJson(json, clazz);
    }
}
