import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class GUITablePanel extends JPanel {

    private final int PLAYER_W = 100;
    private final int PLAYER_H = 100;

    private ArrayList<GUIPlayer> guiPlayers;

    private int currentPlayer;

    public GUITablePanel(ArrayList<Player> players, int dealer) {
        int sb = (dealer + 1) % players.size();
        int bb = (dealer + 2) % players.size();
        guiPlayers = new ArrayList<GUIPlayer>();
        for (int i = 0; i < players.size(); ++i) {
            String tag = players.get(i).getName();
            if (i == dealer)
                tag += " -- D";
            else if (i == sb)
                tag += " -- SB";
            else if (i == bb)
                tag += " -- BB";
            GUIPlayer p = new GUIPlayer(players.get(i), tag);
            p.setVisible(true);
            add(p);
            guiPlayers.add(p);
        }
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        Color floor = new Color(0, 120, 0);
        g2d.setColor(floor);
        g2d.fillRect(0, 0, 2000, 2000);

        // Set color to brown
        Color brownTable = new Color(140, 70, 20);
        g2d.setColor(brownTable);

        int width = getWidth() - (getWidth() / 10);
        int height = getHeight() - (2 * getHeight() / 10);
        int x = ((getWidth() / 2) - (width / 2));
        int y = ((getHeight() / 2) - (height / 2));

        g2d.fillOval(x, y, width, height);

        double angleStep = 360 / (double)guiPlayers.size();
        int a = width / 2;
        int b = height / 2;

        // Calculate positions and draw circles for each player
        double angle = angleStep;
        for (int i = 0; i < guiPlayers.size(); i++) {
            double xCircle = a * Math.cos(Math.toRadians(angle)) + getWidth() / 2;
            double yCircle = b * Math.sin(Math.toRadians(angle)) + getHeight() / 2;
            
            xCircle -= PLAYER_W / 2;
            yCircle -= PLAYER_H / 2;

            if (i == currentPlayer) {
                guiPlayers.get(i).setColor(Color.GREEN);
            }

            guiPlayers.get(i).setCircle((int)xCircle, (int)yCircle, PLAYER_W,
                                        PLAYER_H);

            guiPlayers.get(i).correctBounds(getBounds());

            angle += angleStep;
        }
    }
}
