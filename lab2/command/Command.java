package com.example.db.command;

import com.example.db.engine.Database;

import java.io.PrintStream;

public interface Command {
    void execute(Database db, PrintStream out);
}
