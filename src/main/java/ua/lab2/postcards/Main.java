package ua.lab2.postcards;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.lab2.postcards.model.CardComparators;
import ua.lab2.postcards.model.OldCard;
import ua.lab2.postcards.parser.CardParseException;
import ua.lab2.postcards.parser.CardParser;
import ua.lab2.postcards.parser.DomCardParser;
import ua.lab2.postcards.parser.SaxCardParser;
import ua.lab2.postcards.parser.StaxCardParser;
import ua.lab2.postcards.util.LoggingConfig;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {
        LoggingConfig.init();
        Path xml = Path.of(args.length > 0 ? args[0] : "data/cards.xml");
        Path xsd = Path.of(args.length > 1 ? args[1] : "data/cards.xsd");
        Path xsl = Path.of(args.length > 2 ? args[2] : "data/cards-by-thema.xsl");
        Path out = Path.of(args.length > 3 ? args[3] : "data/cards-by-thema.xml");
        try {
            run(xml, xsd, xsl, out);
        } catch (CardParseException e) {
            LOG.log(Level.SEVERE, "Processing failed", e);
            System.exit(1);
        }
    }

    static Map<String, List<OldCard>> run(Path xml, Path xsd, Path xsl, Path out)
            throws CardParseException {
        CardService service = new CardService();
        service.validate(xml, xsd);

        Map<String, CardParser> parsers = Map.of(
                "SAX", new SaxCardParser(),
                "DOM", new DomCardParser(),
                "StAX", new StaxCardParser());
        Map<String, List<OldCard>> results = new java.util.TreeMap<>();
        for (Map.Entry<String, CardParser> p : parsers.entrySet()) {
            List<OldCard> cards = service.load(xml, p.getValue(), CardComparators.BY_YEAR);
            cards.forEach(c -> LOG.fine(p.getKey() + ": " + c));
            results.put(p.getKey(), cards);
        }
        service.transform(xml, xsl, out);
        return results;
    }
}
