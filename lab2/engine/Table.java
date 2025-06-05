package com.example.db.engine;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class Table {
    private String name;
    private List<Column> columns;
    private List<Row> rows;

    @JsonIgnore
    private final Map<String, Set<Object>> uniqueIdx = new HashMap<>();

    @JsonCreator
    public Table(@JsonProperty("name") String name,
                 @JsonProperty("columns") List<Column> columns,
                 @JsonProperty("rows") List<Row> rows) {
        this.name = name;
        this.columns = List.copyOf(columns);
        this.rows = new ArrayList<>(rows != null ? rows : List.of());

        for (Column col : this.columns) {
            if (col.isUnique()) {
                uniqueIdx.put(col.getName(), new HashSet<>());
            }
        }

        for (Row row : this.rows) {
            List<Object> vals = row.getValues();
            for (int i = 0; i < columns.size(); i++) {
                Column col = columns.get(i);
                if (col.isUnique()) {
                    uniqueIdx.get(col.getName()).add(vals.get(i));
                }
            }
        }
    }

    public Table(String name, List<Column> columns) {
        this(name, columns, new ArrayList<>());
    }

    @JsonProperty("name")
    public String getNameTable() {
        return name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Row> getRows() {
        return Collections.unmodifiableList(rows);
    }

    public void insert(Row row) {
        List<Object> vals = row.getValues();
        if (vals.size() != columns.size()) {
            throw new IllegalArgumentException("Invalid number of values");
        }

        for (int i = 0; i < columns.size(); i++) {
            Column col = columns.get(i);
            Object v = vals.get(i);
            col.checkRules(v);
            checkUnique(col, v);
        }

        for (int i = 0; i < columns.size(); i++) {
            Column col = columns.get(i);
            if (col.isUnique()) {
                uniqueIdx.get(col.getName()).add(vals.get(i));
            }
        }

        rows.add(row);
    }

    public int delete(java.util.function.Predicate<Row> cond) {
        int removed = 0;
        Iterator<Row> it = rows.iterator();
        while (it.hasNext()) {
            Row row = it.next();
            if (cond.test(row)) {
                List<Object> vals = row.getValues();
                for (int i = 0; i < columns.size(); i++) {
                    Column col = columns.get(i);
                    if (col.isUnique()) {
                        uniqueIdx.get(col.getName()).remove(vals.get(i));
                    }
                }
                it.remove();
                removed++;
            }
        }
        return removed;
    }

    private void checkUnique(Column col, Object v) {
        if (!col.isUnique()) return;
        Set<Object> idx = uniqueIdx.get(col.getName());
        if (idx.contains(v)) {
            throw new IllegalArgumentException("UNIQUE constraint violated in column " + col.getName());
        }
    }
}
