package com.deliverytech.delivery_api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {}

    public static Long extractId(String json) {
        try {
            JsonNode node = MAPPER.readTree(json);
            if (node.has("id")) {
                return node.get("id").asLong();
            }
        } catch (Exception e) {
            // ignore and return null
        }
        return null;
    }
}
