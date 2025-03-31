package org.assignment.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties;

    static {
        try (FileInputStream fileInput = new FileInputStream("src/main/resources/config.properties")) {
            properties = new Properties();
            properties.load(fileInput);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties file", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
