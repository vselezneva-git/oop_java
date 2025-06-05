package com.example.transport;

import com.example.general.Message;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ObjectMessageReader implements MessageReader {

    private final ObjectInputStream in;

    public ObjectMessageReader(ObjectInputStream in) {
        this.in = in;
    }

    @Override
    public Message read() throws IOException, ClassNotFoundException {
        return (Message) in.readObject();
    }
}
