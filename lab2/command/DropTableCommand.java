package com.example.db.command;

import com.example.db.engine.Database;
import com.example.db.storage.DatabaseStorage;

import java.io.File;
import java.io.PrintStream;

public class DropTableCommand implements Command {
    private final String tableName;
    private final File folder;
    private final DatabaseStorage storage;

    public DropTableCommand(String tableName, File folder, DatabaseStorage storage) {
        this.tableName = tableName;
        this.folder = folder;
        this.storage = storage;
    }

    @Override
    public void execute(Database db, PrintStream out) {
        db.dropTable(tableName);
        storage.deleteTableFile(folder, tableName);
        out.println("Table " + tableName + " deleted");
    }
}
