package byog.Core;

import byog.TileEngine.TETile;

public class MapVisualTest {
    public static void main(String[] args) {
        Game game = new Game();
        game.ter.initialize(Game.WIDTH, Game.HEIGHT);
        MapGenerator.WIDTH = Game.WIDTH;
        MapGenerator.HEIGHT = Game.HEIGHT;
        TETile[][] world = game.playWithInputString("N886S");
        game.ter.renderFrame(world);
    }
}
