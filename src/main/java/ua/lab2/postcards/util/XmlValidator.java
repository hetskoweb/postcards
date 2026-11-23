package ua.lab2.postcards.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlValidator {
    private static final Logger LOG = Logger.getLogger(XmlValidator.class.getName());

    /**
     * @return
     * @throws IOException
     * @throws SAXException
     */
    public List<String> validate(Path xmlFile, Path xsdFile) throws IOException, SAXException {
        Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                .newSchema(xsdFile.toFile());
        Validator validator = schema.newValidator();
        List<String> errors = new ArrayList<>();
        validator.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException e) {
                LOG.warning(describe(e));
            }

            @Override
            public void error(SAXParseException e) {
                errors.add(describe(e));
            }

            @Override
            public void fatalError(SAXParseException e) {
                errors.add(describe(e));
            }
        });
        try {
            validator.validate(new StreamSource(xmlFile.toFile()));
        } catch (SAXParseException fatal) {
            LOG.fine("Fatal XML error: " + fatal.getMessage());
        }
        if (errors.isEmpty()) {
            LOG.info("XML is valid: " + xmlFile);
        } else {
            errors.forEach(e -> LOG.warning("Validation error: " + e));
        }
        return errors;
    }

    private static String describe(SAXParseException e) {
        return "line " + e.getLineNumber() + ": " + e.getMessage();
    }
}
