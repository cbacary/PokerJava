import java.awt.*;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.*;

public class PokerGUI extends JFrame {

    private final Color BACKGROUND = new Color(0, 120, 0);
    private final int STARTING_CASH = 1000;
    private final String IMAGE_DIR = "./CardImages/";

    private int playerCount;
    private boolean showPlayerCards;

    private GameMaster gm;

    private JButton foldButton;
    private JButton callButton;
    private GUIRaiseButton raiseButton;

    private TablePanel tablePanel;
    private JButton handChecker;

    private JPanel cardPanel;
    private JPanel boardCardPanel;
    private JLayeredPane layeredPane;

    private JPanel playerPanel;
    private JLabel playerMoney;
    private JLabel playerHand;
    private JLabel currentPot;

    public PokerGUI(int numPlayers) {
        playerCount = numPlayers;
        showPlayerCards = false;

        // Create the game and play the first stage, dealing and post blinds
        gm = new GameMaster(playerCount, STARTING_CASH);
        gm.playNextStage();

        setTitle("Texas Hold 'Em Poker");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Set the background color
        getContentPane().setBackground(BACKGROUND);

        initializeComponents();

        updateInfo();
    }

    private void initializeComponents() {
        layeredPane = getLayeredPane();

        playerPanel = new JPanel(new FlowLayout());
        tablePanel = new TablePanel(playerCount);

        foldButton = new JButton("Fold");
        callButton = new JButton("Call");
        // raiseButton = new JButton("Raise");
        handChecker = new JButton("See/Hide Cards");

        // Add action listeners to buttons
        foldButton.addActionListener(e -> handleFoldAction());
        callButton.addActionListener(e -> handleCallAction());
        // raiseButton.addActionListener(e -> handleRaiseAction());
        handChecker.addActionListener(e -> handleSeeCards());

        // Raise button is special
        raiseButton = new GUIRaiseButton();
        raiseButton.setRaiseListener(new GUIRaiseButton.RaiseListener() {
            @Override
            public void onRaiseClick(int raiseAmount) {
                handleRaiseAction(raiseAmount);
            }
        });

        playerMoney = new JLabel("Cash: $");
        playerHand = new JLabel("Hand: ");
        currentPot = new JLabel("");

        playerPanel.add(playerMoney);
        playerPanel.add(foldButton);
        playerPanel.add(callButton);
        playerPanel.add(raiseButton);
        playerPanel.add(handChecker);
        playerPanel.add(playerHand);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setPlayerCardsPosition();
                setBoardCardPosition();
            }
        });

        add(tablePanel, BorderLayout.CENTER);
        add(playerPanel, BorderLayout.SOUTH);
    }

    private void updateInfo() {
        Player player = gm.getCurrentPlayer();

        int money = player.getMoney();
        int callAmount = gm.getCurrentPlayerCallAmount();
        int pot = gm.getPotValue();

        Hand hand = player.getHand().getHand();

        playerMoney.setText(String.format("$%d", money));
        playerHand.setText(hand.toString());
        callButton.setText(String.format("Call ($%d)", callAmount));
        currentPot.setText(String.format("Pot: $%d", pot));

        raiseButton.setMinRaiseValue(callAmount);

        setupBoardCards();
        setupPlayerCards();
    }

    private void setupBoardCards() {
        ArrayList<Card> boardCards = gm.getBoardCards();

        // If there are no board cards just return
        if (boardCards.size() == 0)
            return;
    
        if (boardCardPanel != null) layeredPane.remove(boardCardPanel);

        boardCardPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        boardCardPanel.setOpaque(false);

        for (Card card : boardCards) {
            String imageFileName = IMAGE_DIR + card.getImageFileName();

            Image img = new ImageIcon(imageFileName).getImage();
            img = img.getScaledInstance(75, 200, Image.SCALE_SMOOTH);

            ImageIcon cardIcon = new ImageIcon(img);
            JLabel cardLabel = new JLabel(cardIcon);

            boardCardPanel.add(cardLabel);
        }

        setBoardCardPosition();

        layeredPane.add(boardCardPanel, JLayeredPane.POPUP_LAYER);

        revalidate();
        repaint();
    }

    private void setupPlayerCards() {

        ArrayList<Card> playerCards = gm.getCurrentPlayersCards();

        if (playerCards.size() == 0) return;

        if (cardPanel != null) layeredPane.remove(cardPanel);

        cardPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        cardPanel.setOpaque(false);

        for (Card card : playerCards) {
            String imageFileName = IMAGE_DIR + card.getImageFileName();

            Image img = new ImageIcon(imageFileName).getImage();
            img = img.getScaledInstance(75, 200, Image.SCALE_SMOOTH);

            ImageIcon cardIcon = new ImageIcon(img);
            JLabel cardLabel = new JLabel(cardIcon);

            cardPanel.add(cardLabel);
        }

        // Positioning the cardsPanel in the bottom right corner
        setPlayerCardsPosition();
    }

    private void handleFoldAction() { 
        gm.currentPlayerFold(); 
        checkNextStage();
    }

    private void handleCallAction() { 
        gm.currentPlayerCall(); 
        checkNextStage();
    }

    private void handleRaiseAction(int raiseAmount) {
        gm.currentPlayerRaise(raiseAmount);
        checkNextStage();
    }

    private void checkNextStage() {
        if (gm.nextPlayerTurn()) { // Go to next player
            updateInfo();
            // Reset cards and stuff
        } else { // Stage is finished
            gm.endCurrentStage();
            ArrayList<Player> winners = gm.checkWin();
            if (winners != null) {
                String winMessage = gm.rewardPlayers(winners);
                JOptionPane.showMessageDialog(this, winMessage);
                gm.reset();
            }
            gm.startNextStage();
            updateInfo();
        }
    }

    private void handleSeeCards() {

        showPlayerCards = !showPlayerCards;

        // Toggle see cards off
        if (!showPlayerCards) {
            layeredPane.remove(this.cardPanel);
        } else {
            layeredPane.add(this.cardPanel);
        }

        revalidate();
        repaint();
    }

    private void setPlayerCardsPosition() {
        if (cardPanel == null) {
            return;
        }

        int x = getWidth() - cardPanel.getPreferredSize().width - 10;
        int y = getHeight() - cardPanel.getPreferredSize().height -
                (playerPanel.getHeight() + 40);

        cardPanel.setBounds(x, y, cardPanel.getPreferredSize().width,
                            cardPanel.getPreferredSize().height);
    }

    private void setBoardCardPosition() {
        if (boardCardPanel == null) {
            return;
        }

        int x = getWidth() / 2 - boardCardPanel.getPreferredSize().width;
        int y = getHeight() / 2 - boardCardPanel.getPreferredSize().height;

        cardPanel.setBounds(x, y, boardCardPanel.getPreferredSize().width,
                            boardCardPanel.getPreferredSize().height);
    }
}
