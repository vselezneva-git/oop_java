package com.example.gui;

import com.example.general.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class ChatWindow extends JFrame {

    private final JTextArea chatArea;
    private final JTextField inputField;
    private final JButton sendButton;

    public ChatWindow(String username, ActionListener onSendClick) {
        super("Чат — " + username);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);

        inputField = new JTextField();
        sendButton = new JButton("Отправить");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(onSendClick);

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public String getInputText() {
        return inputField.getText();
    }

    public void clearInput() {
        inputField.setText("");
    }

    public void displayMessage(Message message) {
        chatArea.append(message.getSender() + ": " + message.getContent() + "\n");
    }

    public void displaySystemMessage(String msg) {
        chatArea.append("[SYSTEM]: " + msg + "\n");
    }
}
