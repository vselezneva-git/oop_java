package com.example.db.command;

import com.example.db.engine.Database;
import com.example.db.engine.Row;
import com.example.db.engine.Table;

import java.io.PrintStream;
import java.time.Period;
import java.util.function.Predicate;

public class DeleteCommand implements Command {
    private final String tableName;
    private final Predicate<Row> where;

    public DeleteCommand(String tableName, Predicate<Row> where) {
        this.tableName = tableName;
        this.where = where;
    }

    @Override
    public void execute(Database db, PrintStream out) {
        Table table = db.getTable(tableName);
        int deleted = table.delete(where);
        out.println("Deleted " + deleted + " rows");
    }
}
