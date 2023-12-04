import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class HandHelper {

    private static final int ROYAL_RANKS =
        Rank.TEN.getValue() + Rank.JACK.getValue() + Rank.QUEEN.getValue() +
        Rank.KING.getValue() + Rank.ACE.getValue();

    public static Hand getHand(ArrayList<Card> board, ArrayList<Card> player) {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.addAll(board);
        cards.addAll(player);

        ArrayList<Card> sortedByRank = new ArrayList<>(cards);
        ArrayList<Card> sortedBySuit = new ArrayList<>(cards);

        Collections.sort(sortedByRank, Card.compareByRank);
        Collections.sort(sortedBySuit, Card.compareBySuit);

        if (isRoyal(sortedByRank)) {
            return Hand.ROYAL_FLUSH;
        }
    }

    public static Hand getPairs(ArrayList<ArrayList<Card>> cardsByRank) {
        // We start at index 1 because the first and last index are both ace
        boolean threeKind = false;
        boolean pair = false;

        for (int rank = 1; rank < cardsByRank.size(); ++rank) {
            ArrayList<Card> cards = cardsByRank.get(rank);
            if (cards.size() == 4) {
                return Hand.FOUR_KIND;
            } else if ()
        }
    }

    public static boolean isFullHouse(ArrayList<Card> cards) {

        // Sort the cards by rank so cards of same rank are organized
        Collections.sort(cards, Card.compareByRank);

        // The ranks of the cards that make up the three of a kind and pair
        Rank threeRank = null;
        Rank twoRank = null;

        int i = 0;
        while (i < cards.size()) {
            int j = i + 1;
            Rank rankToCheck = cards.get(i).getRank();
            while (j < cards.size()) {
                if (cards.get(j).getRank() != rankToCheck) {
                    break;
                }
                j += 1;
            }

            int numOfRank = j - i;
            if (numOfRank >= 3) {
                if (threeRank != null) {
                    twoRank = rankToCheck;
                    return true;
                }
                threeRank = rankToCheck;
            } else if (numOfRank >= 2) {
                twoRank = rankToCheck;
            }

            i = j;
        }

        return (threeRank != null && twoRank != null && twoRank != threeRank);
    }

    public static boolean isPair(ArrayList<Card> cards, int pairCount) {
        Collections.sort(cards, Card.compareByRank);

        // Now iterate through the sorted cards and count many ranks in a row
        int sameRanks = 1;
        Rank lastRank = cards.get(0).getRank();
        for (int i = 1; i < cards.size(); ++i) {
            if (cards.get(i).getRank() == lastRank) {
                sameRanks += 1;
                if (sameRanks == pairCount)
                    return true;
            } else {
                lastRank = cards.get(i).getRank();
                sameRanks = 1;
            }
        }

        return sameRanks == pairCount;
    }

    public static boolean isRoyal(ArrayList<Card> cards) {
        // We sort by the suit
        Collections.sort(cards, Card.compareBySuit);

        // Iterate through the sorted cards
        int cardsSum = 0;
        Suit lastSuit = cards.get(0).getSuit();
        for (int i = 1; i < cards.size(); ++i) {

            Suit suit = cards.get(i).getSuit();

            // If the suit is the same add it to the sum
            if (suit == lastSuit) {
                cardsSum = cardsSum | cards.get(i).getRank().getValue();
                if ((cardsSum & ROYAL_RANKS) == ROYAL_RANKS) {
                    // If the suit is not the same check if the last suit
                    // cardSum equals ROYAL_RANKS. if it did then we return true
                    return true;
                }
            } else {
                cardsSum = 0;
            }

            lastSuit = suit;
        }

        return (cardsSum & ROYAL_RANKS) == ROYAL_RANKS;
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

    public static boolean isFlush(ArrayList<Card> cards) {

        ArrayList<ArrayList<Card>> cardsBySuit = cardsBySuit(cards);

        for (ArrayList<Card> cardsForSuit: cardsBySuit) {
            if (cardsForSuit.size() >= 5) {
                return true;
            }
        }

        return false;
    }

    public static ArrayList<ArrayList<Card>>
    cardsBySuit(ArrayList<Card> cards) {

        ArrayList<ArrayList<Card>> cardsBySuit =
            new ArrayList<ArrayList<Card>>(Suit.values().length);

        for (Card card: cards) {
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
