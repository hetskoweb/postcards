package ua.lab2.postcards.model;

import java.util.ArrayList;
import java.util.List;

public class CardBuilder {
    public static final String TAG_CARD = "OldCard";
    public static final String TAG_THEMA = "Thema";
    public static final String TAG_TYPE = "Type";
    public static final String TAG_COUNTRY = "Country";
    public static final String TAG_YEAR = "Year";
    public static final String TAG_AUTHOR = "Author";
    public static final String TAG_VALUABLE = "Valuable";
    public static final String ATTR_ID = "id";
    public static final String ATTR_SENT = "sent";

    private String id;
    private Thema thema;
    private CardKind kind;
    private boolean sent;
    private String country;
    private int year;
    private final List<String> authors = new ArrayList<>();
    private Valuable valuable;

    public CardBuilder id(String value) {
        this.id = value;
        return this;
    }

    public CardBuilder sent(String value) {
        this.sent = Boolean.parseBoolean(value.trim());
        return this;
    }

    public CardBuilder field(String tag, String text) {
        String value = text.trim();
        switch (tag) {
            case TAG_THEMA -> thema = Thema.valueOf(value);
            case TAG_TYPE -> kind = CardKind.valueOf(value);
            case TAG_COUNTRY -> country = value;
            case TAG_YEAR -> year = Integer.parseInt(value);
            case TAG_AUTHOR -> authors.add(value);
            case TAG_VALUABLE -> valuable = Valuable.valueOf(value);
            default -> {}
        }
        return this;
    }

    public OldCard build() {
        return new OldCard(id, thema, kind, sent, country, year, authors, valuable);
    }
}
