package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class GameInterface {

    public static void addHUD(TETile[][] world, int mousePosX, int mousePosY, Game game) {
        String s = " ";
        if (mousePosX >= 0 && mousePosX < MapGenerator.WIDTH && mousePosY >= 0 && mousePosY < MapGenerator.HEIGHT) {
            TETile t = world[mousePosX][mousePosY];
            if (t.equals(Tileset.LOCKED_DOOR)) {
                s = "locked door";
            }
            if (t.equals(Tileset.WALL)) {
                s = "wall";
            }
            if (t.equals(Tileset.FLOOR)) {
                s = "floor";
            }
            if (t.equals(Tileset.NOTHING)) {
                s = "outdoor space";
            }
        }
        int timeLeft = Game.timeStepLimit - game.timeStep;
        StdDraw.setPenColor(Color.white);
        if (game.gameOver) {
            StdDraw.text(Game.WIDTH / 2, Game.HEIGHT - 1, "Game Over! Final level: " + game.round);
        } else {
            StdDraw.textLeft(1, Game.HEIGHT - 1, s);
            StdDraw.text(Game.WIDTH / 2, Game.HEIGHT - 1, "round: " + game.round);
            StdDraw.textRight(Game.WIDTH - 1, Game.HEIGHT - 1, "time left: " + timeLeft);
        }
        StdDraw.show();
    }

}
