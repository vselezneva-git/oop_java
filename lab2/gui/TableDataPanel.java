package com.example.db.gui;

import com.example.db.engine.Column;
import com.example.db.engine.Row;
import com.example.db.engine.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TableDataPanel extends JPanel {
    private final DefaultTableModel model = new DefaultTableModel();
    private final JTable table = new JTable(model);

    public TableDataPanel() {
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void showTable(Table t) {
        model.setColumnCount(0);
        for (Column col : t.getColumns()) {
            model.addColumn(col.getName());
        }

        model.setRowCount(0);
        for (Row row : t.getRows()) {
            model.addRow(row.getValues().toArray());
        }
    }

    public void clearTable() {
        model.setRowCount(0);
        model.setColumnCount(0);
    }
}
