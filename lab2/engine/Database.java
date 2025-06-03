package com.example.db.engine;

import java.util.HashMap;

import java.util.*;


public class Database {
    private final Map<String, Table> tables = new HashMap<>();

    public void createTable(String name, List<Column> columns) {
        if (tables.containsKey(name)) {
            throw new IllegalArgumentException("Table " + name + " create.");
        }
        tables.put(name, new Table(name, columns));
    }

    public void dropTable(String name) {
        if (!tables.containsKey(name)) {
            throw new IllegalArgumentException("Table " + name + " not found");
        }
        tables.remove(name);
    }

    public Table getTable(String name) {
        Table table = tables.get(name);
        if (table == null) {
            throw new IllegalArgumentException("Table " + name + " not found");
        }
        return table;
    }

    public Set<String> listTableName() {
        return Collections.unmodifiableSet(tables.keySet());
    }
}
