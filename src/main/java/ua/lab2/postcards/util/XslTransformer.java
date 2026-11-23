package ua.lab2.postcards.util;

import java.nio.file.Path;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XslTransformer {
    private static final Logger LOG = Logger.getLogger(XslTransformer.class.getName());

    public void transform(Path xml, Path xsl, Path output) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Transformer transformer = factory.newTransformer(new StreamSource(xsl.toFile()));
        transformer.transform(new StreamSource(xml.toFile()), new StreamResult(output.toFile()));
        LOG.info("Transformed " + xml + " -> " + output);
    }
}
