package com.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private final Properties properties = new Properties();

    public ConfigLoader(String filename) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                throw new RuntimeException("Configuration file not found: " + filename);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration file", e);
        }
    }

    public int getInt(String key) {
        String value = properties.getProperty(key);
        if (value == null)
            throw new RuntimeException("Missing configuration parameter: " + key);
        return Integer.parseInt(value);
    }

    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) return defaultValue;
        return Integer.parseInt(value);
    }

    public boolean getBoolean(String key) {
        String value = properties.getProperty(key);
        if (value == null)
            throw new RuntimeException("Missing configuration parameter: " + key);
        return Boolean.parseBoolean(value);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) return defaultValue;
        return Boolean.parseBoolean(value);
    }
}
