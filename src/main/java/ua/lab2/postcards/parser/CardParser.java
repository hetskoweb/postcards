package ua.lab2.postcards.parser;

import java.nio.file.Path;
import java.util.List;
import ua.lab2.postcards.model.OldCard;

public interface CardParser {
    List<OldCard> parse(Path xmlFile) throws CardParseException;
}
