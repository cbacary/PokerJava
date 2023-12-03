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

    private void getBets() {

        int playerIndex = (dealer + 3) % players.size();
        do {
            if (playersInGame.get(playerIndex) == false) {
                continue;
            }
            Player player = players.get(playerIndex);
            System.out.printf("board: %s\tpot: %d\traise: %d", boardToString(),
                              pot, currentRaise);
            System.out.printf("Player %d cards: %s", playerIndex,
                              player.toString());
            System.out.printf("Player %d ('r'aise, 'c'all, 'f'old): ",
                              playerIndex);
            
        } while (playerIndex != (dealer + 3) % players.size());
    }

    private void userAction(Player player, int playerIndex) {
        String choice = scanner.nextLine();
            switch (choice) {
            case "r":
                if (currentRaise - player.getMoneyEntered() >
                    player.getMoney()) {
                    System.out.println(
                        "You do not have enough money to raise. You can either call or fold.");
                    continue;
                }
                playerRaise(player);
                break;
            case "c":
                playerCall(player);
                break;
            case "f":
                playerFold(playerIndex);
                break;
            default:
                System.out.println("Invalid choice. Try again.");
                continue;
            }
    }

    private void playerRaise(Player player) {
        System.out.printf("raise by : %d + ", currentRaise);
        int amount = scanner.nextInt();

        amount = player.raise(amount);
        currentRaise += amount;
        pot += amount;
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
