package com.example.db.gui;

import com.example.db.engine.Database;
import com.example.db.storage.DatabaseStorage;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainWindow extends JFrame {
    private final Database db;
    private final DatabaseStorage storage;
    private final File dbFile;

    private final TableListPanel tableListPanel;
    private final TableDataPanel tableDataPanel;

    public MainWindow(Database db, DatabaseStorage storage, File dbFile) {
        super("Database: " + dbFile.getName());
        this.db = db;
        this.storage = storage;
        this.dbFile = dbFile;

        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        tableListPanel = new TableListPanel(db, this::onTableSelected, this::onCreateTable, this::onDropTable);
        tableDataPanel = new TableDataPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                tableListPanel, tableDataPanel);
        splitPane.setDividerLocation(250);

        getContentPane().add(splitPane);
        tableListPanel.refreshTableList();
    }

    private void onTableSelected(String tableName) {
        tableDataPanel.showTable(db.getTable(tableName));
    }

    private void onCreateTable() {
        NewTableDialog dialog = new NewTableDialog(this, db, () -> {
            tableListPanel.refreshTableList();
            storage.save(db, dbFile.getParentFile());
        });
        dialog.setVisible(true);
    }

    private void onDropTable(String tableName) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Удалить таблицу " + tableName + "?",
                "Подтверждение",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            db.dropTable(tableName);

            File folder = dbFile.getParentFile();
            if (folder == null) folder = new File(".");
            storage.deleteTableFile(folder, tableName);

            tableListPanel.refreshTableList();
            tableDataPanel.clearTable();

            storage.save(db, folder);
        }
    }
}
