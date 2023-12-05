import java.util.Comparator;

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
    
    public Rank getRank() {return rank; }
    public Suit getSuit() {return suit; }

    public int getRankValue() {return rank.getValue(); }

    public int getSuitValue() {return suit.getValue(); }

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
        return Suit.values()[s];
    }

    public static Comparator<Card> compareByRank = new Comparator<Card>() {
        @Override
        public int compare(Card c1, Card c2) {
            return Integer.compare(c1.rank.getValue(), c2.rank.getValue());
        }
    };

    public static Comparator<Card> compareBySuit = new Comparator<Card>() {
        @Override
        public int compare(Card c1, Card c2) {
            return Integer.compare(c1.suit.getValue(), c2.suit.getValue());
        }
    };

    public String toString() {
        return rank.toString() + " " + suit.toString();
    }

}
