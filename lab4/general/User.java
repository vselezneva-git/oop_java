package com.example.general;

import java.io.Serializable;


public class User implements Serializable {
    private String name;
    private String type;
    private String sessionId;

    public User(String name, String type, String sessionId) {
        this.name = name;
        this.type = type;
        this.sessionId = sessionId;
    }

    public User(String name) {
        this.name = name;
        this.type = "ObjectClient";
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
