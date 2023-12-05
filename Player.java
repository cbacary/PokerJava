import java.util.ArrayList;
import java.util.Comparator;

public class Player {

    private ArrayList<Card> cards;
    private HandResult hand;
    
    private int money;
    private int moneyEnteredThisRound;
    
    Player(int startingCash) {
        cards = new ArrayList<Card>();
        money = startingCash;
        moneyEnteredThisRound = 0;
    }

    public ArrayList<Card> getCards() { return cards; }

    public void addCard(Card card) { cards.add(card); }

    public void clearCards() { cards.clear(); }
    
    public void addMoney(int amount) {
        money += amount;
    }

    public int getMoney() {
        return money;
    }
    
    public int getMoneyEntered() {
        return moneyEnteredThisRound;
    }

    public void resetMoneyEntered() {
        moneyEnteredThisRound = 0;
    }

    public void resetPlayer() {
        hand = new HandResult();
        moneyEnteredThisRound = 0;
        cards = new ArrayList<Card>();
    }

    /**
     * Decreased player money by `amount`. If amount > money then player goes
     * all in and `money` is set to 0
     * @param amount of money to raise
     * @return returns the actual amount the pot will be raised by
     * */
    public int raise(int amount) {

        int amountRaised = (money - amount < 0 ? money : amount);
        money -= amountRaised;
        moneyEnteredThisRound += amountRaised;
        return amountRaised;
    }

    public int call(int raiseToCall) {
        int amountCalled = (money - raiseToCall < 0 ? money : raiseToCall);
        moneyEnteredThisRound += amountCalled;
        money -= amountCalled;
        return amountCalled;
    }

    public String handString() {
        String handStr = "";
        for (Card card: cards) {
            handStr += card.toString() + ", ";
        }
        return handStr;
    }

    public static Comparator<Player> compareByHand = new Comparator<Player>() {
        @Override
        public int compare(Player p1, Player p2) {
            return p1.hand.compareTo(p2.hand);
        };
    };
}
