package com.example.objectChat;

import com.example.general.Message;
import com.example.transport.MessageReader;
import com.example.transport.MessageWriter;
import com.example.transport.ObjectMessageReader;
import com.example.transport.ObjectMessageWriter;
import com.example.gui.ChatEventListener;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ObjectClientConnection {

    private final Socket socket;
    private final MessageReader reader;
    private final MessageWriter writer;
    private final ChatEventListener listener;

    public ObjectClientConnection(Socket socket, ChatEventListener listener, String username) throws Exception {
        this.socket = socket;
        this.listener = listener;

        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        this.writer = new ObjectMessageWriter(out);
        this.reader = new ObjectMessageReader(in);


        Message loginMessage = new Message(username, "вошёл в чат");
        writer.write(loginMessage);

        startMessageListener();
    }

    private void startMessageListener() {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    Message message = reader.read();
                    listener.onMessageReceived(message);
                }
            } catch (Exception e) {
                listener.onSystemMessage("Отключился от сервера: " + e.getMessage());
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void sendMessage(Message message) {
        try {
            writer.write(message);
        } catch (Exception e) {
            listener.onSystemMessage("Ошибка отправки сообщения: " + e.getMessage());
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (Exception ignored) {}
    }
}
