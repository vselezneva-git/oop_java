package com.example.objectChat;

import com.example.general.Message;
import com.example.gui.ChatEventListener;
import com.example.gui.ChatWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.Socket;

public class ObjectChatClient implements ChatEventListener {

    private final String username;
    private ObjectClientConnection connection;
    private ChatWindow chatWindow;

    public ObjectChatClient(String username, String serverAddress, int port) {
        this.username = username;

        try {
            Socket socket = new Socket(serverAddress, port);
            chatWindow = new ChatWindow(username, this::handleSendClick);
            connection = new ObjectClientConnection(socket, this, username);


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Не удалось подключиться к серверу: " + e.getMessage(),
                    "ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSendClick(ActionEvent e) {
        String text = chatWindow.getInputText().trim();
        if (!text.isEmpty()) {
            Message message = new Message(username, text);
            connection.sendMessage(message);
            chatWindow.clearInput();
        }
    }

    @Override
    public void onMessageReceived(Message message) {
        if ("СЕРВЕР".equals(message.getSender())) {
            chatWindow.displaySystemMessage(message.getContent());
        } else {
            chatWindow.displayMessage(message);
        }
    }


    @Override
    public void onSystemMessage(String msg) {
        chatWindow.displaySystemMessage(msg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String name = JOptionPane.showInputDialog("Введите имя:");
            if (name != null && !name.trim().isEmpty()) {
                new ObjectChatClient(name.trim(), "localhost", 12345);
            }
        });
    }
}
