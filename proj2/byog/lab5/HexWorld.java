package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import javax.swing.text.Position;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    /**
     * Create a Position class
     */
    private static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Get the maximum width of the hexagon.
     */
    private static int getWidthOfShape(int size) {
        int widthOfShape = size;
        for (int i = 0; i < size - 1; i += 1) {
            widthOfShape += 2;
        }
        return widthOfShape;
    }

    /**
     * Get the maximum height of the hexagon
     */
    private static int getHeightOfShape(int size) {
        return 2 * size;
    }

    /**
     * Draw the left part of the hexagon
     */
    private static void fillLeftPart(TETile[][] world, Position p, int s, TETile t) {
        int start = s - 1 + p.y;
        int len = 2;
        for (int i = p.x; i < s - 1 + p.x; i += 1) {
            for(int j = start; j < start + len; j += 1) {
                world[i][j] = t;
            }
            start -= 1;
            len += 2;
        }
    }

    /**
     * Draw the middle part of the hexagon
     */
    private static void fillMidPart(TETile[][] world, Position p, int s, int heightOfShape, TETile t) {
        int start = p.y;
        int len = heightOfShape;
        for (int i = s - 1 + p.x; i < 2 * s - 1 + p.x; i += 1) {
            for (int j = start; j < start + len; j += 1) {
                world[i][j] = t;
            }
        }
    }

    /**
     * Draw the right port of the hexagon
     */
    private static void fillRightPart(TETile[][] world, Position p, int s, int heightOfShape, int widthOfShape, TETile t) {
        int start = 1 + p.y;
        int len = heightOfShape - 2;
        for (int i = 2 * s - 1 + p.x; i < widthOfShape + p.x; i += 1) {
            for (int j = start; j < start + len; j += 1) {
                world[i][j] = t;
            }
            start += 1;
            len -= 2;
        }
    }

    /**
     * draw and add an hexagon to the window screen.
     */
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        int widthOfShape = getWidthOfShape(s);
        int heightOfShape = getHeightOfShape(s);

        fillLeftPart(world, p, s, t);
        fillMidPart(world, p, s, heightOfShape, t);
        fillRightPart(world, p, s, heightOfShape, widthOfShape, t);
    }

    /**
     * draw 19 hexagons as shown on the web page.
     */
    public static void addMulHexagons(TETile[][] world, Position p, int s) {
        int yPosInterval = s;
        int xPosInterval = 2 * s - 1;

        int start = 2;
        int len = 4;
        // for loop to draw every hexagon.
        for (int i = 0; i < 3; i += 1) {
            for (int j = start; j < start + len + 1; j += 2) {
                // 1. get the position of the hexagon.
                Position eachP = new Position(i * xPosInterval + p.x, j * yPosInterval + p.y);
                // 2. call random to decide a tile pattern for this hexagon.
                TETile t = randomTile();
                // 3. add the hexagon.
                addHexagon(world, eachP, s, t);
            }
            start -= 1;
            len += 2;
        }

        start = 1;
        len = 6;
        for (int i = 3; i < 5; i += 1) {
            for (int j = start; j < start + len + 1; j += 2) {
                Position eachP = new Position(i * xPosInterval + p.x, j * yPosInterval + p.y);
                TETile t = randomTile();
                addHexagon(world, eachP, s, t);
            }
            start += 1;
            len -= 2;
        }
    }

    /**
     *  generate random pattern of tiles.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(6);
        switch (tileNum) {
            case 0: return Tileset.GRASS;
            case 1: return Tileset.WATER;
            case 2: return Tileset.FLOWER;
            case 3: return Tileset.MOUNTAIN;
            case 4: return Tileset.SAND;
            case 5: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }

    public static void main(String[] args) {
        /* create a tile rendering engine. */
        TERenderer ter = new TERenderer();

        /* initialize a window. */
        ter.initialize(WIDTH, HEIGHT);

        /* fill the window with tile.nothing. */
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        /* draw and add a hexagon to the window screen. */
        Position p = new Position(20, 20);
        addHexagon(world, p, 5, Tileset.FLOWER);
        //addMulHexagons(world, p, 3);

        /* display tiles to the screen. */
        ter.renderFrame(world);
    }
}
