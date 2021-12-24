package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import java.io.*;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
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
        TETile[][] finalWorldFrame = null;
        if (input.length() == 0) {
            return null;
        }
        input = toLower(input);
        char firstChar = input.charAt(0);

        char lastChar = input.charAt(input.length() - 1);
        if (firstChar == 'n') {
            finalWorldFrame = newGame(input);
        } else if (firstChar == 'l') {
            finalWorldFrame = loadGame();
        } else if (lastChar == 'q') {
            System.exit(0);
        } else {
            finalWorldFrame = newGame(input);
        }
        if (input.length() >= 2) {
            char secondTolastChar = input.charAt(input.length() - 2);
            if (secondTolastChar == ':' && lastChar == 'q') {
                saveGame(finalWorldFrame);
                System.exit(0);
            }
        }
        return finalWorldFrame;
    }

    private String toLower(String input) {
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

    private TETile[][] newGame(String input) {
        long seed = getSeed(input);
        TETile[][] finalWorldFrame = MapGenerator.addWorld(WIDTH, HEIGHT, seed);
        return finalWorldFrame;
    }

    private TETile[][] loadGame() {
        TETile[][] finalWorldFrame = getSavedGame();
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

}
