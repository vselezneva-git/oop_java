package com.example.db.gui;

import com.example.db.engine.Column;
import com.example.db.engine.DataType;
import com.example.db.engine.Database;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NewTableDialog extends JDialog {
    private final JTextField tableNameField = new JTextField();
    private final JPanel columnsPanel = new JPanel();
    private final List<ColumnConfigPanel> columnConfigs = new ArrayList<>();
    private final Database db;
    private final Runnable onCreateCallback;

    public NewTableDialog(Frame parent, Database db, Runnable onCreateCallback) {
        super(parent, "New Table", true);
        this.db = db;
        this.onCreateCallback = onCreateCallback;

        setLayout(new BorderLayout());

        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.add(new JLabel("Name:"), BorderLayout.WEST);
        namePanel.add(tableNameField, BorderLayout.CENTER);
        add(namePanel, BorderLayout.NORTH);

        columnsPanel.setLayout(new BoxLayout(columnsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(columnsPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addColumnBtn = new JButton("+ Add Column");
        JButton removeColumnBtn = new JButton("- Remove Column");
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        buttonPanel.add(addColumnBtn);
        buttonPanel.add(removeColumnBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);


        addColumnBtn.addActionListener(e -> addColumn());
        removeColumnBtn.addActionListener(e -> removeLastColumn());
        saveBtn.addActionListener(e -> saveTable());
        cancelBtn.addActionListener(e -> dispose());

        addColumn();
        setSize(600, 400);
        setLocationRelativeTo(parent);
    }

    private void addColumn() {
        ColumnConfigPanel panel = new ColumnConfigPanel();
        columnConfigs.add(panel);
        columnsPanel.add(panel);
        columnsPanel.revalidate();
        columnsPanel.repaint();
    }

    private void removeLastColumn() {
        if (!columnConfigs.isEmpty()) {
            ColumnConfigPanel removed = columnConfigs.remove(columnConfigs.size() - 1);
            columnsPanel.remove(removed);
            columnsPanel.revalidate();
            columnsPanel.repaint();
        }
    }

    private void saveTable() {
        String tableName = tableNameField.getText().trim();
        if (tableName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Table name is required");
            return;
        }

        List<Column> columns = new ArrayList<>();
        for (ColumnConfigPanel config : columnConfigs) {
            Column col = config.toColumn();
            if (columns.stream().anyMatch(c -> c.getName().equals(col.getName()))) {
                JOptionPane.showMessageDialog(this, "Duplicate column name: " + col.getName());
                return;
            }
            columns.add(col);
        }

        try {
            db.createTable(tableName, columns);
            onCreateCallback.run();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
