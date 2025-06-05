package com.example.transport;

import com.example.general.Message;

public interface MessageWriter {
    void write(Message message) throws Exception;
}
