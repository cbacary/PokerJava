import java.awt.*;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.*;

public class PokerGUI extends JFrame {

    private final Color BACKGROUND = new Color(0, 120, 0);
    private final int STARTING_CASH = 1000;
    private final String IMAGE_DIR = "./CardImages/";

    private GameMaster gm;

    private int playerCount;
    private JButton foldButton;
    private JButton callButton;
    private JButton raiseButton;

    private TablePanel tablePanel;
    private JButton handChecker;
    private JPanel playerPanel;
    private JPanel cardPanel;
    private JLayeredPane layeredPane;

    public PokerGUI(int playerCount) {
        this.playerCount = playerCount;

        // Create the game and play the first stage, dealing and post blinds
        gm = new GameMaster(playerCount, STARTING_CASH);
        gm.playNextStage();

        setTitle("Texas Hold 'Em Poker");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initializeComponents();

        // Set the background color
        getContentPane().setBackground(BACKGROUND);
    }

    private void initializeComponents() {
        playerPanel = new JPanel(new FlowLayout());
        tablePanel = new TablePanel(playerCount);

        foldButton = new JButton("Fold");
        callButton = new JButton("Call");
        raiseButton = new JButton("Raise");
        handChecker = new JButton("See/Hide Cards");

        // Add action listeners to buttons
        foldButton.addActionListener(e -> handleFoldAction());
        callButton.addActionListener(e -> handleCallAction());
        raiseButton.addActionListener(e -> handleRaiseAction());
        handChecker.addActionListener(e -> handleSeeCards());

        playerPanel.add(foldButton);
        playerPanel.add(callButton);
        playerPanel.add(raiseButton);
        playerPanel.add(handChecker);

        add(tablePanel, BorderLayout.CENTER);
        add(playerPanel, BorderLayout.SOUTH);
    }

    private void handleFoldAction() {
        // Implement fold action logic
    }

    private void handleCallAction() {
        // Implement call action logic
    }

    private void handleRaiseAction() {
        // Implement raise action logic
    }

    private void handleSeeCards() {

        // Toggle see cards off
        if (this.cardPanel != null) {
            layeredPane.remove(this.cardPanel);
            this.cardPanel = null;
            revalidate();
            repaint();
            return;
        }

        JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        cardPanel.setOpaque(false);

        ArrayList<Card> playerCards = gm.getCurrentPlayersCards();

        for (Card card : playerCards) {
            String imageFileName = IMAGE_DIR + card.getImageFileName();
            Image img = new ImageIcon(imageFileName).getImage();
            Image resized = img.getScaledInstance(75, 200, Image.SCALE_SMOOTH);
            ImageIcon cardIcon = new ImageIcon(resized);
            JLabel cardLabel = new JLabel(cardIcon);
            System.out.println(card.toString() + imageFileName);
            cardPanel.add(cardLabel);
        }

        // Positioning the cardsPanel in the bottom right corner
        cardPanel.setBounds(
            getWidth() - cardPanel.getPreferredSize().width - 10,
            getHeight() - cardPanel.getPreferredSize().height -
                playerPanel.getHeight() - 10,
            cardPanel.getPreferredSize().width,
            cardPanel.getPreferredSize().height);

        layeredPane = getLayeredPane();
        layeredPane.add(cardPanel, JLayeredPane.POPUP_LAYER);

        this.cardPanel = cardPanel;

        revalidate();
        repaint();
    }
}
