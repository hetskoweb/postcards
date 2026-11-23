package ua.lab2.postcards.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ua.lab2.postcards.model.CardBuilder;
import ua.lab2.postcards.model.OldCard;

public class DomCardParser extends AbstractCardParser {

    @Override
    protected List<OldCard> parseStream(InputStream in) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        Document doc = factory.newDocumentBuilder().parse(in);

        NodeList nodes = doc.getElementsByTagName(CardBuilder.TAG_CARD);
        List<OldCard> cards = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            cards.add(readCard((Element) nodes.item(i)));
        }
        return cards;
    }

    private OldCard readCard(Element card) {
        CardBuilder builder = new CardBuilder().id(card.getAttribute(CardBuilder.ATTR_ID));
        NodeList children = card.getElementsByTagName("*");
        for (int i = 0; i < children.getLength(); i++) {
            Element e = (Element) children.item(i);
            if (CardBuilder.TAG_TYPE.equals(e.getTagName())) {
                builder.sent(e.getAttribute(CardBuilder.ATTR_SENT));
            }
            builder.field(e.getTagName(), e.getTextContent());
        }
        return builder.build();
    }
}
