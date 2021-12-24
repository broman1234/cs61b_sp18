package byog.Core;

import byog.TileEngine.TETile;

public class MapVisualTest {
    public static void main(String[] args) {
        Game game = new Game();
        game.ter.initialize(Game.WIDTH, Game.HEIGHT);

        TETile[][] world = game.playWithInputString("743");
        game.ter.renderFrame(world);
    }
}
