import javax.swing.*;
import java.awt.*;
import java.awt.Color;

public class PokerGUI extends JFrame {

    private final Color backgroundColor;
    private int playerCount;
    private JButton foldButton;
    private JButton callButton;
    private JButton raiseButton;

    private JButton handChecker;
    private JPanel tablePanel;
    private JPanel playerPanel;

    public PokerGUI(int playerCount, Color backgroundColor) {
        this.playerCount = playerCount;
        this.backgroundColor = backgroundColor;
        setTitle("Texas Hold 'Em Poker");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initializeComponents();

        // Set the background color
        getContentPane().setBackground(backgroundColor);
        JFrame frame = new JFrame("Poker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());



    }

    private void initializeComponents() {
        tablePanel = new JPanel(new BorderLayout());
        playerPanel = new JPanel(new FlowLayout());
        TablePanel tablePanel = new TablePanel(playerCount);
        add(tablePanel, BorderLayout.CENTER);


        foldButton = new JButton("Fold");
        callButton = new JButton("Call");
        raiseButton = new JButton("Raise");
        handChecker = new JButton("See/Hide Cards");

        // Add action listeners to buttons
        foldButton.addActionListener(e -> handleFoldAction());
        callButton.addActionListener(e -> handleCallAction());
        raiseButton.addActionListener(e -> handleRaiseAction());
        handChecker.addActionListener(e -> handleCheckAction());

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

    private void handleCheckAction() {
        // Implement raise action logic
    }
}
