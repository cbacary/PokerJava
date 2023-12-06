import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class GUITablePanel extends JComponent {

    private final int PLAYER_W = 100;
    private final int PLAYER_H = 100;

    private int currentPlayer;
    private int dealer;

    private ArrayList<Player> players;

    public GUITablePanel(ArrayList<Player> players) {
        this.players = new ArrayList<Player>(players);
    }

    public void updatePlayerGUI(int currentPlayer, int dealer) {
        this.currentPlayer = currentPlayer;
        this.dealer = dealer;

        repaint();
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

        int width = getWidth() - (getWidth() / 8);
        int height = getHeight() - (2 * getHeight() / 8);
        int x = ((getWidth() / 2) - (width / 2));
        int y = ((getHeight() / 2) - (height / 2));

        g2d.fillOval(x, y, width, height);

        double angleStep = 360 / (double)players.size();
        int a = width / 2;
        int b = height / 2;

        double angle = angleStep;
        for (int i = 0; i < players.size(); i++) {

            double xCircle =
                a * Math.cos(Math.toRadians(angle)) + getWidth() / 2;
            double yCircle =
                b * Math.sin(Math.toRadians(angle)) + getHeight() / 2;

            xCircle -= PLAYER_W / 2;
            yCircle -= PLAYER_H / 2;

            x = (int)xCircle;
            y = (int)yCircle;

            g2d.setColor(Color.BLUE);
            if (i == currentPlayer) {
                g2d.setColor(Color.GREEN);
            }

            // Gray out the oval if folded
            if (players.get(i).getLastAction().equals("Fold")) {
                g2d.setColor(Color.GRAY);
            }

            g2d.fillOval(x, y, PLAYER_W, PLAYER_H);

            int sb = (dealer + 1) % players.size();
            int bb = (dealer + 2) % players.size();

            String tag = players.get(i).getName();
            if (i == dealer)
                tag += " -- D";
            else if (i == sb)
                tag += " -- SB";
            else if (i == bb)
                tag += " -- BB";

            g2d.setColor(Color.WHITE);

            FontMetrics f = g2d.getFontMetrics();

            int centerX, centerY;

            centerX = centerStringX(tag, f, x, PLAYER_W);
            g2d.drawString(tag, centerX, y - 20);

            String lastAction = players.get(i).getLastAction();
            centerX = centerStringX(lastAction, f, x, PLAYER_W);
            g2d.drawString(players.get(i).getLastAction(), centerX, y - 5);

            String playerMoney =
                String.format("$%d", players.get(i).getMoney());

            centerX = centerStringX(playerMoney, f, x, PLAYER_W);

            g2d.drawString(playerMoney, centerX, y + PLAYER_H / 2);

            angle += angleStep;
        }
    }

    private int centerStringX(String text, FontMetrics f, int x, int width) {
        int stringWidth = f.stringWidth(text);

        return x + (width / 2) - (stringWidth / 2);
    }

}
