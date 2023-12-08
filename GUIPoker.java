import java.awt.*;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.*;

public class GUIPoker extends JFrame {

    private final Color BACKGROUND = new Color(0, 120, 0);
    private final int STARTING_CASH = 1000;
    private final String IMAGE_DIR = "./CardImages/";

    private int playerCount;
    private boolean showPlayerCards;

    private GameMaster gm;

    private JButton foldButton;
    private JButton callButton;
    private GUIRaiseButton raiseButton;

    private GUITablePanel tablePanel;
    private JButton handChecker;

    private JPanel cardPanel;
    private JPanel boardCardPanel;
    private JLayeredPane layeredPane;

    private JPanel playerPanel;
    private JLabel playerMoney;
    private JLabel playerHand;
    private JLabel currentPot;

    public GUIPoker(int numPlayers) {
        playerCount = numPlayers;
        showPlayerCards = false;

        // Create the game and play the first stage, dealing and post blinds
        gm = new GameMaster(playerCount, STARTING_CASH);
        gm.startNextStage();

        setTitle("Texas Hold 'Em Poker");
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Set the background color
        getContentPane().setBackground(BACKGROUND);

        initializeComponents();

        updateInfo();
    }

    private void initializeComponents() {
        layeredPane = getLayeredPane();

        playerPanel = new JPanel(new FlowLayout());

        tablePanel = new GUITablePanel(gm.getAllPlayers());
        tablePanel.updatePlayerGUI(gm.getCurrentPlayerInt(), gm.getDealer());

        foldButton = new JButton("Fold");
        callButton = new JButton("Call");
        handChecker = new JButton("See/Hide Cards");

        // Add action listeners to buttons
        foldButton.addActionListener(e -> handleFoldAction());
        callButton.addActionListener(e -> handleCallAction());
        handChecker.addActionListener(e -> toggleSeeCards());

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
        playerHand.setVisible(false);

        playerPanel.add(playerMoney);
        playerPanel.add(foldButton);
        playerPanel.add(callButton);
        playerPanel.add(raiseButton);
        playerPanel.add(handChecker);

        currentPot = new JLabel("Pot: $");

        currentPot.setForeground(Color.WHITE);
        currentPot.setFont(currentPot.getFont().deriveFont(14f));

        layeredPane.add(currentPot);
        layeredPane.add(playerHand);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setPlayerCardsPosition();
                setBoardCardPosition();
                setPotTextPosition();
            }
        });

        add(tablePanel, BorderLayout.CENTER);
        add(playerPanel, BorderLayout.SOUTH);
    }

    private void updateInfo() {
        Player player = gm.getCurrentPlayer();
        System.out.println(player.getName());

        int money = player.getMoney();
        int callAmount = gm.getCurrentPlayerCallAmount();
        int pot = gm.getPotValue();

        Hand hand = player.getHand().getHand();

        playerMoney.setText(String.format("$%d", money));
        playerHand.setForeground(Color.WHITE);
        playerHand.setText(hand.toString());
        callButton.setText(String.format("Call ($%d)", callAmount));

        currentPot.setText(String.format("Pot: $%d", pot));
        setPotTextPosition();

        raiseButton.setMinRaiseValue(callAmount);

        tablePanel.updatePlayerGUI(gm.getCurrentPlayerInt(), gm.getDealer());

        setupBoardCards();
        setupPlayerCards();

        if (showPlayerCards)
            toggleSeeCards();
    }

    private void setupBoardCards() {
        ArrayList<Card> boardCards = gm.getBoardCards();

        // If there are no board cards just return
        if (boardCardPanel != null) {
            layeredPane.remove(boardCardPanel);
            boardCardPanel.removeAll();
        }

        if (boardCards.size() == 0)
            return;

        boardCardPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        boardCardPanel.setOpaque(false);

        for (Card card : boardCards) {
            String imageFileName = IMAGE_DIR + card.getImageFileName();

            Image img = new ImageIcon(imageFileName).getImage();
            img = img.getScaledInstance(78, 113, Image.SCALE_SMOOTH);

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

        if (playerCards.size() == 0)
            return;

        if (cardPanel != null)
            layeredPane.remove(cardPanel);

        cardPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        cardPanel.setOpaque(false);

        for (Card card : playerCards) {
            String imageFileName = IMAGE_DIR + card.getImageFileName();
            System.out.println(imageFileName);

            Image img = new ImageIcon(imageFileName).getImage();
            img = img.getScaledInstance(75, 113, Image.SCALE_SMOOTH);

            ImageIcon cardIcon = new ImageIcon(img);
            JLabel cardLabel = new JLabel(cardIcon);

            cardPanel.add(cardLabel);
        }

        cardPanel.setVisible(false);

        layeredPane.add(cardPanel);

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
        if (gm.enterNextTurnOrder()) { // Go to next player
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

    private void toggleSeeCards() {

        showPlayerCards = !showPlayerCards;

        // Toggle see cards off
        if (!showPlayerCards) {
            cardPanel.setVisible(false);
            playerHand.setVisible(false);
        } else {
            cardPanel.setVisible(true);
            playerHand.setVisible(true);
        }

        revalidate();
        repaint();
    }

    private void setPlayerCardsPosition() {
        if (cardPanel == null) {
            return;
        }

        int x = getWidth() - cardPanel.getPreferredSize().width - 3;
        int y = getHeight() - cardPanel.getPreferredSize().height -
                (playerPanel.getHeight() + 60);

        cardPanel.setBounds(x, y, cardPanel.getPreferredSize().width,
                            cardPanel.getPreferredSize().height);

        x = (getWidth() - (cardPanel.getPreferredSize().width / 2)) -
            (playerHand.getPreferredSize().width / 2);
        y = getHeight() - playerHand.getPreferredSize().height -
            (playerPanel.getHeight() + 40);

        playerHand.setBounds(x, y, playerHand.getPreferredSize().width,
                             playerHand.getPreferredSize().height);
    }

    private void setBoardCardPosition() {
        if (boardCardPanel == null) {
            return;
        }

        int x = getWidth() / 2 - boardCardPanel.getPreferredSize().width / 2;
        int y = getHeight() / 2 - boardCardPanel.getPreferredSize().height;

        boardCardPanel.setBounds(x, y, boardCardPanel.getPreferredSize().width,
                                 boardCardPanel.getPreferredSize().height);
    }

    private void setPotTextPosition() {
        if (currentPot == null) {
            return;
        }

        int x = getWidth() / 2 - currentPot.getPreferredSize().width / 2;
        int y = ((getHeight()) / 5) - currentPot.getPreferredSize().height;

        currentPot.setBounds(x, y, currentPot.getPreferredSize().width,
                             currentPot.getPreferredSize().height);
    }
}
