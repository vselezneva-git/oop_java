package com.example.objectChat;

import com.example.general.Message;
import com.example.general.User;
import com.example.transport.ObjectMessageReader;
import com.example.transport.ObjectMessageWriter;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ObjectClientHandler extends Thread {

    private final Socket socket;
    private final ObjectChatServer server;
    private ObjectMessageReader reader;
    private ObjectMessageWriter writer;
    private User user;

    public ObjectClientHandler(Socket socket, ObjectChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public User getUser() {
        return user;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            writer = new ObjectMessageWriter(out);
            reader = new ObjectMessageReader(in);

            Message firstMessage = reader.read();
            this.user = new User(firstMessage.getSender());

            server.broadcastSystemMessage(user.getName() + " подключился к чату.");

            while (true) {
                Message message = reader.read();
                System.out.println("[" + user.getName() + "]: " + message.getContent());
                server.broadcastMessage(message);
            }

        } catch (Exception e) {
            System.out.println("Клиент был отключен: " + e.getMessage());
        } finally {
            server.removeClient(this);
            try {
                socket.close();
            } catch (Exception ignored) {}
        }
    }

    public void send(Message message) {
        try {
            writer.write(message);
        } catch (Exception e) {
            System.err.println("Ошибка отправки: " + e.getMessage());
        }
    }
}
