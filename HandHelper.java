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
        } else if ()

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

    public static boolean isStraight(ArrayList<Card> cards) {

        int cardRanks = listRanksToInt(cards);

        // The ranks that represent a straight
        int straightRanks = ROYAL_RANKS;
        for (int i = 0; i <= 7; ++i) {

            // AND the given cardRanks and straightRanks, if it is equals the
            // straightRank then it is a striaght
            if ((straightRanks & cardRanks) == straightRanks) {
                return true;
            }

            // Shift the card ranks down by 1, do this until we covered all
            // ranks
            straightRanks = straightRanks >> 1;
        }

        // If it has not already returned true, then their is no straight
        return false;
    }

    public static boolean isStraightFlush(ArrayList<Card> cards) {

        int straight = 0;
        int i = 0;
        while (i < cards.size()) {
            Rank currentRank = cards.get(i).getRank();

            int j = i + 1;
        }

    }

    public static boolean isFlush(ArrayList<Card> cards) {

        // First sort the cards by suit so all suits are aligned
        Collections.sort(cards, Card.compareBySuit);

        // Now iterate through the sorted cards and count many suits in row
        int sameSuits = 1;
        Suit lastSuit = cards.get(0).getSuit();
        for (int i = 1; i < cards.size(); ++i) {
            if (cards.get(i).getSuit() == lastSuit) {
                sameSuits += 1;
                if (sameSuits >= 5)
                    return true;
            } else {
                lastSuit = cards.get(i).getSuit();
                sameSuits = 1;
            }
        }

        return sameSuits >= 5;
    }

    public static ArrayList<ArrayList<Card>> cardsBySuit(ArrayList<Card> cards) {
        
        ArrayList<ArrayList<Card>> cardsBySuit = new ArrayList<ArrayList<Card>>();

        int i = 0;
        while (i < cards.size()) {
            Suit currentSuit = cards.get(i).getSuit();
            ArrayList<Card> cardsForSuit = new ArrayList<Card>();
            cardsForSuit.add(cards.get(i));
            int j = i + 1;

            while (j < cards.size()) {

                if (cards.get(j).getSuit() != currentSuit) {
                    break;
                }

                cardsForSuit.add(cards.get(j));
                j += 1;
            }

            cardsBySuit.add(cardsForSuit);
            i = j;
        }

        return cardsBySuit;

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
}
