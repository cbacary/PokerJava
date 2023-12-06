import java.util.ArrayList;
import java.util.Comparator;

public class Player {

    private ArrayList<Card> cards;
    private HandResult hand;

    private String name;

    private int money;
    private int moneyEnteredThisRound;
    // player can only win as much as they have entered in a pot
    private int moneyEnteredThisPot;

    Player(String playerName, int startingCash) {
        cards = new ArrayList<Card>();

        // just set hand to null that way if we check it we'll get null except
        hand = null;

        money = startingCash;
        moneyEnteredThisRound = 0;

        name = playerName;
    }

    public ArrayList<Card> getCards() { return cards; }

    public void addCard(Card card) { cards.add(card); }

    public void setHand(HandResult h) { hand = h; }

    public HandResult getHand() { return hand; }

    public String getName() { return name; }

    public void addMoney(int amount) { money += amount; }

    public int getMoney() { return money; }

    public int getMoneyEntered() { return moneyEnteredThisRound; }

    public int getMoneyEnteredThisPot() {
        return moneyEnteredThisPot;
    }

    public void resetMoneyEntered() { moneyEnteredThisRound = 0; }

    public void resetPlayer() {
        hand = null;
        moneyEnteredThisRound = 0;
        moneyEnteredThisPot = 0;
        cards.clear();
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
        moneyEnteredThisPot += amountRaised;

        return amountRaised;
    }

    public int call(int raiseToCall) {
        int amountCalled = (money - raiseToCall - moneyEnteredThisRound < 0 ? money : raiseToCall);

        money -= amountCalled;

        moneyEnteredThisRound += amountCalled;
        moneyEnteredThisPot += amountCalled;

        return amountCalled;
    }

    public String handString() {
        String handStr = "";
        for (Card card : cards) {
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
