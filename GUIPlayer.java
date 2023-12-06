import java.awt.*;
import javax.swing.*;

/** 
 * What we will use to represent a player graphically. 
 * */
public class GUIPlayer extends JComponent {

    // Contains info like Dealer, SB, BB
    private final String tag;

    private Player player;

    public GUIPlayer(Player p, String _tag) {
        tag = _tag;
    }
}
