package ua.lab2.postcards.parser;

public class CardParseException extends Exception {
    public CardParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public CardParseException(String message) {
        super(message);
    }
}
