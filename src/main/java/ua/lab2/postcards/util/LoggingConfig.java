package ua.lab2.postcards.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.LogManager;

public final class LoggingConfig {
    private static final String RESOURCE = "/logging.properties";

    private LoggingConfig() {
    }

    public static void init() throws IOException {
        Files.createDirectories(Path.of("logs"));
        try (InputStream in = LoggingConfig.class.getResourceAsStream(RESOURCE)) {
            if (in == null) {
                throw new IOException("Missing resource " + RESOURCE);
            }
            LogManager.getLogManager().readConfiguration(in);
        }
    }
}
