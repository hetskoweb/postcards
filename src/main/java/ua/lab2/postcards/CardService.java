package ua.lab2.postcards;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;
import ua.lab2.postcards.model.OldCard;
import ua.lab2.postcards.parser.CardParseException;
import ua.lab2.postcards.parser.CardParser;
import ua.lab2.postcards.util.XmlValidator;
import ua.lab2.postcards.util.XslTransformer;

public class CardService {
    private static final Logger LOG = Logger.getLogger(CardService.class.getName());

    private final XmlValidator validator = new XmlValidator();
    private final XslTransformer transformer = new XslTransformer();

    public void validate(Path xml, Path xsd) throws CardParseException {
        try {
            List<String> errors = validator.validate(xml, xsd);
            if (!errors.isEmpty()) {
                throw new CardParseException("XML is not valid: " + errors);
            }
        } catch (IOException | SAXException e) {
            throw new CardParseException("Cannot validate " + xml, e);
        }
    }

    public List<OldCard> load(Path xml, CardParser parser, Comparator<OldCard> order)
            throws CardParseException {
        List<OldCard> cards = new ArrayList<>(parser.parse(xml));
        cards.sort(order);
        LOG.info(() -> "Loaded " + cards.size() + " cards");
        return cards;
    }

    public void transform(Path xml, Path xsl, Path out) throws CardParseException {
        try {
            transformer.transform(xml, xsl, out);
        } catch (TransformerException e) {
            throw new CardParseException("Cannot transform " + xml, e);
        }
    }
}
