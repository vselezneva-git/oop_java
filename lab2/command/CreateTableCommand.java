package com.example.db.command;

import com.example.db.engine.Column;
import com.example.db.engine.Database;
import java.io.PrintStream;
import java.util.List;

public class CreateTableCommand implements Command {
    private final String tableName;
    private final List<Column> columns;

    public CreateTableCommand(String tableName, List<Column> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    @Override
    public void execute(Database db, PrintStream out) {
        db.createTable(tableName, columns);
        out.println("Table " + tableName + " created");
    }
}
