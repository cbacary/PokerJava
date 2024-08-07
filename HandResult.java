import java.util.ArrayList;
import java.util.Collections;

public class HandResult implements Comparable<HandResult> {
    /**
     * This abstraction mostly exists because I needed a way to store both the
     * hand and the cards making up the hand. this is needed in case of a tie,
     * we have to compare the highest cards in the hand
     * */

    // The cards making up the actual hand... Only set this using setHandCards()
    // just so that we ensure handCards are sorted by rank
    private ArrayList<Card> handCards;

    // The hand
    private Hand hand;

    HandResult(Hand h, ArrayList<Card> cards) {
        hand = h;
        handCards = new ArrayList<Card>(cards);
    }

    HandResult(Hand h, Card card) {
        hand = h;
        handCards = new ArrayList<Card>();
        handCards.add(card);
    }

    public void updateHand(Hand h, ArrayList<Card> cards) {
        hand = h;
        handCards = new ArrayList<Card>(cards);
    }

    public Hand getHand() { return hand; }

    public ArrayList<Card> getHandCards() { return handCards; }

    public int compareTo(HandResult other) {
        if (hand != other.hand) {
            return Integer.compare(hand.getValue(), other.hand.getValue());
        }

        // if both HandResults have the same hand we have to compare the cards
        // First ensure cards are sorted
        Collections.sort(other.handCards, Card.compareByRank);
        Collections.sort(handCards, Card.compareByRank);

        int i = handCards.size() - 1;
        int j = other.handCards.size() - 1;
        while (i >= 0 && j >= 0) {
            int comparison = handCards.get(i).compareTo(other.handCards.get(j));
            if (comparison != 0) {
                return comparison;
            }
            i -= 1;
            j -= 1;
        }

        // both hands are identical
        return 0;
    }
}
