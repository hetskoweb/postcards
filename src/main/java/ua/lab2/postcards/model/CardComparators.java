package ua.lab2.postcards.model;

import java.util.Comparator;

public final class CardComparators {

    public static final Comparator<OldCard> BY_YEAR =
            Comparator.comparingInt(OldCard::getYear).thenComparing(OldCard::getId);

    public static final Comparator<OldCard> BY_COUNTRY_THEN_YEAR =
            Comparator.comparing(OldCard::getCountry).thenComparing(BY_YEAR);

    public static final Comparator<OldCard> BY_THEMA_THEN_YEAR =
            Comparator.comparing(OldCard::getThema).thenComparing(BY_YEAR);

    private CardComparators() {
    }
}
