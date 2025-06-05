package com.example.gui;

import com.example.general.Message;

public interface ChatEventListener {
    void onMessageReceived(Message message);
    void onSystemMessage(String message);
}
