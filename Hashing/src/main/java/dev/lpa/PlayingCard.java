package dev.lpa;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PlayingCard {

    private String suit;

    private String face;

    private int internalHash;

    public PlayingCard(String suit, String face) {
        this.suit = suit;
        this.face = face;
        this.internalHash = (suit.equals("Hearts")) ? 11 : 12;
    }

    @Override
    public String toString() {
        return face + " of " + suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayingCard that = (PlayingCard) o;

        return Objects.equals(suit, that.suit) && Objects.equals(face, that.face);
    }

    @Override
    public int hashCode() {
        int result = suit != null ? suit.hashCode() : 0;
        result = 31 * result + (face != null ? face.hashCode() : 0);
        return result;
    }
}
