import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIRaiseButton extends JPanel {

    public interface RaiseListener {
        void onRaiseClick(int raiseAmount);
    }

    private RaiseListener listener;

    private final int RAISE_STEP = 5;

    private JButton leftButton;
    private JButton topRightButton;
    private JButton bottomRightButton;

    private int minRaiseValue;
    int raiseValue;

    public GUIRaiseButton() {
        setLayout(new GridLayout(1, 2));

        leftButton = new JButton("Raise");
        
        leftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listener.onRaiseClick(raiseValue);
            }
        });

        add(leftButton);

        JPanel rightPanel = new JPanel(new GridLayout(2, 1));
        topRightButton = new JButton("Increase");
        bottomRightButton = new JButton("Decrease");

        topRightButton.addActionListener(e -> increase());
        bottomRightButton.addActionListener(e -> decrease());

        rightPanel.add(topRightButton);
        rightPanel.add(bottomRightButton);

        add(rightPanel);
    }

    public void setMinRaiseValue(int min) {
        minRaiseValue = min;
        raiseValue = min;
        updateRaiseButton();
    }

    public void setRaiseListener(RaiseListener theListener) {
        listener = theListener;
    }

    private void increase() {
        raiseValue += RAISE_STEP;
        updateRaiseButton();
    }

    private void decrease() {
        raiseValue = Math.max(minRaiseValue, raiseValue - RAISE_STEP);
        updateRaiseButton();
    }

    private void updateRaiseButton() {
        leftButton.setText(String.format("$%d", raiseValue));
    }
}
