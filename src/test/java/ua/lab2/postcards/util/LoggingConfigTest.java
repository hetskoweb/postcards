package ua.lab2.postcards.util;

import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;
import org.junit.Test;

public class LoggingConfigTest {
    @Test
    public void initConfiguresLoggerAndCreatesLogDirectory() throws Exception {
        LoggingConfig.init();
        Logger.getLogger("ua.lab2.postcards.test").info("logging works");
        assertTrue(Files.isDirectory(Path.of("logs")));
        assertTrue(Logger.getLogger("ua.lab2").getLevel() != null);
    }
}
