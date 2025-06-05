package com.example.transport;

import com.example.general.Message;

public interface MessageReader {
    Message read() throws Exception;
}
