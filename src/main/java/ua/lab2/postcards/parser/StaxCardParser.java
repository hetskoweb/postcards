package ua.lab2.postcards.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import ua.lab2.postcards.model.CardBuilder;
import ua.lab2.postcards.model.OldCard;

public class StaxCardParser extends AbstractCardParser {

    @Override
    protected List<OldCard> parseStream(InputStream in) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        XMLStreamReader reader = factory.createXMLStreamReader(in);
        try {
            return readCards(reader);
        } finally {
            reader.close();
        }
    }

    private List<OldCard> readCards(XMLStreamReader reader) throws Exception {
        List<OldCard> cards = new ArrayList<>();
        CardBuilder builder = null;
        StringBuilder text = new StringBuilder();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                text.setLength(0);
                String name = reader.getLocalName();
                if (CardBuilder.TAG_CARD.equals(name)) {
                    builder = new CardBuilder().id(reader.getAttributeValue(null, CardBuilder.ATTR_ID));
                } else if (CardBuilder.TAG_TYPE.equals(name)) {
                    builder.sent(reader.getAttributeValue(null, CardBuilder.ATTR_SENT));
                }
            } else if (event == XMLStreamConstants.CHARACTERS) {
                text.append(reader.getText());
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                String name = reader.getLocalName();
                if (CardBuilder.TAG_CARD.equals(name)) {
                    cards.add(builder.build());
                } else if (builder != null) {
                    builder.field(name, text.toString());
                }
            }
        }
        return cards;
    }
}
