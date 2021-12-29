package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class GameInterface {

    public static void addHUD(TETile[][] world, int mousePosX, int mousePosY) {
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

        StdDraw.setPenColor(Color.white);
        StdDraw.textLeft(1, Game.HEIGHT - 1, s);
        StdDraw.show();
    }

}
