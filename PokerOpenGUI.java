import javax.swing.*;

public class PokerOpenGUI {

    private static GUIMainMenu mainMenu;
    private static PokerGUI pokerGame;

    public static void main(String[] args) {

        mainMenu = new GUIMainMenu();
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.setSize(1000, 800);
        mainMenu.setVisible(true);

        mainMenu.setMenuListener(new GUIMainMenu.MenuListener() {
            @Override
            public void onPlayClicked(int playerCount) {
                mainMenu.setVisible(false);
                pokerGame = new PokerGUI(playerCount);
                pokerGame.setVisible(true);
            }
        });
    }

}
