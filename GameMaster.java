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

        cardsInPlay = 0;
        pot = 0;
        currentRaise = 0;
        gameStage = 0;
    }

    public void playNextPhase() {
        int currentPhase = gameStage % NUM_STAGES;

        if (currentPhase == 0) {
            postBlinds();
            dealCards();
            getBets();
        }

        currentPhase += 1;
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
            do {
                players.get(player).addCard(deck.getTopCard(cardsInPlay++));
                player += 1;
            } while (player != (dealer + 3) % players.size());
        }
    }

    /** 
     * Iterates through players in correct order getting each of their bets / 
     * decissions
     * */
    private void getBets() {
        int playerIndex = (dealer + 3) % players.size();
        // endPlayer is how we control where we stop asking for bets, it will 
        // be changed by playerRaise if that player is performing a re-raise
        endPlayer = (dealer + 3) % players.size();
        do {

            if (!playersInGame.get(playerIndex)) {
                playerIndex += 1;
                continue;
            }

            Player player = players.get(playerIndex);
            printMenu(player, playerIndex);

            // if the user made an acceptable decision, increment playerIndex
            if (userAction(player, playerIndex)) {
                playerIndex += 1;
            }
            // Otherwise don't increment and just rerun loop
        } while (playerIndex != endPlayer);
    }

    /**
     * Checks if a player raised off a raise. Important because then we have to
     * reask what previous players want to do. Should only be called after all
     * players have made their move
     * @return returns true if a player raised off a raise
     * */
    private boolean playerReRaised() {
        for (int i = 0; i < players.size(); ++i) {
            if (!playersInGame.get(i))
                continue;
            Player player = players.get(i);
            if (player.getMoneyEntered() != currentRaise &&
                player.getMoney() != 0) {
                return true;
            }
        }
        return false;
    }

    private void printMenu(Player player, int playerIndex) {
        System.out.printf("board: %s\tpot: %d\traise: %d", boardToString(), pot,
                          currentRaise);
        System.out.printf("Player %d cards: %s", playerIndex,
                          player.toString());
        System.out.printf("Player %d ('r'aise, 'c'all, 'f'old): ", playerIndex);
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
        System.out.printf("raise by : %d + ", currentRaise);
        int amount = scanner.nextInt();
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
