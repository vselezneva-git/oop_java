package com.example.db.gui;

import com.example.db.engine.Column;
import com.example.db.engine.DataType;

import javax.swing.*;
import java.awt.*;

public class ColumnConfigPanel extends JPanel {
    private final JTextField nameField = new JTextField(10);
    private final JComboBox<DataType> typeBox = new JComboBox<>(DataType.values());
    private final JCheckBox uniqueBox = new JCheckBox("unique");
    private final JCheckBox notNullBox = new JCheckBox("not-null");

    public ColumnConfigPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(new JLabel("Name:"));
        add(nameField);

        add(new JLabel("Type:"));
        add(typeBox);

        add(uniqueBox);
        add(notNullBox);
    }

    public Column toColumn() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Column name cannot be empty");
        }

        DataType type = (DataType) typeBox.getSelectedItem();
        boolean isUnique = uniqueBox.isSelected();
        boolean isNotNull = notNullBox.isSelected();

        return new Column(name, type, isNotNull, isUnique);
    }
}
