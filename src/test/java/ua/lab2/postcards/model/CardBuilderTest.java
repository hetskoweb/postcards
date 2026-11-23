package ua.lab2.postcards.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Test;

public class CardBuilderTest {
    @Test
    public void buildsCardFromTextValues() {
        OldCard c = new CardBuilder().id("card-9").sent(" true ")
                .field("Thema", " SPORT ").field("Type", "ADVERTISING").field("Country", "Poland")
                .field("Year", "1950").field("Author", "A B").field("Author", "C D")
                .field("Authors", "ignored container").field("Valuable", "COLLECTIBLE").build();
        assertEquals("card-9", c.getId());
        assertTrue(c.isSent());
        assertEquals(Thema.SPORT, c.getThema());
        assertEquals(CardKind.ADVERTISING, c.getKind());
        assertEquals("Poland", c.getCountry());
        assertEquals(1950, c.getYear());
        assertEquals(List.of("A B", "C D"), c.getAuthors());
        assertEquals(Valuable.COLLECTIBLE, c.getValuable());
    }

    @Test
    public void sentFalse() {
        assertFalse(new CardBuilder().sent("false").build().isSent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void unknownEnumValueFails() {
        new CardBuilder().field("Thema", "UNKNOWN");
    }

    @Test(expected = NumberFormatException.class)
    public void badYearFails() {
        new CardBuilder().field("Year", "abc");
    }
}
