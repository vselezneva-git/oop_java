package com.example.db.command;

import com.example.db.engine.Column;
import com.example.db.engine.Database;
import com.example.db.engine.Row;
import com.example.db.engine.Table;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class InsertCommand implements Command {
    private final String tableName;
    private final List<String> rawValues;

    public InsertCommand(String tableName, List<String> rawValues) {
        this.tableName = tableName;
        this.rawValues = rawValues;
    }

    @Override
    public void execute(Database db, PrintStream out) {
        Table table = db.getTable(tableName);
        List<Column> cols = table.getColumns();

        if (rawValues.size() != cols.size()) {
            throw new IllegalArgumentException("Expected " + cols.size() + " values but got " + rawValues.size());
        }

        List<Object> parsed = new ArrayList<>();
        for (int i = 0; i < cols.size(); i++) {
            Column col = cols.get(i);
            String text = rawValues.get(i);
            Object obj = col.getType().parse(text);
            col.checkRules(obj);
            parsed.add(obj);
        }
        Row row = new Row(parsed);
        table.insert(row);
        out.println("Inserted 1 row");
    }
}
