package ua.lab2.postcards.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

public class CardComparatorsTest {
    private static OldCard c(String id, Thema t, String country, int year) {
        return new OldCard(id, t, CardKind.ORDINARY, false, country, year, List.of(), Valuable.HISTORICAL);
    }

    private final List<OldCard> cards = List.of(
            c("card-3", Thema.SPORT, "Poland", 1920),
            c("card-1", Thema.NATURE, "Austria", 1920),
            c("card-2", Thema.NATURE, "Poland", 1900));

    private List<String> sortedIds(java.util.Comparator<OldCard> cmp) {
        List<OldCard> copy = new ArrayList<>(cards);
        copy.sort(cmp);
        return copy.stream().map(OldCard::getId).collect(Collectors.toList());
    }

    @Test
    public void byYearThenId() {
        assertEquals(List.of("card-2", "card-1", "card-3"), sortedIds(CardComparators.BY_YEAR));
    }

    @Test
    public void byCountryThenYear() {
        assertEquals(List.of("card-1", "card-2", "card-3"), sortedIds(CardComparators.BY_COUNTRY_THEN_YEAR));
    }

    @Test
    public void byThemaThenYear() {
        assertEquals(List.of("card-2", "card-1", "card-3"), sortedIds(CardComparators.BY_THEMA_THEN_YEAR));
    }
}
