package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;


import java.io.*;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 33;
    public int round;
    public boolean gameOver;
    public Menu menu = new Menu(35, 45);
    public Player player = new Player();
    public int timeStep = 0;
    public long SEED;
    public Random RANDOM;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        // 1. draw menu.
        menu.addMenu();
        // 2. if n: start a new game.
        TETile[][] finalWorld = menu.chooseOption(this);
        RANDOM = new Random(SEED);
        startGame(finalWorld);
    }

    public void startGame(TETile[][] world) {
        ter.initialize(Game.WIDTH, Game.HEIGHT);
        gameOver = false;
        round = 1;
        while (!gameOver) {
            while(!StdDraw.hasNextKeyTyped()) {
                ter.renderFrame(world);
                GameInterface.addHUD(world, (int) StdDraw.mouseX(), (int) StdDraw.mouseY());
                StdDraw.pause(10);
            }

            char key = StdDraw.nextKeyTyped();
            if (key == ':') {
                while(!StdDraw.hasNextKeyTyped()) {
                    ter.renderFrame(world);
                    GameInterface.addHUD(world, (int) StdDraw.mouseX(), (int) StdDraw.mouseY());
                    StdDraw.pause(10);
                }
                char secondKey = StdDraw.nextKeyTyped();
                String strSecondKey = toLower(String.valueOf(secondKey));
                if (strSecondKey.equals("q")) {
                    saveGame(world);
                    System.exit(0);
                }
            }

            world[player.p.x][player.p.y] = Tileset.FLOOR;
            player.move(key, world, this);

            if (world[player.p.x][player.p.y].equals(Tileset.LOCKED_DOOR) ) {
                world[player.p.x][player.p.y] = Tileset.UNLOCKED_DOOR;
                ter.renderFrame(world);
                GameInterface.addHUD(world, (int) StdDraw.mouseX(), (int) StdDraw.mouseY());
                world = nextRound(round);
                timeStep = 0;
                round += 1;
                StdDraw.pause(1000);
                continue;
            }

            Player.addPlayer(world, player);
            ter.renderFrame(world);
            GameInterface.addHUD(world, (int) StdDraw.mouseX(), (int) StdDraw.mouseY());
            StdDraw.pause(10);
            timeStep += 1;

            if (timeStep >= 500) {
                gameOver = true;
                StdDraw.pause(2000);
                System.exit(0);
            }
        }
    }

    public TETile[][] nextRound(int round) {
        long seed = RANDOM.nextLong() + round;
        SEED = seed;
        return newGame(String.valueOf(seed));
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        ter.initialize(Game.WIDTH, Game.HEIGHT);

        TETile[][] finalWorldFrame = null;
        if (input.length() == 0) {
            return finalWorldFrame;
        }
        String lowerInput = toLower(input);

        char firstChar = lowerInput.charAt(0);
        if (firstChar == 'n') {
            finalWorldFrame = newGame(lowerInput);
        } else if (firstChar == 'l') {
            finalWorldFrame = loadGame();
        } else if (firstChar == 'q') {
            System.exit(0);
        } else {
            finalWorldFrame = newGame(lowerInput);
        }

        finalWorldFrame = play(finalWorldFrame, lowerInput);

        if (lowerInput.length() >= 2) {
            char secondToLastChar = lowerInput.charAt(lowerInput.length() - 2);
            char lastChar = lowerInput.charAt(lowerInput.length() - 1);
            if (secondToLastChar == ':' && lastChar == 'q') {
                saveGame(finalWorldFrame);
            }
        }
        return finalWorldFrame;
    }

    private String getMove(String input) {
        StringBuilder sb = new StringBuilder();
        int indexOfSave = input.indexOf(':');
        if (input.charAt(0) == 'l' && indexOfSave < 0) {
            for (int i = 1; i < input.length(); i += 1) {
                sb.append(input.charAt(i));
            }
            return sb.toString();
        }
        if (input.charAt(0) == 'l' && indexOfSave > 0) {
            for (int i = 1; i < indexOfSave; i += 1) {
                sb.append(input.charAt(i));
            }
            return sb.toString();
        }
        if (input.indexOf('s') < 0) {
            return "";
        }

        int indexOfMove = input.indexOf('s') + 1;
        if (indexOfSave < 0) {
            for (int i = indexOfMove; i < input.length(); i += 1) {
                sb.append(input.charAt(i));
            }
        } else {
            for (int i = indexOfMove; i < indexOfSave; i += 1) {
                sb.append(input.charAt(i));
            }
        }
        return sb.toString();
    }

    String toLower(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i += 1) {
            char ch = input.charAt(i);
            if (Character.isUpperCase(ch)) {
                sb.append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    private long getSeed(String input) {
        StringBuilder sb = new StringBuilder();
        char firstChar = input.charAt(0);
        if (firstChar != 'n' && firstChar != 'l' && firstChar != 'q') {
            sb.append(firstChar);
        }
        for (int i = 1; i < input.length(); i += 1) {
            if (input.charAt(i) == 's') {
                break;
            }
            sb.append(input.charAt(i));
        }
        return Long.parseLong(sb.toString());
    }

    TETile[][] newGame(String input) {
        long seed = getSeed(input);
        TETile[][] newWorld = MapGenerator.addWorld(MapGenerator.WIDTH, MapGenerator.HEIGHT, seed);
        player.p = Player.setPlayerPos(newWorld, seed);
        Player.addPlayer(newWorld, player);
        return newWorld;
    }

    TETile[][] loadGame() {
        TETile[][] finalWorldFrame = getSavedGame();
        player.p = Player.getPlayerPos(finalWorldFrame);
        return finalWorldFrame;
    }

    private void saveGame(TETile[][] finalWorldFrame) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("savefile.txt"));
            out.writeObject(finalWorldFrame);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TETile[][] getSavedGame() {
        TETile[][] finalWorldFrame = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("savefile.txt"));
            finalWorldFrame = (TETile[][]) in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return finalWorldFrame;
    }

    public TETile[][] play(TETile[][] world, String input) {
        String move = getMove(input);

        if (move.length() == 0) {
            ter.renderFrame(world);
            return world;
        }

        ter.renderFrame(world);
        StdDraw.pause(250);

        for (int i = 0; i < move.length(); i += 1) {
            world[player.p.x][player.p.y] = Tileset.FLOOR;
            player.move(move.charAt(i), world, this);
            Player.addPlayer(world, player);
            ter.renderFrame(world);
            //GameInterface.addHUD(world, (int) StdDraw.mouseX(), (int) StdDraw.mouseY());
            StdDraw.pause(250);
        }
        return world;
    }
}
