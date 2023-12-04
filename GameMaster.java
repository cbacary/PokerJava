import java.util.ArrayList;
import java.util.Scanner;

public class GameMaster {

    private final int SB_AMOUNT = 20;
    private final int BB_AMOUNT = 50;
    private final static int NUM_STAGES = 4;

    // Scanner object is temporary just for testing, will be replaced with GUI
    private Scanner scanner;
    private ArrayList<Player> players;
    private ArrayList<Boolean> playersInGame;
    private ArrayList<Card> boardCards;
    private Deck deck;

    // Stores the index of which player is the final player to place a bet
    private int endPlayer;

    private int pot;
    private int currentRaise;
    private int cardsInPlay;
    private int gameStage;

    // Dealer is simply represented with int, will be incremented each round
    private int dealer;

    GameMaster(int numPlayers, int startingCash) {
        scanner = new Scanner(System.in);

        players = new ArrayList<Player>();
        playersInGame = new ArrayList<Boolean>();
        for (int i = 0; i < numPlayers; ++i) {
            players.add(new Player(startingCash));
            playersInGame.add(true);
        }

        // Creates a pre-shuffled and initialized deck
        deck = new Deck();
        boardCards = new ArrayList<Card>();

        cardsInPlay = 0;
        pot = 0;
        currentRaise = 0;
        gameStage = 0;
    }

    public void playNextPhase() {
        int currentPhase = gameStage % NUM_STAGES;

        int preFlopStart = (dealer + 3) % players.size();
        int postFlopStart = (dealer + 1) % players.size();

        if (currentPhase == 0) {
            postBlinds();
            dealCards();
            getBets(preFlopStart);
        } else if (currentPhase == 1) {
            placeFlop();
            getBets(postFlopStart);
        } else if (currentPhase == 2) {
            placeTurn();
            getBets(postFlopStart);
        } else if (currentPhase == 3) {
            placeRiver();
            getBets(postFlopStart);
        }

        currentPhase += 1;
    }

    private void placeFlop() {
        for (int i = 0; i < 3; ++i) {
            boardCards.add(deck.getTopCard(cardsInPlay++));
        }
    }

    private void placeTurn() {
        boardCards.add(deck.getTopCard(cardsInPlay++));
    }

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

    /**
     * Iterates through players in correct order getting each of their bets /
     * decissions
     * */
    private void getBets(int startingPlayerIndex) {
        int playerIndex = startingPlayerIndex;
        // endPlayer is how we control where we stop asking for bets, it will
        // be changed by playerRaise if that player is performing a re-raise
        endPlayer = startingPlayerIndex;
        do {

            if (!playersInGame.get(playerIndex)) {
                playerIndex = (playerIndex + 1) % players.size();
                continue;
            }

            Player player = players.get(playerIndex);
            printMenu(player, playerIndex);

            // if the user made an acceptable decision, increment playerIndex
            if (userAction(player, playerIndex)) {
                playerIndex = (playerIndex + 1) % players.size();
            }
            // Otherwise don't increment and just rerun loop
        } while (playerIndex != endPlayer);
    }

    private void printMenu(Player player, int playerIndex) {
        int playerCallAmount = currentRaise - player.getMoneyEntered();

        System.out.printf("\nboard: %s\t\tpot: $%d\t\tcall: $%d\n",
                          boardToString(), pot, playerCallAmount);

        System.out.printf("\nPLAYER %d: %s\t\tcash: $%d\n", playerIndex,
                          player.handString(), player.getMoney());

        System.out.printf("PLAYER %d ('r'aise, 'c'all, 'f'old)\n", playerIndex);
    }

    private boolean userAction(Player player, int playerIndex) {
        String choice = scanner.nextLine();
        switch (choice) {
        case "r":
            if (currentRaise - player.getMoneyEntered() > player.getMoney()) {
                System.out.println(
                    "You do not have enough money to raise. You can either call or fold.");
                return false;
            }
            if (!playerRaise(player, playerIndex)) {
                System.out.println("You cannot raise by this amount.");
                return false;
            }
            break;
        case "c":
            playerCall(player);
            break;
        case "f":
            playerFold(playerIndex);
            break;
        default:
            System.out.println("Invalid choice. Try again.");
            return false;
        }
        return true;
    }

    // WARNING: I think there are some bugs in this, some infinite loops that
    // could happen without prior checks so careful when re-writing for GUI.
    // Specifically, regarding the reRaisedIndex
    private boolean playerRaise(Player player, int playerIndex) {
        System.out.printf("raise by : ", currentRaise);
        int amount = scanner.nextInt();

        if (amount < 0 || player.getMoney() - (currentRaise + amount) < 0) {
            System.out.println("Invalid raise amount. Please try again");
            return false;
        }

        // First match the call
        player.call(currentRaise);
        // Now add raise
        amount = player.raise(amount);

        if (amount > 0) {
            // If this is a re-raise, then the round should end only once this
            // player is reached again, providing other players with their
            // opportunity to respond.
            endPlayer = (currentRaise > 0 ? playerIndex : endPlayer);
            currentRaise += amount;
            pot += amount;
            return true;
        }
        System.out.println("Invalid raise amount. Please try again");
        return false;
    }

    private void playerCall(Player player) {
        int amount = player.call(currentRaise);
        pot += amount;
    }

    private void playerFold(int playerIndex) {
        playersInGame.set(playerIndex, false);
    }

    private String boardToString() {
        String boardString = "";
        for (Card card : boardCards) {
            boardString += card.toString() + ", ";
        }

        return boardString;
    }
}
