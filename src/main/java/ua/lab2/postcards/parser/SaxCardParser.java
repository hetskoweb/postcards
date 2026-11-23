package ua.lab2.postcards.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import ua.lab2.postcards.model.CardBuilder;
import ua.lab2.postcards.model.OldCard;

public class SaxCardParser extends AbstractCardParser {

    @Override
    protected List<OldCard> parseStream(InputStream in) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        CardHandler handler = new CardHandler();
        factory.newSAXParser().parse(in, handler);
        return handler.cards;
    }

    static class CardHandler extends DefaultHandler {
        private final List<OldCard> cards = new ArrayList<>();
        private final StringBuilder text = new StringBuilder();
        private CardBuilder builder;

        @Override
        public void startElement(String uri, String local, String qName, Attributes attrs) {
            text.setLength(0);
            if (CardBuilder.TAG_CARD.equals(qName)) {
                builder = new CardBuilder().id(attrs.getValue(CardBuilder.ATTR_ID));
            } else if (CardBuilder.TAG_TYPE.equals(qName)) {
                builder.sent(attrs.getValue(CardBuilder.ATTR_SENT));
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            text.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String local, String qName) {
            if (CardBuilder.TAG_CARD.equals(qName)) {
                cards.add(builder.build());
            } else if (builder != null) {
                builder.field(qName, text.toString());
            }
        }
    }
}
