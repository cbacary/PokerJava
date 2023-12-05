import java.util.Random;

public class Deck {
    private Card[] deck;

    public Deck() {
        deck = new Card[52];

        initializeDeck();
        shuffleDeck();
    }

    public Deck shuffleDeck() {

        Random rand = new Random();

        for (int i = 0; i < deck.length; ++i) {
            int r = rand.nextInt(deck.length);

            // Simply swap the values
            Card tmp = deck[i];
            deck[i] = deck[r];
            deck[r] =  tmp;
        }

        return this;
    }

    private void initializeDeck() {
        int count = 0;
        for (Rank rank: Rank.values()) {
            if (rank == Rank.ONE) continue;
            for (Suit suit: Suit.values()) {
                Card c = new Card(rank, suit);
                deck[count] = c;
                count += 1;
            }
        }
    }

    public Card getTopCard(int cardsInPlay) {
        return deck[deck.length - 1 - cardsInPlay];
    }
}
