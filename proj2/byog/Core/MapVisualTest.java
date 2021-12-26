package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class MapVisualTest {
    public static void main(String[] args) {
        Game game = new Game();

        game.ter.initialize(Game.WIDTH, Game.HEIGHT);

        TETile[][] world = game.playWithInputString("47873");
        game.ter.renderFrame(world);

        GameInterface.addHUD(Tileset.LOCKED_DOOR);

    }
}
