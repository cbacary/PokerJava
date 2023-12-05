public class Game {
    public static void main(String[] args) {
        GameMaster gm = new GameMaster(6, 1000);

        while (true) {
            gm.playNextStage();
        }
    }
}
