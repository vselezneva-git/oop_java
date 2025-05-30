package com.example.db.engine;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

public enum DataType {
    INT, STRING, DATE, BOOLEAN, ARRAY_STRING;

    public Object parse(String text) {
        switch (this) {
            case INT:
                return Integer.parseInt(text);
            case STRING:
                return text;
            case DATE:
                return OffsetDateTime.parse(text);
            case BOOLEAN:
                return Boolean.parseBoolean(text);
            case ARRAY_STRING:
                String t = text.trim();
                if (!t.startsWith("[") || !t.endsWith("]")) {
                    throw new IllegalArgumentException("Invalid array format");
                }
                String inner = t.substring(1, t.length() - 1).trim();
                if (inner.isEmpty()) {
                    return List.of();
                }
                return Arrays.asList(inner.split("\\s*, \\s*"));
            default:
                throw new IllegalStateException("Unexpected DataType");
        }
    }
}
