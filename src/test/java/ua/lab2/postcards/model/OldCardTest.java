package ua.lab2.postcards.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class OldCardTest {
    private static OldCard card(String id, int year) {
        return new OldCard(id, Thema.NATURE, CardKind.GREETING, true, "Ukraine", year,
                List.of("A B"), Valuable.THEMATIC);
    }

    @Test
    public void gettersReturnConstructorValues() {
        OldCard c = card("card-1", 1900);
        assertEquals("card-1", c.getId());
        assertEquals(Thema.NATURE, c.getThema());
        assertEquals(CardKind.GREETING, c.getKind());
        assertTrue(c.isSent());
        assertEquals("Ukraine", c.getCountry());
        assertEquals(1900, c.getYear());
        assertEquals(List.of("A B"), c.getAuthors());
        assertEquals(Valuable.THEMATIC, c.getValuable());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void authorsListIsImmutable() {
        card("card-1", 1900).getAuthors().add("X");
    }

    @Test
    public void authorsAreCopiedDefensively() {
        List<String> authors = new ArrayList<>(List.of("A B"));
        OldCard c = new OldCard("card-1", Thema.SPORT, CardKind.ORDINARY, false, "X", 1900,
                authors, Valuable.HISTORICAL);
        authors.add("C D");
        assertEquals(1, c.getAuthors().size());
    }

    @Test
    public void equalsAndHashCode() {
        OldCard a = card("card-1", 1900);
        assertEquals(a, a);
        assertEquals(a, card("card-1", 1900));
        assertEquals(a.hashCode(), card("card-1", 1900).hashCode());
        assertNotEquals(a, card("card-1", 1901));
        assertNotEquals(a, card("card-2", 1900));
        assertNotEquals(a, null);
        assertNotEquals(a, "card");
    }

    @Test
    public void toStringContainsFields() {
        String s = card("card-1", 1900).toString();
        assertTrue(s.contains("card-1") && s.contains("1900") && s.contains("NATURE"));
    }
}
