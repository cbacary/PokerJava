import java.util.ArrayList;

public class Player {
    private ArrayList<Card> hand;
    
    private int money;
    private int moneyEnteredThisRound;
    
    Player(int startingCash) {
        hand = new ArrayList<Card>();
        money = startingCash;
        moneyEnteredThisRound = 0;
    }

    public ArrayList<Card> getHand() { return hand; }

    public void addCard(Card card) { hand.add(card); }

    public void clearCards() { hand.clear(); }

    public int getMoney() {
        return money;
    }
    
    public int getMoneyEntered() {
        return moneyEnteredThisRound;
    }

    public void resetMoneyEntered() {
        moneyEnteredThisRound = 0;
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
        for (Card card: hand) {
            handStr += card.toString() + ", ";
        }
        return handStr;
    }
}
