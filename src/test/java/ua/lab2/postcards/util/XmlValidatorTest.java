package ua.lab2.postcards.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ua.lab2.postcards.TestData.XML;
import static ua.lab2.postcards.TestData.XSD;
import static ua.lab2.postcards.TestData.card;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xml.sax.SAXException;
import ua.lab2.postcards.TestData;

public class XmlValidatorTest {
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private final XmlValidator validator = new XmlValidator();

    private List<String> validateXml(String xml) throws Exception {
        return validator.validate(TestData.write(tmp.getRoot().toPath(), "t.xml", xml), XSD);
    }

    private static String withYear(String year) {
        return card("card-1", "NATURE", "true", "GREETING", "Ukraine", year, "HISTORICAL");
    }

    @Test
    public void sampleFileIsValid() throws Exception {
        assertTrue(validator.validate(XML, XSD).isEmpty());
    }

    @Test
    public void yearBoundariesAreInclusive() throws Exception {
        assertTrue(validateXml(withYear("1840")).isEmpty());
        assertTrue(validateXml(withYear("1980")).isEmpty());
    }

    @Test
    public void yearOutsideBoundariesIsInvalid() throws Exception {
        assertFalse(validateXml(withYear("1839")).isEmpty());
        assertFalse(validateXml(withYear("1981")).isEmpty());
    }

    @Test
    public void unknownEnumValueIsInvalid() throws Exception {
        assertFalse(validateXml(card("card-1", "BOGUS", "true", "GREETING", "X", "1900", "HISTORICAL")).isEmpty());
        assertFalse(validateXml(card("card-1", "NATURE", "true", "BOGUS", "Xx", "1900", "HISTORICAL")).isEmpty());
        assertFalse(validateXml(card("card-1", "NATURE", "true", "GREETING", "Xx", "1900", "BOGUS")).isEmpty());
    }

    @Test
    public void missingRequiredAttributeIsInvalid() throws Exception {
        assertFalse(validateXml(TestData.validCard().replace(" sent=\"true\"", "")).isEmpty());
        assertFalse(validateXml(TestData.validCard().replace(" id=\"card-1\"", "")).isEmpty());
    }

    @Test
    public void duplicateIdIsInvalid() throws Exception {
        String one = TestData.validCard();
        String inner = one.substring(one.indexOf("<OldCard"), one.indexOf("</OldCards>"));
        assertFalse(validateXml("<OldCards>" + inner + inner + "</OldCards>").isEmpty());
    }

    @Test
    public void idPatternIsEnforced() throws Exception {
        assertFalse(validateXml(TestData.validCard().replace("card-1", "bad")).isEmpty());
    }

    @Test
    public void shortCountryIsInvalid() throws Exception {
        assertFalse(validateXml(card("card-1", "NATURE", "true", "GREETING", "X", "1900", "HISTORICAL")).isEmpty());
    }

    @Test
    public void emptyAuthorsContainerIsInvalid() throws Exception {
        assertFalse(validateXml(TestData.validCard().replace("<Valuable>", "<Authors/><Valuable>")).isEmpty());
    }

    @Test
    public void notWellFormedXmlIsReported() throws Exception {
        assertFalse(validateXml("<OldCards><OldCard>").isEmpty());
    }

    @Test(expected = IOException.class)
    public void missingXmlFileFails() throws Exception {
        validator.validate(Path.of("no/such.xml"), XSD);
    }

    @Test(expected = SAXException.class)
    public void brokenSchemaFails() throws Exception {
        Path bad = TestData.write(tmp.getRoot().toPath(), "bad.xsd", "<not-a-schema/>");
        validator.validate(XML, bad);
    }
}
