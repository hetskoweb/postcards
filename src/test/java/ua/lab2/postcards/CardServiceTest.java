package ua.lab2.postcards;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ua.lab2.postcards.TestData.XML;
import static ua.lab2.postcards.TestData.XSD;
import static ua.lab2.postcards.TestData.XSL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ua.lab2.postcards.model.CardComparators;
import ua.lab2.postcards.model.OldCard;
import ua.lab2.postcards.parser.CardParseException;
import ua.lab2.postcards.parser.DomCardParser;

public class CardServiceTest {
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private final CardService service = new CardService();

    @Test
    public void validatesSampleFile() throws Exception {
        service.validate(XML, XSD);
    }

    @Test(expected = CardParseException.class)
    public void invalidXmlIsRejected() throws Exception {
        service.validate(TestData.write(tmp.getRoot().toPath(), "i.xml", "<OldCards/>"), XSD);
    }

    @Test(expected = CardParseException.class)
    public void missingXmlIsRejected() throws Exception {
        service.validate(Path.of("missing.xml"), XSD);
    }

    @Test
    public void loadSortsCardsByComparator() throws Exception {
        List<OldCard> cards = service.load(XML, new DomCardParser(), CardComparators.BY_YEAR);
        assertEquals(1840, cards.get(0).getYear());
        for (int i = 1; i < cards.size(); i++) {
            assertTrue(cards.get(i - 1).getYear() <= cards.get(i).getYear());
        }
    }

    @Test
    public void loadSortsByCountry() throws Exception {
        List<OldCard> cards = service.load(XML, new DomCardParser(), CardComparators.BY_COUNTRY_THEN_YEAR);
        assertEquals("Austria", cards.get(0).getCountry());
    }

    @Test
    public void transformWritesFile() throws Exception {
        Path out = tmp.getRoot().toPath().resolve("o.xml");
        service.transform(XML, XSL, out);
        assertTrue(Files.size(out) > 0);
    }

    @Test(expected = CardParseException.class)
    public void transformWithBrokenStylesheetFails() throws Exception {
        Path bad = TestData.write(tmp.getRoot().toPath(), "b.xsl", "<x");
        service.transform(XML, bad, tmp.getRoot().toPath().resolve("o.xml"));
    }
}
