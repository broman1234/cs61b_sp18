package byog.Core;

import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import org.junit.Test;

import java.awt.*;

public class Menu {
    private int width;
    private int height;

    public Menu(int w, int h) {
        this.width = w;
        this.height = h;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public static void main(String[] args) {
        Game game = new Game();
        Menu m = new Menu(35, 45);
        m.addMenu();
        m.chooseOption(game);

    }

    public void addMenu() {
        StdDraw.clear();
        StdDraw.clear(Color.black);

        drawTitle();
        drawOptions();

        StdDraw.show();
    }

    public void drawTitle() {
        int midWidth = width / 2;
        int titlePos = height / 6 * 5;
        Font bigFont = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, titlePos, "CS61B: THE GAME");
    }

    public void drawOptions() {
        int midWidth = width / 2;
        int midHeight = height / 2;

        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.text(midWidth, midHeight + 2, "New Game (N)");
        StdDraw.text(midWidth, midHeight, "Load Game (L)");
        StdDraw.text(midWidth, midHeight - 2, "Quit (Q)");
    }

    public void drawEnterSeed() {
        int midWidth = width / 2;
        int midHeight = height / 2;
        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.text(midWidth, midHeight + 1, "Please enter a seed");
    }

    public TETile[][] chooseOption(Game game) {
        while (!StdDraw.hasNextKeyTyped()) {
            continue;
        }
        char key = StdDraw.nextKeyTyped();
        String strKey = game.toLower(String.valueOf(key));
        TETile[][] finalWorld = null;
        if (strKey.equals("n")) {
            String seed = typeSeed(game);
            game.SEED = Long.parseLong(seed);
            finalWorld = game.newGame(seed);
        } else if (strKey.equals("l")) {
            finalWorld = game.loadGame();
        } else if (strKey.equals("q")) {
            System.exit(0);
        }
        return finalWorld;
    }

    public void addSeedPage(String s) {
        int midWidth = width / 2;
        int midHeight = height / 2;
        StdDraw.clear(Color.black);
        drawTitle();
        drawEnterSeed();


        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight - 1, s);
        StdDraw.line(width / 5, midHeight - 2, width * 4 / 5, midHeight - 2);
        StdDraw.show();
    }

    public String typeSeed(Game game) {
        String input = "";
        addSeedPage(input);

        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            String strKey = game.toLower(String.valueOf(key));
            if (strKey.equals("s")) {
                break;
            }
            input += strKey;
            addSeedPage(input);
        }
        return input;
    }
}
