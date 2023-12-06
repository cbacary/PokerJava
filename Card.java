import java.util.Comparator;

public class Card implements Comparable<Card> {
    private Rank rank;
    private Suit suit;

    Card(Rank r, Suit s) {
        rank = r;
        suit = s;
    }

    public Rank getRank() { return rank; }
    public Suit getSuit() { return suit; }

    public int getRankValue() { return rank.getValue(); }

    public int getSuitValue() { return suit.getValue(); }

    public static Rank intToRank(int r) {
        for (Rank rank : Rank.values()) {
            if ((rank.getValue() & r) != 0) {
                return rank;
            }
        }
        throw new IllegalArgumentException("No rank with given int: " + r);
    }

    public static Suit intToSuit(int s) { return Suit.values()[s]; }

    public String toString() { return rank.toString() + " " + suit.toString(); }

    public String getImageFileName() {
        String name = rank.toString().toLowerCase();
        for (int i = 1; i < 10; ++i) {
            if (Rank.values()[i] == rank) {
                name = "" + (i + 1);
                break;
            }
        }
        name += "_of_" + suit.toString().toLowerCase() + ".png";

        return name;
    }

    public int compareTo(Card other) {
        return Integer.compare(rank.getValue(), other.rank.getValue());
    }

    public static Comparator<Card> compareByRank = new Comparator<Card>() {
        @Override
        public int compare(Card c1, Card c2) {
            return Integer.compare(c1.rank.getValue(), c2.rank.getValue());
        }
    };
}
