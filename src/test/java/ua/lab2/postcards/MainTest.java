package ua.lab2.postcards;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ua.lab2.postcards.TestData.XML;
import static ua.lab2.postcards.TestData.XSD;
import static ua.lab2.postcards.TestData.XSL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ua.lab2.postcards.model.OldCard;
import ua.lab2.postcards.parser.CardParseException;

public class MainTest {
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void runProducesSameSortedResultForAllParsers() throws Exception {
        Path out = tmp.getRoot().toPath().resolve("out.xml");
        Map<String, List<OldCard>> results = Main.run(XML, XSD, XSL, out);
        assertEquals(3, results.size());
        assertEquals(results.get("SAX"), results.get("DOM"));
        assertEquals(results.get("SAX"), results.get("StAX"));
        assertEquals(1840, results.get("SAX").get(0).getYear());
        assertTrue(Files.size(out) > 0);
    }

    @Test(expected = CardParseException.class)
    public void runFailsOnInvalidXml() throws Exception {
        Path bad = TestData.write(tmp.getRoot().toPath(), "bad.xml", "<OldCards/>");
        Main.run(bad, XSD, XSL, tmp.getRoot().toPath().resolve("o.xml"));
    }

    @Test
    public void mainWithExplicitArguments() throws Exception {
        Path out = tmp.getRoot().toPath().resolve("main-out.xml");
        Main.main(new String[]{XML.toString(), XSD.toString(), XSL.toString(), out.toString()});
        assertTrue(Files.exists(out));
    }

    @Test
    public void mainWithDefaultArguments() throws Exception {
        Main.main(new String[0]);
        assertTrue(Files.exists(Path.of("data/cards-by-thema.xml")));
    }
}
