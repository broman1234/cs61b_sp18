package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    public Position p;

    public static Position setPlayerPos(TETile[][] world, long seed) {
        ArrayList<Position> playerPos = new ArrayList<> ();
        for (int i = 0; i < MapGenerator.WIDTH; i += 1) {
            for (int j = 0; j < MapGenerator.HEIGHT; j += 1) {
                if (world[i][j].equals(Tileset.FLOOR)) {
                    playerPos.add(new Position(i, j));
                }
            }
        }
        Random RANDOM = new Random(seed);
        int k = RANDOM.nextInt(playerPos.size());
        return playerPos.get(k);
    }

    public static Position getPlayerPos(TETile[][] world) {
        Position pos = null;
        for (int i = 0; i < MapGenerator.WIDTH; i += 1) {
            for (int j = 0; j < MapGenerator.HEIGHT; j += 1) {
                if (world[i][j].equals(Tileset.PLAYER)) {
                    pos = new Position(i, j);
                }
            }
        }
        return pos;
    }

    public static void addPlayer(TETile[][] world, Player player) {
        world[player.p.x][player.p.y] = Tileset.PLAYER;
    }

    public void moveRight() {
        p.x += 1;
    }

    public void moveLeft() {
        p.x -= 1;
    }

    public void moveUp() {
        p.y += 1;
    }

    public void moveDown() {
        p.y -= 1;
    }

    public void move(char key, TETile[][] world, Game game) {
        String strKey = game.toLower(String.valueOf(key));
        if (strKey.equals("d")) {
            if (world[p.x + 1][p.y].equals(Tileset.WALL)) {
                return;
            }
            moveRight();
        }
        if (strKey.equals("a")) {
            if (world[p.x - 1][p.y].equals(Tileset.WALL)) {
                return;
            }
            moveLeft();
        }
        if (strKey.equals("w")) {

            if (world[p.x][p.y + 1].equals(Tileset.WALL)) {
                return;
            }
            moveUp();
        }
        if (strKey.equals("s")) {

            if (world[p.x][p.y - 1].equals(Tileset.WALL)) {
                return;
            }
            moveDown();
        }
    }
}
