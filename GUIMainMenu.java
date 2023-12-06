import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class GUIMainMenu extends JFrame {

    public interface MenuListener {
        void onPlayClicked(int playerCount);
    }

    private MenuListener listener;

    private final Color DARK_GREEN = new Color(0, 120, 0);
    private final int MAX_PLAYERS = 6;


    public GUIMainMenu() {
        super("Poker Game Main Menu");

        setLayout(new FlowLayout());

        // Set background color to dark green
        getContentPane().setBackground(DARK_GREEN);

        JLabel Title = new JLabel("Welcome to Java Texas Hold Em Poker");
        Title.setFont(new Font("Impact", Font.BOLD, 40));
        add(Title, BorderLayout.NORTH);

        JLabel playerAmount = new JLabel("Choose how many players");
        playerAmount.setFont(new Font("Verdana", Font.BOLD, 20));
        add(playerAmount, BorderLayout.SOUTH);

        // Panel for radio buttons
        JPanel radioButtonPanel = new JPanel(new FlowLayout());
        radioButtonPanel.setBackground(Color.BLACK);

        ButtonGroup group = new ButtonGroup();

        ArrayList<JRadioButton> countButtons = new ArrayList<JRadioButton>();

        for (int i = 2; i <= MAX_PLAYERS; ++i) {
            // add radio buttons for player count
            JRadioButton b = new JRadioButton(String.format("%d Players", i));
            // Add the buttons to the corresponding panels for layout
            countButtons.add(b);
            group.add(b);
            radioButtonPanel.add(b);
        }

        // Add the panel with radio buttons to the frame
        add(radioButtonPanel);

        // Play button
        JButton playButton = new JButton("Play");

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                for (int i = 0; i < countButtons.size(); ++i) {
                    if (countButtons.get(i).isSelected()) {
                        listener.onPlayClicked(i + 2);
                        return;
                    }
                }

                JOptionPane.showMessageDialog(
                        GUIMainMenu.this, "Please select the number of players.");
            }
        });

        // Add play button to the frame
        add(playButton);
    }

    public void setMenuListener(MenuListener theListener) {
        listener = theListener;
    }
}
