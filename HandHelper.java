import java.util.ArrayList;
import java.util.Collections;

public class HandHelper {

    private static final int ROYAL_RANKS =
        Rank.TEN.getValue() | Rank.JACK.getValue() | Rank.QUEEN.getValue() |
        Rank.KING.getValue() | Rank.ACE.getValue();

    public static HandResult getHand(ArrayList<Card> board, ArrayList<Card> player) {

        ArrayList<Card> cards = new ArrayList<Card>();
        ArrayList<HandResult> hands = new ArrayList<HandResult>();

        cards.addAll(board);
        cards.addAll(player);

        Collections.sort(cards);

        ArrayList<ArrayList<Card>> cardsByRank = cardsByRank(cards);
        ArrayList<ArrayList<Card>> cardsBySuit = cardsBySuit(cards);

        hands.add(getStraights(cardsByRank));

        hands.add(getPairs(cardsByRank));

        hands.add(getFlush(cardsBySuit));

        hands.add(new HandResult(Hand.HIGH_CARD, cards.get(cards.size() - 1)));

        return Collections.max(hands);
    }

    public static HandResult getPairs(ArrayList<ArrayList<Card>> cardsByRank) {

        // Loop through cards sorted by rank and get the highest pair count and
        // the next highest pair count.
        int highestPairCount = 0;
        int highestPairCountIndex = 0;
        int nextPairCount = 0;
        int nextPairCountIndex = 0;

        // We start at index 1 because the first and last index are both ace
        for (int rank = 1; rank < cardsByRank.size(); ++rank) {
            if (cardsByRank.get(rank).size() >= highestPairCount) {
                nextPairCount = highestPairCount;
                nextPairCountIndex = highestPairCountIndex;
                highestPairCount = cardsByRank.get(rank).size();
                highestPairCountIndex = rank;
            }
        }

        ArrayList<Card> handCards = new ArrayList<Card>();

        // Add the cards that make up the hand to a list for creating HandResult
        handCards.addAll(cardsByRank.get(nextPairCountIndex));
        handCards.addAll(cardsByRank.get(highestPairCountIndex));

        Hand hand = Hand.HIGH_CARD;
        if (highestPairCount == 4) {
            hand = Hand.FOUR_KIND;
        } else if (highestPairCount >= 3 && nextPairCount >= 2) {
            hand = Hand.FULL_HOUSE;
        } else if (highestPairCount == 3) {
            hand = Hand.THREE_KIND;
        } else if (highestPairCount == 2 && nextPairCount == 2) {
            hand = Hand.TWO_PAIR;
        } else if (highestPairCount == 2) {
            hand = Hand.PAIR;
        }

        return new HandResult(hand, handCards);
    }

    /**
     * Returns the highest straight value present in the cards.
     *
     * @param cardsByRank: list of cards sorted by rank.
     * @return HandResult... checks for:
     *         ROYAL_FLUSH, STRAIGHT_FLUSH, STRAIGHT
     * */
    public static HandResult getStraights(ArrayList<ArrayList<Card>> cardsByRank) {

        // Create a list of straights present in the deck
        int straightCounter = 0;
        ArrayList<ArrayList<Card>> straights = new ArrayList<ArrayList<Card>>();
        ArrayList<Card> currentStraight = new ArrayList<Card>();
        for (ArrayList<Card> cards : cardsByRank) {

            if (cards.size() != 0) {
                currentStraight.addAll(cards);
                straightCounter += 1;
            } else {
                // If the size of cards is 0 then current straight is disrupted
                // so if we have a straight add it to list of straights
                if (straightCounter >= 5) {
                    straights.add(new ArrayList<Card>(currentStraight));
                }

                // reset currentStraight
                straightCounter = 0;
                currentStraight.clear();
            }
        }

        // Have to perform additional check in cases where final card checked
        // is part of straight
        if (straightCounter >= 5) {
            straights.add(new ArrayList<Card>(currentStraight));
        }

        Hand bestHand = Hand.HIGH_CARD;
        ArrayList<Card> handCards = new ArrayList<Card>();

        // Iterate through straights
        for (ArrayList<Card> straight : straights) {

            // If we are here, bestHand should at a minimum be a straight
            if (bestHand.getValue() < Hand.STRAIGHT.getValue()) {
                bestHand = Hand.STRAIGHT;
                // this should probably just be set as a reference but safer
                handCards = new ArrayList<Card>(straight);
            }

            // Now check if straight flush by organizing the straight by suit
            ArrayList<ArrayList<Card>> straightsBySuit = cardsBySuit(straight);
            for (ArrayList<Card> lst : straightsBySuit) {

                // If the list size is greater than 5 then straight flush
                if (lst.size() >= 5) {

                    // check if royal flush
                    if ((listRanksToInt(lst) & ROYAL_RANKS) == ROYAL_RANKS) {
                        bestHand = Hand.ROYAL_FLUSH;
                        handCards = new ArrayList<Card>(lst);
                        break;
                    }
                    handCards = new ArrayList<Card>(lst);
                    bestHand = Hand.STRAIGHT_FLUSH;
                }
            }
        }

        return new HandResult(bestHand, handCards);
    }

    /** 
     * function checks for flush in passed in cards
     * 
     * @param cardsBySuit: list of cards sorted by suit
     * @return HandResult if flush otherwise returns throwaway HandResult 
     * which is a HIGH_CARD with a ONE of CLUBS which doesnt actually exist
     * */
    public static HandResult getFlush(ArrayList<ArrayList<Card>> cardsBySuit) {

        for (ArrayList<Card> cardsForSuit : cardsBySuit) {
            if (cardsForSuit.size() >= 5) {
                return new HandResult(Hand.FLUSH, cardsForSuit);
            }
        }

        // Return a throwaway because we cant return null and a card of rank 
        // one wont interere with anything
        return new HandResult(Hand.HIGH_CARD, new Card(Rank.ONE, Suit.CLUBS));
    }

    /** 
     * Returns a list of cards organized by suit where each index is a list 
     * corresponding to a suit
     * */
    public static ArrayList<ArrayList<Card>>
    cardsBySuit(ArrayList<Card> cards) {

        ArrayList<ArrayList<Card>> cardsBySuit =
            new ArrayList<ArrayList<Card>>();

        // Initialize list
        for (int i = 0; i < Suit.values().length; ++i) {
            cardsBySuit.add(new ArrayList<Card>());
        }

        for (Card card : cards) {
            Suit s = card.getSuit();
            cardsBySuit.get(s.getValue()).add(card);
        }

        return cardsBySuit;
    }

    /**
     * Takes in a list of cards and returns an list of arraylists where the
     * indexes map to the rank of the cards. Index 0 is ACE and the final index
     * is ACE too because ACE needs to wrap around for straights.
     * */
    public static ArrayList<ArrayList<Card>>
    cardsByRank(ArrayList<Card> cards) {
        ArrayList<ArrayList<Card>> cardsByRank;
        cardsByRank = new ArrayList<ArrayList<Card>>();

        // Initialize list
        for (int i = 0; i < Rank.values().length; ++i) {
            cardsByRank.add(new ArrayList<Card>());
        }

        for (Card card : cards) {
            Rank r = card.getRank();

            // If its an ace it also needs to be at index 0
            if (r == Rank.ACE) {
                cardsByRank.get(0).add(card);
            }

            int index = getRankIndex(r);
            cardsByRank.get(index).add(card);
        }

        return cardsByRank;
    }

    /**
     * Returns an integer that represents the value of the passed in cards,
     * only considers rank.
     * */
    public static int listRanksToInt(ArrayList<Card> cards) {
        int sum = 0;
        for (Card card : cards) {
            sum = sum | card.getRankValue();
        }

        return sum;
    }

    public static int getRankIndex(Rank rank) {
        return (int)(Math.log(rank.getValue()) / Math.log(2));
    }
}
