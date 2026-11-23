package ua.lab2.postcards.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CardParseExceptionTest {
    @Test
    public void messageOnly() {
        CardParseException e = new CardParseException("m");
        assertEquals("m", e.getMessage());
        assertNull(e.getCause());
    }
}
