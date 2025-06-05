package com.example.objectChat;

import com.example.general.User;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ObjectChatServer {

    private static final int PORT = 12345;

    private final List<ObjectClientHandler> clients = new CopyOnWriteArrayList<>();

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер работает на порту " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Новое подключение: " + socket);

                ObjectClientHandler handler = new ObjectClientHandler(socket, this);
                clients.add(handler);
                handler.start();
            }
        } catch (Exception e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
        }
    }

    public void removeClient(ObjectClientHandler handler) {
        clients.remove(handler);
        broadcastSystemMessage(handler.getUser().getName() + " покинул чат.");
    }

    public void broadcastMessage(com.example.general.Message msg) {
        for (ObjectClientHandler client : clients) {
            client.send(msg);
        }
    }

    public void broadcastSystemMessage(String text) {
        com.example.general.Message msg = new com.example.general.Message("СЕРВЕР", text);
        broadcastMessage(msg);
    }

    public static void main(String[] args) {
        new ObjectChatServer().start();
    }
}
