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

    HandResult() {
        hand = Hand.HIGH_CARD;
        handCards = new ArrayList<Card>();
        handCards.add(new Card(Rank.ONE, Suit.CLUBS));
    }

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

        // Since both hands are identical, their list size is also the same
        for (int i = handCards.size() - 1; i >= 0; --i) {
            int comparison = handCards.get(i).compareTo(other.handCards.get(i));
            if (comparison != 0) {
                return comparison;
            }
        }

        // both hands are identical
        return 0;
    }
}
