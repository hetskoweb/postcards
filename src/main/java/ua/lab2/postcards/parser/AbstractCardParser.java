package ua.lab2.postcards.parser;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.lab2.postcards.model.OldCard;

public abstract class AbstractCardParser implements CardParser {
    private static final Logger LOG = Logger.getLogger(AbstractCardParser.class.getName());

    @Override
    public final List<OldCard> parse(Path xmlFile) throws CardParseException {
        String name = getClass().getSimpleName();
        LOG.log(Level.INFO, "{0}: parsing {1}", new Object[]{name, xmlFile});
        try (InputStream in = Files.newInputStream(xmlFile)) {
            List<OldCard> cards = parseStream(in);
            LOG.log(Level.INFO, "{0}: parsed {1} cards", new Object[]{name, cards.size()});
            return cards;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, name + ": failed to parse " + xmlFile, e);
            throw new CardParseException("Cannot parse " + xmlFile + " with " + name, e);
        }
    }

    protected abstract List<OldCard> parseStream(InputStream in) throws Exception;
}
