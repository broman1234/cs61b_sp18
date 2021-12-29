package byog.Core;


import java.util.Random;

public class MapVisualTest {
    static long SEED = 1;
    static Random RANDOM = new Random(SEED);
    public static void main(String[] args) {
        Game game = new Game();

        game.playWithInputString("N643SWAAAAASDWSDDDDD");
        //game.playWithInputString("3");
    }
}
