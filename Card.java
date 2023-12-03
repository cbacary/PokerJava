public class Card {

    /*
     * Playing cards are represented using an integer where each card type is
     * shifted one bit down. This makes checks much simpler because we can
     * just do bitwise operations.
     * */

    private int value;

    private Rank rank;
    private Suit suit;

    Card(int v) {
        value = v;
        rank = intToRank(value);
        suit = intToSuit(value);
    }

    Card(Rank r, Suit s) {
        rank = r;
        suit = s;
        value = r.getValue() | s.getValue();
    }

    public int getValue() { return value; }

    public static boolean isCard(Card card, int other) {
        return (card.value & other) == other;
    }

    public static boolean isCard(Card card, Card other) {
        return (card.value & other.value) == other.value;
    }

    public boolean isCard(int other) { return (value & other) == other; }

    public boolean isCard(Card other) {
        return (value & other.value) == other.value;
    }

    public static Rank intToRank(int r) {
        for (Rank rank : Rank.values()) {
            if ((rank.getValue() & r) != 0) {
                return rank;
            }
        }
        throw new IllegalArgumentException("No rank with given int: " + r);
    }

    public static Suit intToSuit(int s) {
        for (Suit suit: Suit.values()) {
            if ((suit.getValue() & s) != 0) {
                return suit;
            }
        }
        throw new IllegalArgumentException("No rank with given int: " + s);
    }

    public String toString() {
        return rank.toString() + " " + suit.toString();
    }

}
