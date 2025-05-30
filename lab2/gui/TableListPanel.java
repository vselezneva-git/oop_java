package com.example.db.gui;

import com.example.db.engine.Database;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TableListPanel extends JPanel {
    private final Database db;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> tableList = new JList<>(listModel);

    public TableListPanel(Database db, Consumer<String> onTableSelected, Runnable onCreateTable, Consumer<String> onDropTable) {
        this.db = db;
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(tableList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 2));
        JButton newBtn = new JButton("New Table");
        JButton dropBtn = new JButton("Drop Table");
        buttons.add(newBtn);
        buttons.add(dropBtn);

        add(buttons, BorderLayout.SOUTH);

        tableList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = tableList.getSelectedValue();
                if (selected != null) onTableSelected.accept(selected);
            }
        });

        newBtn.addActionListener(e -> onCreateTable.run());
        dropBtn.addActionListener(e -> {
            String selected = tableList.getSelectedValue();
            if (selected != null) onDropTable.accept(selected);
        });
    }

    public void refreshTableList() {
        listModel.clear();
        for (String name : db.listTableName()) {
            listModel.addElement(name);
        }
    }
}
