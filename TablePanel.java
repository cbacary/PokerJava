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

        // An oval is the table
        //int diameter = Math.min(getWidth(), getHeight());
        //int x = (getWidth() - diameter) / 2;
        //int y = (getHeight() - diameter) / 2;
        //g2d.fillOval(x + 50, y + 50, diameter - 100, diameter - 100);

        int width = getWidth() - (getWidth() / 10);
        int height = getHeight() - (getHeight() / 10);
        int x = ((getWidth() / 2) - (width / 2));
        int y = ((getHeight() / 2) - (height / 2));

        g2d.fillOval(x, y, width, height);

        double angle = 360 / (double) playerCount;

        // Calculate positions and draw circles for each player
        for (int i = 0; i < playerCount; i++) {
            
        }
    }
}
