import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PokerOpenGUI {

    // Set background color as a variable to use in other GUI
    private static final Color darkGreen = new Color(0, 120, 0);

    public static void main(String[] args) {
        // Frame setup
        JFrame frame = new JFrame("Poker Game Title Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        frame.getContentPane().setBackground(darkGreen);

        JLabel Title = new JLabel("Welcome to Java Texas Hold Em Poker");
        Title.setFont(new Font("Impact", Font.BOLD, 40));
        frame.add(Title, BorderLayout.NORTH);

        JLabel playerAmount = new JLabel("Choose how many players");
        playerAmount.setFont(new Font("Verdana", Font.BOLD, 20));
        frame.add(playerAmount, BorderLayout.SOUTH);

        // Panel for radio buttons
        JPanel radioButtonPanel = new JPanel(new FlowLayout());
        radioButtonPanel.setBackground(Color.BLACK); // Set the background color here

        // Radio buttons for player count
        JRadioButton twoPlayers = new JRadioButton("2 Players");
        JRadioButton threePlayers = new JRadioButton("3 Players");
        JRadioButton fourPlayers = new JRadioButton("4 Players");
        JRadioButton fivePlayers = new JRadioButton("5 Players");
        JRadioButton sixPlayers = new JRadioButton("6 Players");


        // Group the radio buttons
        ButtonGroup group = new ButtonGroup();
        group.add(twoPlayers);
        group.add(threePlayers);
        group.add(fourPlayers);
        group.add(fivePlayers);
        group.add(sixPlayers);

// Add radio buttons to the panel instead of directly to the frame
        radioButtonPanel.add(twoPlayers);
        radioButtonPanel.add(threePlayers);
        radioButtonPanel.add(fourPlayers);
        radioButtonPanel.add(fivePlayers);
        radioButtonPanel.add(sixPlayers);

        // Add the panel with radio buttons to the frame
        frame.add(radioButtonPanel);

        // Play button
        JButton playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // Ensures that a player amount is selected before starting game
                if (!twoPlayers.isSelected() && !threePlayers.isSelected() &&
                        !fourPlayers.isSelected() && !fivePlayers.isSelected() &&
                        !sixPlayers.isSelected()) {
                    JOptionPane.showMessageDialog(frame, "Please select the number of players.");
                    return; // Do not proceed if no selection is made
                }

                // Determine which radio button is selected
                int playerCount = 0;
                if (twoPlayers.isSelected()) playerCount = 2;
                if (threePlayers.isSelected()) playerCount = 3;
                if (fourPlayers.isSelected()) playerCount = 4;
                if (fivePlayers.isSelected()) playerCount = 5;
                if (sixPlayers.isSelected()) playerCount = 6;

                // Call method to play poker with the selected number of players
                playPoker(playerCount);

            }
        });

        // Add play button to the frame
        frame.add(playButton);

        // Final frame setup
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private static void playPoker(int playerCount) {
        // Poker game logic for the specified number of players
        PokerGUI pokerGame = new PokerGUI(playerCount, darkGreen);
        pokerGame.setVisible(true);
    }
}
