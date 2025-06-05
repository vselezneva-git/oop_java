package com.example.db.engine;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Row {
    private List<Object> values;

    @JsonCreator
    public Row(@JsonProperty("values") List<Object> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Values cannot be null or empty");
        }
        this.values = List.copyOf(values);
    }

    public List<Object> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "Row" + values;
    }
}
