package com.example.db.engine;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Column {
    private String name;
    private DataType type;
    private boolean isNotNull;
    private boolean isUnique;

    @JsonCreator
    public Column(@JsonProperty("name") String name,
                  @JsonProperty("type") DataType type,
                  @JsonProperty("notNull") boolean isNotNull,
                  @JsonProperty("unique") boolean isUnique) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name column shouldn't be empty");
        }
        this.name = name;
        this.type = type;
        this.isNotNull = isNotNull;
        this.isUnique = isUnique;
    }

    public String getName() {
        return name;
    }

    public DataType getType() {
        return type;
    }

    @JsonProperty("notNull")
    public boolean isNotNull() {
        return isNotNull;
    }

    @JsonProperty("unique")
    public boolean isUnique() {
        return isUnique;
    }

    public void checkRules(Object value) {
        if (value == null) {
            if (isNotNull) {
                throw new IllegalArgumentException("Column " + name + " shouldn't be null");
            }
            return;
        }

        switch (type) {
            case INT -> {
                if (!(value instanceof Integer)) throwTypeError(value);
            }
            case STRING -> {
                if (!(value instanceof String)) throwTypeError(value);
            }
            case DATE -> {
                if (!(value instanceof java.time.OffsetDateTime)) throwTypeError(value);
            }
            case BOOLEAN -> {
                if (!(value instanceof Boolean)) throwTypeError(value);
            }
            case ARRAY_STRING -> {
                if (!(value instanceof java.util.List)) throwTypeError(value);
            }
            default -> throw new IllegalStateException("Unknown type column: " + type);
        }
    }

    private void throwTypeError(Object value) {
        String actual = value.getClass().getSimpleName();
        throw new IllegalArgumentException("invalid type for column " + name + ", expected " + type + ", received " + actual);
    }
}
