package ua.lab2.postcards.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ua.lab2.postcards.TestData.XML;
import static ua.lab2.postcards.TestData.XSD;
import static ua.lab2.postcards.TestData.XSL;

import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ua.lab2.postcards.TestData;

public class XslTransformerTest {
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private Document transform() throws Exception {
        Path out = tmp.getRoot().toPath().resolve("out.xml");
        new XslTransformer().transform(XML, XSL, out);
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(out.toFile());
    }

    @Test
    public void cardsAreGroupedByThemaAndSorted() throws Exception {
        Document doc = transform();
        NodeList groups = doc.getElementsByTagName("Thema");
        assertEquals(6, groups.getLength());
        Element first = (Element) groups.item(0);
        assertEquals("ARCHITECTURE", first.getAttribute("name"));
        assertEquals("2", first.getAttribute("count"));
        NodeList inside = first.getElementsByTagName("OldCard");
        assertEquals("card-1", ((Element) inside.item(0)).getAttribute("id"));
        assertEquals("card-4", ((Element) inside.item(1)).getAttribute("id"));
    }

    @Test
    public void allCardsArePreservedAndThemaIsNotDuplicated() throws Exception {
        Document doc = transform();
        assertEquals(8, doc.getElementsByTagName("OldCard").getLength());
        for (int i = 0; i < doc.getElementsByTagName("OldCard").getLength(); i++) {
            Element card = (Element) doc.getElementsByTagName("OldCard").item(i);
            assertEquals(0, card.getElementsByTagName("Thema").getLength());
            assertEquals(1, card.getElementsByTagName("Year").getLength());
        }
    }

    @Test(expected = TransformerException.class)
    public void brokenStylesheetFails() throws Exception {
        Path bad = TestData.write(tmp.getRoot().toPath(), "bad.xsl", "<broken");
        new XslTransformer().transform(XML, bad, tmp.getRoot().toPath().resolve("o.xml"));
    }

    @Test
    public void outputPreservesSentAttribute() throws Exception {
        Element type = (Element) transform().getElementsByTagName("Type").item(0);
        assertTrue(type.hasAttribute("sent"));
        assertTrue(XSD.toFile().exists());
    }
}
