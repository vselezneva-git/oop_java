package com.example.stackcalc;

import java.io.InputStream;
import java.util.Properties;

public class CommandFactory {
    private final Properties props = new Properties();

    public CommandFactory() {
        try (InputStream in = getClass().getResourceAsStream("/commands.properties")) {
            if (in == null) {
                throw new RuntimeException("commands.properties not found in classpath");
            }
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load commands.properties", e);
        }
    }

    public Command create(String name) throws CommandException {
        String className = props.getProperty(name);
        if (className == null) {
            throw new CommandException("Unknown command: " + name);
        }
        try {
            Class<?> cls = Class.forName(className);
            return (Command) cls.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new CommandException("Error creating command " + name, e);
        }
    }
}
