package ua.lab2.postcards.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ua.lab2.postcards.TestData.XML;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import ua.lab2.postcards.TestData;
import ua.lab2.postcards.model.CardKind;
import ua.lab2.postcards.model.OldCard;
import ua.lab2.postcards.model.Thema;
import ua.lab2.postcards.model.Valuable;

@RunWith(Parameterized.class)
public class CardParsersTest {
    @Parameters(name = "{0}")
    public static Collection<Object[]> parsers() {
        return Arrays.asList(new Object[][]{
                {"SAX", new SaxCardParser()}, {"DOM", new DomCardParser()}, {"StAX", new StaxCardParser()}});
    }

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private final CardParser parser;

    public CardParsersTest(String name, CardParser parser) {
        this.parser = parser;
    }

    @Test
    public void parsesAllCardsFromSampleFile() throws Exception {
        List<OldCard> cards = parser.parse(XML);
        assertEquals(8, cards.size());
        OldCard first = cards.get(0);
        assertEquals("card-1", first.getId());
        assertEquals(Thema.ARCHITECTURE, first.getThema());
        assertEquals(CardKind.GREETING, first.getKind());
        assertTrue(first.isSent());
        assertEquals("Ukraine", first.getCountry());
        assertEquals(1912, first.getYear());
        assertEquals(List.of("Ivan Trush"), first.getAuthors());
        assertEquals(Valuable.HISTORICAL, first.getValuable());
    }

    @Test
    public void cardWithoutAuthorsHasEmptyList() throws Exception {
        assertTrue(parser.parse(XML).get(1).getAuthors().isEmpty());
    }

    @Test
    public void cardWithSeveralAuthors() throws Exception {
        assertEquals(List.of("Hans Müller", "Karl Weiss"), parser.parse(XML).get(2).getAuthors());
    }

    @Test
    public void allParsersGiveSameResult() throws Exception {
        List<OldCard> expected = new SaxCardParser().parse(XML);
        assertEquals(expected, parser.parse(XML));
    }

    @Test(expected = CardParseException.class)
    public void missingFileFails() throws Exception {
        parser.parse(Path.of("no/such/file.xml"));
    }

    @Test(expected = CardParseException.class)
    public void malformedXmlFails() throws Exception {
        parser.parse(TestData.write(tmp.getRoot().toPath(), "bad.xml", "<OldCards><OldCard>"));
    }

    @Test(expected = CardParseException.class)
    public void doctypeIsRejected() throws Exception {
        String xxe = "<!DOCTYPE x [<!ENTITY e SYSTEM \"file:///etc/passwd\">]>"
                + TestData.validCard().replace("Ukraine", "&e;");
        parser.parse(TestData.write(tmp.getRoot().toPath(), "xxe.xml", xxe));
    }

    @Test(expected = CardParseException.class)
    public void unknownEnumValueFails() throws Exception {
        String xml = TestData.card("card-1", "BOGUS", "true", "GREETING", "X", "1900", "HISTORICAL");
        parser.parse(TestData.write(tmp.getRoot().toPath(), "enum.xml", xml));
    }

    @Test
    public void exceptionKeepsCause() throws Exception {
        try {
            parser.parse(Path.of("no/such/file.xml"));
        } catch (CardParseException e) {
            assertNotNull(e.getCause());
            return;
        }
        throw new AssertionError("expected exception");
    }
}
