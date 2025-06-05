package com.example.transport;

import com.example.general.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ObjectMessageWriter implements MessageWriter {

    private final ObjectOutputStream out;

    public ObjectMessageWriter(ObjectOutputStream out) {
        this.out = out;
    }

    @Override
    public void write(Message message) throws IOException {
        out.writeObject(message);
        out.flush();
    }
}
