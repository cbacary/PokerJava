import java.util.ArrayList;

public class HandHelper {

    private static final int ROYAL_RANKS =
        Rank.TEN.getValue() | Rank.JACK.getValue() | Rank.QUEEN.getValue() |
        Rank.KING.getValue() | Rank.ACE.getValue();

    public static Hand getHand(ArrayList<Card> board, ArrayList<Card> player) {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.addAll(board);
        cards.addAll(player);


        ArrayList<ArrayList<Card>> cardsByRank = cardsByRank(cards);
        ArrayList<ArrayList<Card>> cardsBySuit = cardsBySuit(cards);


        Hand bestHand = Hand.HIGH_CARD;

        Hand h = getStraights(cardsByRank);
        bestHand = (bestHand.getValue() < h.getValue() ? h : bestHand);

        h = getPairs(cardsByRank);
        bestHand = (bestHand.getValue() < h.getValue() ? h : bestHand);

        h = getFlush(cardsBySuit);
        bestHand = (bestHand.getValue() < h.getValue() ? h : bestHand);

        return bestHand;

    }

    public static Hand getPairs(ArrayList<ArrayList<Card>> cardsByRank) {

        // Loop through cards sorted by rank and get the highest pair count and 
        // the next highest pair count.
        int highestPairCount = 0;
        int nextPairCount = 0;

        // We start at index 1 because the first and last index are both ace
        for (int rank = 1; rank < cardsByRank.size(); ++rank) {
            if (cardsByRank.get(rank).size() >= highestPairCount) {
                nextPairCount = highestPairCount;
                highestPairCount = cardsByRank.get(rank).size();
            }
        }

        if (highestPairCount == 4) {
            return Hand.FOUR_KIND;
        } else if (highestPairCount >= 3 && nextPairCount >= 3) {
            return Hand.FULL_HOUSE;
        } else if (highestPairCount == 3) {
            return Hand.THREE_KIND;
        } else if (highestPairCount == 2 && nextPairCount == 2) {
            return Hand.TWO_PAIR;
        } else if (highestPairCount == 2) {
            return Hand.PAIR;
        }

        return Hand.HIGH_CARD;
    }

    /**
     * Returns the highest straight value present in the cards. A list of cards
     * sorted by rank must be passed in.
     *
     * @param cardsByRank: Must be a list of cards sorted by rank.
     * @return the highest value straight type hand present in the cards.
     *          ROYAL_FLUSH, STRAIGHT_FLUSH, STRAIGHT
     * */
    public static Hand getStraights(ArrayList<ArrayList<Card>> cardsByRank) {

        /* Essentially, we create a list of straights
         * which stores all the straights present in the cards. we count how
         * many ranks in a row are seen (I.E when the size of 5 consecutive
         * indicies is > 0). if a straight is found, we add it to a list of
         * straights. then loop through the list of straights and create a list
         * categorized by suit for each straight. if the size of any of these
         * lists is >= 5 then we either have a straight flush or a royal flush.
         * this may or may not be a terrible way to do it.
         * */
        ArrayList<ArrayList<Card>> straights = new ArrayList<ArrayList<Card>>();
        ArrayList<Card> currentStraight = new ArrayList<Card>();
        int straightCounter = 0;
        for (ArrayList<Card> cards : cardsByRank) {
            if (cards.size() != 0) {
                currentStraight.addAll(cards);
                straightCounter += 1;
            } else {
                // If the size of cards is 0 then current straight is disrupted
                // so if we have a straight add it to list of straights
                if (straightCounter >= 5) {
                    straights.add(currentStraight);
                }

                // reset currentStraight
                straightCounter = 0;
                currentStraight.clear();
            }
        }

        // Have to perform additional check in cases where final card checked
        // is part of straight
        if (straightCounter >= 5) {
            straights.add(currentStraight);
        }

        Hand bestHand = Hand.HIGH_CARD;
        for (ArrayList<Card> straight : straights) {

            // If we are here, there must atleast be a straight... we must
            // perform a check before setting bestHand otherwise we may replace
            // a straightFlush with a straight
            bestHand =
                (bestHand.getValue() < Hand.STRAIGHT.getValue() ? Hand.STRAIGHT
                                                                : bestHand);

            // Now check if straight flush by organizing the straight by suit
            ArrayList<ArrayList<Card>> straightsBySuit = cardsBySuit(straight);
            for (ArrayList<Card> lst : straightsBySuit) {
                if (lst.size() >= 5) {
                    // checks if it is a straight flush or a royal flush
                    bestHand = straightFOrR(lst, bestHand);
                }
            }
        }

        return bestHand;
    }

    /**
     * Checks if a straight flush is just a straight flush or is a royal flush
     *
     * This function should only be called by getStraights() and is only here
     * to make things more concise
     * */
    private static Hand straightFOrR(ArrayList<Card> cards, Hand bestHand) {
        if (listRanksToInt(cards) == ROYAL_RANKS) {
            return Hand.ROYAL_FLUSH;
        }

        return (bestHand.getValue() < Hand.STRAIGHT_FLUSH.getValue()
                    ? Hand.STRAIGHT_FLUSH
                    : bestHand);
    }

    public static Hand getFlush(ArrayList<ArrayList<Card>> cardsBySuit) {

        for (ArrayList<Card> cardsForSuit : cardsBySuit) {
            if (cardsForSuit.size() >= 5) {
                return Hand.FLUSH;
            }
        }

        return Hand.HIGH_CARD;
    }

    public static ArrayList<ArrayList<Card>>
    cardsBySuit(ArrayList<Card> cards) {

        ArrayList<ArrayList<Card>> cardsBySuit =
            new ArrayList<ArrayList<Card>>(Suit.values().length);

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
        cardsByRank = new ArrayList<ArrayList<Card>>(Rank.values().length);

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

    private static int getRankIndex(Rank rank) {
        return (int)(Math.log(rank.getValue()) / Math.log(2));
    }
}
