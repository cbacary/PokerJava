import java.awt.*;
import javax.swing.*;

/**
 * What we will use to represent a player graphically.
 * */
public class GUIPlayer extends JComponent {

    // Contains info like Dealer, SB, BB
    private final String tag;

    private Color color;
    private Player player;
    private int x, y, w, h;

    public GUIPlayer(Player p, String _tag) {
        this.tag = _tag;
        player = p;
        color = Color.BLUE;
    }

    public void setCircle(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void correctBounds(Rectangle bounds) {
        setBounds(bounds);
        System.out.println(
            String.format("%d %d", getBounds().width, getBounds().height));
    }

    public void setFold() { color = Color.GRAY; }

    public void setColor(Color c) {
        color = c;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        g2d.setColor(color);
        g2d.fillOval(x, y, w, h);

        g2d.setColor(Color.WHITE);
        g2d.drawString(tag, x, y - 15);
        //System.out.println(String.format("%d %d %d %d", x, y, w, h));

        g2d.drawString(player.getLastAction(), x + w / 4, y + h / 2);

        color = Color.BLUE;
    }
}
