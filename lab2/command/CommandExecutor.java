package com.example.db.command;

import com.example.db.engine.Database;
import com.example.db.storage.DatabaseStorage;

import java.io.File;
import java.io.PrintStream;

public class CommandExecutor {
    private final Database db;
    private final CommandParser parser;
    private final PrintStream out;

    public CommandExecutor(Database db, File folder, DatabaseStorage storage, PrintStream out) {
        this.db = db;
        this.out = out;
        this.parser = new CommandParser(db, folder, storage);
    }

    public void executeLine(String line) {
        try {
            Command cmd = parser.parse(line);
            cmd.execute(db, out);
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
}
