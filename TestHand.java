import java.util.ArrayList;

public class TestHand {
    public static void main(String[] args) {
        testRoyal();
        testStraight(false);
        System.out.print("testStraightFlush -- ");
        testStraight(true);
        testPair();
        testFourOfAKind();
        testFullHouse();
        testFourOfAKind();
    }

    private static void testRoyal() {

        final int EXPECTED = 4;

        ArrayList<Card> board = new ArrayList<Card>();
        ArrayList<Card> player = new ArrayList<Card>();

        int royalCount = 0;
        for (Suit suit : Suit.values()) {
            board.add(new Card(Rank.NINE, Suit.DIAMONDS));
            player.add(new Card(Rank.EIGHT, Suit.SPADES));

            board.add(new Card(Rank.TEN, suit));
            board.add(new Card(Rank.ACE, suit));
            board.add(new Card(Rank.KING, suit));
            board.add(new Card(Rank.JACK, suit));
            player.add(new Card(Rank.QUEEN, suit));

            Hand hand = HandHelper.getHand(board, player);
            if (hand == Hand.ROYAL_FLUSH) {
                royalCount += 1;
            }

            board.clear();
            player.clear();
        }

        if (royalCount == EXPECTED) {
            System.out.printf("testRoyal check PASSED\n");
        } else {
            System.out.printf("testRoyal check FAILED. -- GOT %d EXPECTED %d\n",

                              royalCount, EXPECTED);
        }
    }

    private static void testStraight(boolean testFlush) {

        final int EXPECTED = 10;
        final Hand EXPECTED_HAND =
            (testFlush ? Hand.STRAIGHT_FLUSH : Hand.STRAIGHT);

        ArrayList<Card> board = new ArrayList<>();
        ArrayList<Card> player = new ArrayList<>();

        int straightCount = 0;
        for (int i = 0; i < 10; i++) {

            int suit = 0;

            player.add(new Card(Rank.THREE, Suit.HEARTS));

            if (i == 0) {
                player.add(new Card(Rank.ACE, Suit.values()[suit]));
            } else {
                player.add(new Card(Rank.values()[i], Suit.values()[suit]));
            }

            for (int j = 1; j < 5; j++) {
                Rank rank = Rank.values()[i + j];
                board.add(new Card(rank, Suit.values()[suit]));
                if (!testFlush)
                    suit = (suit + 1) % Suit.values().length;
            }

            Hand hand = HandHelper.getHand(board, player);
            if (hand == EXPECTED_HAND) {
                straightCount += 1;
            }

            board.clear();
            player.clear();
        }

        if (straightCount == EXPECTED || (testFlush && straightCount == 9)) {
            System.out.println("testStraight check PASSED");
        } else {
            System.out.printf(
                "testStraight check FAILED. -- got %d expected %d\n",
                straightCount, EXPECTED);
        }
    }

    private static void testPair() {
        ArrayList<Card> board = new ArrayList<>();
        ArrayList<Card> player = new ArrayList<>();

        board.add(new Card(Rank.TWO, Suit.HEARTS));
        board.add(new Card(Rank.FIVE, Suit.SPADES));
        board.add(new Card(Rank.SIX, Suit.CLUBS));
        board.add(new Card(Rank.SEVEN, Suit.DIAMONDS));
        player.add(new Card(Rank.EIGHT, Suit.HEARTS));
        player.add(new Card(Rank.EIGHT, Suit.DIAMONDS));

        checkHand("Pair", Hand.PAIR, board, player);
    }

    private static void testThreeOfAKind() {
        ArrayList<Card> board = new ArrayList<>();
        ArrayList<Card> player = new ArrayList<>();

        board.add(new Card(Rank.NINE, Suit.HEARTS));
        board.add(new Card(Rank.NINE, Suit.SPADES));
        board.add(new Card(Rank.NINE, Suit.CLUBS));
        board.add(new Card(Rank.TEN, Suit.DIAMONDS));
        player.add(new Card(Rank.JACK, Suit.HEARTS));
        player.add(new Card(Rank.QUEEN, Suit.DIAMONDS));

        checkHand("Three Kind", Hand.THREE_KIND, board, player);
    }

    private static void testFullHouse() {
        ArrayList<Card> board = new ArrayList<>();
        ArrayList<Card> player = new ArrayList<>();

        board.add(new Card(Rank.KING, Suit.HEARTS));
        board.add(new Card(Rank.KING, Suit.SPADES));
        board.add(new Card(Rank.ACE, Suit.CLUBS));
        board.add(new Card(Rank.ACE, Suit.DIAMONDS));
        player.add(new Card(Rank.ACE, Suit.HEARTS));
        player.add(new Card(Rank.TWO, Suit.DIAMONDS));

        checkHand("Full House", Hand.FULL_HOUSE, board, player);
    }

    private static void testFourOfAKind() {
        ArrayList<Card> board = new ArrayList<>();
        ArrayList<Card> player = new ArrayList<>();

        board.add(new Card(Rank.THREE, Suit.HEARTS));
        board.add(new Card(Rank.THREE, Suit.SPADES));
        board.add(new Card(Rank.THREE, Suit.CLUBS));
        board.add(new Card(Rank.THREE, Suit.DIAMONDS));
        player.add(new Card(Rank.FOUR, Suit.HEARTS));
        player.add(new Card(Rank.FIVE, Suit.DIAMONDS));

        checkHand("Four Kind", Hand.FOUR_KIND, board, player);
    }

    private static void checkHand(String handType, Hand expected,
                                  ArrayList<Card> board,
                                  ArrayList<Card> player) {
        Hand hand = HandHelper.getHand(board, player);
        if (hand == expected) {
            System.out.println(handType + " test PASSED");
        } else {
            System.out.println(handType + " test FAILED. -- got " + hand +
                               " expected " + expected);
        }
    }

    private static String boardToString(ArrayList<Card> board,
                                        ArrayList<Card> player) {
        String result = "";
        for (Card card : board) {
            result += card.toString() + " | ";
        }

        for (Card card : player) {
            result += card.toString() + " | ";
        }

        return result;
    }

    private static void testCardBySuit() {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new Card(Rank.ACE, Suit.DIAMONDS));
        cards.add(new Card(Rank.TWO, Suit.HEARTS));
        cards.add(new Card(Rank.TEN, Suit.HEARTS));
        cards.add(new Card(Rank.FIVE, Suit.CLUBS));
        cards.add(new Card(Rank.THREE, Suit.SPADES));

        ArrayList<ArrayList<Card>> cardsBySuit = HandHelper.cardsBySuit(cards);
        for (ArrayList<Card> cardsForSuit : cardsBySuit) {
            System.out.println(
                boardToString(cardsForSuit, new ArrayList<Card>()));
        }
    }

    private static void testCardByRank() {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 1; i < Rank.values().length; ++i) {
            Rank rank = Rank.values()[i];
            cards.add(new Card(rank, Suit.DIAMONDS));
        }

        var cardsByRank = HandHelper.cardsByRank(cards);
        for (int i = 0; i < cardsByRank.size(); ++i) {
            System.out.printf(
                "%s: %s\n", Rank.values()[i],
                boardToString(cardsByRank.get(i), new ArrayList<Card>()));
        }
    }
}
