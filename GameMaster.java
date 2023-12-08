import java.util.ArrayList;
import java.util.Collections;

public class GameMaster {

    private final int SB_AMOUNT = 20;
    private final int BB_AMOUNT = 50;
    private final static int NUM_STAGES = 4;

    private ArrayList<Player> players;
    private ArrayList<Boolean> playersInGame;
    private ArrayList<Card> boardCards;
    private Deck deck;

    // Stores the index of which player is the final player to place a bet
    private int endPlayer;
    // index of current players turn
    private int currentPlayer;

    // number of players remaining in this round
    private int playersRemaining;

    private int pot;
    private int currentRaise;
    private int cardsInPlay;
    private int gameStage;

    // Dealer is simply represented with int, will be incremented each round
    private int dealer;

    GameMaster(int numPlayers, int startingCash) {
        players = new ArrayList<Player>();
        playersInGame = new ArrayList<Boolean>();
        for (int i = 0; i < numPlayers; ++i) {
            players.add(
                new Player(String.format("Player %d", (i + 1)), startingCash));
            playersInGame.add(true);
        }

        // Creates a pre-shuffled and initialized deck
        deck = new Deck();
        boardCards = new ArrayList<Card>();

        playersRemaining = players.size();
        cardsInPlay = 0;
        pot = 0;
        currentRaise = 0;
        gameStage = 0;
        dealer = 0;
    }

    public void startNextStage() {
        currentPlayer = (gameStage == 0 ? (dealer + 3) % players.size()
                                        : (dealer + 1) % players.size());

        // If the next active player is in fact the SB then it returns that
        if (!playersInGame.get(currentPlayer))
            currentPlayer = getNextActivePlayer(currentPlayer);

        endPlayer = currentPlayer;

        if (gameStage == 0) {
            postBlinds();
            dealCards();
        } else if (gameStage == 1) {
            placeFlop();
        } else if (gameStage == 2) {
            placeTurn();
        } else if (gameStage == 3) {
            placeRiver();
        }

        updatePlayerHands();
    }

    public void endCurrentStage() {
        currentRaise = 0;
        gameStage += 1;
        for (Player player : players) {
            player.resetMoneyEntered();
        }
    }

    public void reset() {
        playersRemaining = players.size();
        cardsInPlay = 0;
        pot = 0;
        currentRaise = 0;
        gameStage = 0;

        dealer = (dealer + 1) % players.size();

        for (int i = 0; i < players.size(); ++i) {
            playersInGame.set(i, (players.get(i).getMoney() > 0 ? true : false));
            players.get(i).resetPlayer();
        }

        boardCards.clear();

        deck.shuffleDeck();
    }

    public void currentPlayerRaise(int raiseAmount) {
        playerRaise(currentPlayer, raiseAmount);
    }

    public void currentPlayerFold() { playerFold(currentPlayer); }

    public void currentPlayerCall() { playerCall(players.get(currentPlayer)); }

    /**
     * Returns true if the stage is finished.
     * */
    public boolean enterNextTurnOrder() {

        if (playersRemaining <= 1) {
            return false;
        }

        int nextPlayer = currentPlayer;
        while (true) {
            nextPlayer = (nextPlayer + 1) % players.size();
            System.out.println(nextPlayer + " " + endPlayer);
            if (nextPlayer == endPlayer)
                return false;
            if (playersInGame.get(nextPlayer)) {
                currentPlayer = nextPlayer;
                return true;
            }
        }
    }

    public ArrayList<Player> getAllPlayers() { return players; }

    public int getDealer() { return dealer; }

    public Player getCurrentPlayer() { return players.get(currentPlayer); }

    public int getCurrentPlayerInt() { return currentPlayer; }

    public int getCurrentPlayerCallAmount() {
        System.out.printf("%d %d %d\n",
                          players.get(currentPlayer).getMoneyEntered(),
                          currentRaise, endPlayer);
        return currentRaise - players.get(currentPlayer).getMoneyEntered();
    }

    public ArrayList<Card> getCurrentPlayersCards() {
        return players.get(currentPlayer).getCards();
    }

    public ArrayList<Card> getBoardCards() { return boardCards; }

    public int getPotValue() { return pot; }

    private void updatePlayerHands() {

        for (Player p : players) {
            HandResult h = HandHelper.getHand(boardCards, p.getCards());
            p.setHand(h);
        }
    }

    // 1,100 lines of code later... finally get to write this function
    // function returns null if no winner
    public ArrayList<Player> checkWin() {

        // There can be multiple winners
        ArrayList<Player> winners = new ArrayList<Player>();

        if (playersRemaining > 1 && gameStage < NUM_STAGES) {
            return null;
        } else if (playersRemaining == 1) {
            for (int i = 0; i < players.size(); ++i) {
                if (playersInGame.get(i)) {
                    winners.add(players.get(i));
                    return winners;
                }
            }
        }

        // Otherwise its final stage and we actually need to compare hands

        // First make sure the player hands are correct
        updatePlayerHands();

        // Get active players
        ArrayList<Player> sortedByHand = new ArrayList<Player>();
        for (int i = 0; i < players.size(); ++i) {
            if (playersInGame.get(i)) {
                sortedByHand.add(players.get(i));
            }
        }

        // Sort by hand value
        Collections.sort(sortedByHand, Player.compareByHand);

        for (int i = sortedByHand.size() - 1; i >= 1; --i) {

            winners.add(sortedByHand.get(i));
            if (Player.compareByHand.compare(sortedByHand.get(i),
                                             sortedByHand.get(i - 1)) != 0) {
                break;
            }
        }

        return winners;
    }

    public String rewardPlayers(ArrayList<Player> winners) {
        String result = "";
        for (Player p : winners) {
            int winnings = pot / winners.size();
            p.addMoney(winnings);
            result += String.format("%s won $%d\n", p.getName(), winnings);
        }
        return result;
    }

    private void placeFlop() {
        for (int i = 0; i < 3; ++i) {
            boardCards.add(deck.getTopCard(cardsInPlay++));
        }
    }

    private void placeTurn() { boardCards.add(deck.getTopCard(cardsInPlay++)); }

    private void placeRiver() {
        boardCards.add(deck.getTopCard(cardsInPlay++));
    }

    private void postBlinds() {
        int sb = (dealer + 1) % players.size();
        int bb = (dealer + 2) % players.size();

        pot += players.get(sb).raise(SB_AMOUNT);
        pot += players.get(bb).raise(BB_AMOUNT);

        currentRaise = BB_AMOUNT;
    }

    private void dealCards() {
        for (int i = 0; i < 2; ++i) {
            int player = (dealer + 3) % players.size();
            System.out.println(player);
            do {
                players.get(player).addCard(deck.getTopCard(cardsInPlay));
                cardsInPlay += 1;
                player = (player + 1) % players.size();
            } while (player != (dealer + 3) % players.size());
        }
    }

    private boolean playerRaise(int playerIndex, int amount) {
        Player player = players.get(playerIndex);

        if (amount < 0) {
            return false;
        }

        // First match the call
        int amountCalled = player.call(currentRaise);

        int amountToRaise = amount - amountCalled;

        // Now add raise
        int amountRaised = player.raise(amountToRaise);

        endPlayer = (amount > 0 ? playerIndex : endPlayer);

        currentRaise += amountRaised;
        pot += amountCalled + amountRaised;

        return true;
    }

    private void playerCall(Player player) {
        int amount = player.call(currentRaise);
        pot += amount;
    }

    private void playerFold(int playerIndex) {
        players.get(playerIndex).setActionFold();
        playersInGame.set(playerIndex, false);
        playersRemaining--;
    }

    private int getNextActivePlayer(int index) {
        int nextActive = index;
        for (int i = index + 1; (i + 1) % players.size() != index; ++i) {
            if (playersInGame.get(i % players.size())) {
                nextActive = i % players.size();
                break;
            }
        }
        System.out.println(nextActive);
        return nextActive;
    }
}
