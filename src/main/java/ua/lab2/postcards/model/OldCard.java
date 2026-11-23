package ua.lab2.postcards.model;

import java.util.List;
import java.util.Objects;

public final class OldCard {
    private final String id;
    private final Thema thema;
    private final CardKind kind;
    private final boolean sent;
    private final String country;
    private final int year;
    private final List<String> authors;
    private final Valuable valuable;

    public OldCard(String id, Thema thema, CardKind kind, boolean sent, String country,
                   int year, List<String> authors, Valuable valuable) {
        this.id = id;
        this.thema = thema;
        this.kind = kind;
        this.sent = sent;
        this.country = country;
        this.year = year;
        this.authors = List.copyOf(authors);
        this.valuable = valuable;
    }

    public String getId() { return id; }
    public Thema getThema() { return thema; }
    public CardKind getKind() { return kind; }
    public boolean isSent() { return sent; }
    public String getCountry() { return country; }
    public int getYear() { return year; }
    public List<String> getAuthors() { return authors; }
    public Valuable getValuable() { return valuable; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OldCard)) {
            return false;
        }
        OldCard c = (OldCard) o;
        return sent == c.sent && year == c.year && Objects.equals(id, c.id) && thema == c.thema
                && kind == c.kind && Objects.equals(country, c.country)
                && authors.equals(c.authors) && valuable == c.valuable;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, thema, kind, sent, country, year, authors, valuable);
    }

    @Override
    public String toString() {
        return "OldCard{id=" + id + ", thema=" + thema + ", kind=" + kind + ", sent=" + sent
                + ", country=" + country + ", year=" + year + ", authors=" + authors
                + ", valuable=" + valuable + '}';
    }
}
