import javax.swing.*;
import java.awt.*;

public class TablePanel extends JPanel {

    private int playerCount;

    public TablePanel(int playerCount) {
        this.playerCount = playerCount;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Color floor = new Color(0, 120, 0);
        g2d.setColor(floor);
        g2d.fillRect(0,0, 2000,2000);
        // Set color to brown
        Color brownTable = new Color(140, 70, 20);
        g2d.setColor(brownTable);

        // Draw a circle
        int diameter = Math.min(getWidth(), getHeight());
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;
        g2d.fillOval(x + 50, y + 50, diameter - 100, diameter - 100);

        // Calculate positions and draw circles for each player
        for (int i = 0; i < playerCount; i++) {
            if (i == 0) {
                g2d.setColor(Color.BLUE);
                g2d.fillOval(x + 250, y + 20, 30, 30);
            }
            if (i == 1) {
                g2d.setColor(Color.RED);
                g2d.fillOval(x + 250, y + 475, 30, 30);
            }
            if (i == 2) {
                g2d.setColor(Color.YELLOW);
                g2d.fillOval(x + 120, y + 127, 30, 30);
            }
        }
    }
}
