package ua.lab2.postcards;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class TestData {
    public static final Path XML = Path.of("data/cards.xml");
    public static final Path XSD = Path.of("data/cards.xsd");
    public static final Path XSL = Path.of("data/cards-by-thema.xsl");

    private TestData() {
    }

    public static String card(String id, String thema, String sent, String kind, String country,
                              String year, String valuable) {
        return "<OldCards><OldCard id=\"" + id + "\"><Thema>" + thema + "</Thema><Type sent=\"" + sent
                + "\">" + kind + "</Type><Country>" + country + "</Country><Year>" + year
                + "</Year><Valuable>" + valuable + "</Valuable></OldCard></OldCards>";
    }

    public static String validCard() {
        return card("card-1", "NATURE", "true", "GREETING", "Ukraine", "1900", "HISTORICAL");
    }

    public static Path write(Path dir, String name, String content) throws IOException {
        return Files.writeString(dir.resolve(name), content);
    }
}
