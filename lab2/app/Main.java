package com.example.db.app;

import com.example.db.command.CommandExecutor;
import com.example.db.engine.Database;
import com.example.db.gui.MainWindow;
import com.example.db.storage.DatabaseStorage;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        String dbPath = null;
        String command = null;

        for (int i = 0; i < args.length; i++) {
            if ("--db".equals(args[i]) && i + 1 < args.length) {
                dbPath = args[++i];
            } else if ("-c".equals(args[i]) && i + 1 < args.length) {
                command = args[++i];
            }
        }

        if (dbPath == null) {
            System.err.println("need path --db");
            return;
        }

        File mainDbFile = new File(dbPath);
        File folder = mainDbFile.getParentFile();
        if (folder == null) folder = new File(".");

        Database db = new Database();
        DatabaseStorage storage = new DatabaseStorage();

        storage.load(db, mainDbFile);

        if (command != null) {
            CommandExecutor executor = new CommandExecutor(db, folder, storage, System.out);
            executor.executeLine(command);
            storage.save(db, folder);
        } else {
            SwingUtilities.invokeLater(() -> {
                MainWindow window = new MainWindow(db, storage, mainDbFile);
                window.setVisible(true);
            });
        }
    }
}
