package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class MapGenerator {
    static int WIDTH = 80;
    static int HEIGHT = 30;
    static final int GENERATIONLIMIT = 30;
    static long SEED = 1;
    static Random RANDOM = new Random(SEED);

    public static void addSpace(TETile[][] world, Square space, ArrayList<Square> spaceBuilt) {
        for (int i = 0; i < space.width; i += 1) {
            for (int j = 0; j < space.height; j += 1) {
                if (i == 0 || i == space.width - 1 || j == 0 || j == space.height - 1) {
                    world[i + space.p.x][j + space.p.y] = Tileset.WALL;
                } else {
                    world[i + space.p.x][j + space.p.y] = Tileset.FLOOR;
                }
            }
        }
        spaceBuilt.add(space);
    }

    public static void addLockDoor(TETile[][] world, Room room) {
        int doorPos = randomPos(1, room.width - 2);
        world[doorPos + room.p.x][room.p.y] = Tileset.LOCKED_DOOR;
    }

    public static int randomPos(int min, int max) {
        if (min == max) {
            return min;
        }
        return RANDOM.nextInt(max - min + 1) + min;
    }

    /** use random to decide which type of object (room or hallway)
    to connect to the current room(hallway). */
    public static boolean isRoom() {
        int shapeNum = RANDOM.nextInt(2);
        return shapeNum == 1;
    }

    public static ArrayList<Integer> validSides(Square space, int side) {
        ArrayList<Integer> validSides = new ArrayList<> ();
        validSides.add(side);
        for (int i = 1; i < 5; i += 1) {
            if (i != side && space.hasConnectPoint()) {
                validSides.add(i);
            }
        }
        return validSides;
    }

    public static Room roomLoop(Room room, ArrayList<Square> spaceBuilt, int side, Position prevConnectPoint) {
        int i = 0;
        Room newRoom = room;
        while (!canAdd(newRoom, spaceBuilt) && i < GENERATIONLIMIT) {
            newRoom = new Room();
            setupSpace(newRoom, prevConnectPoint, side);
            i += 1;
        }
        return newRoom;
    }

    public static Hallway hallwayLoop(Hallway hallway, ArrayList<Square> spaceBuilt, int side, Position prevConnectPoint) {
        int i = 0;
        Hallway newHallway = hallway;
        while (!canAdd(newHallway, spaceBuilt) && i < GENERATIONLIMIT) {
            newHallway = new Hallway();
            setupSpace(newHallway, prevConnectPoint, side);
            i += 1;
        }
        return newHallway;
    }

    public static void addMulti(TETile[][] world, Position prevConnectPoint, int side, ArrayList<Square> spaceBuilt) {
        if (isRoom()) {
            Room currentRoom = new Room();
            setupSpace(currentRoom, prevConnectPoint, side);
            currentRoom = roomLoop(currentRoom, spaceBuilt, side, prevConnectPoint);
            addMultiSpace(world, currentRoom, prevConnectPoint, spaceBuilt);
        } else {
            Hallway currentHall = new Hallway();
            setupSpace(currentHall, prevConnectPoint, side);
            currentHall = hallwayLoop(currentHall, spaceBuilt, side, prevConnectPoint);
            addMultiSpace(world, currentHall, prevConnectPoint, spaceBuilt);
        }
    }

    public static void setupSpace(Square space, Position prevConnectPoint, int side) {
        space.p = currentSpacePos(space, prevConnectPoint, side);
        space.bottomLeft = space.setBottomLeft(space.p);
        space.topRight = space.setTopRight(space.p, space.width, space.height);
    }

    public static void addMultiSpace(TETile[][] world, Square space, Position prevConnectPoint, ArrayList<Square> spaceBuilt) {
        if (canAdd(space, spaceBuilt)) {
            addSpace(world, space, spaceBuilt);
            connectThroughWalls(world, space, prevConnectPoint);

            ArrayList<Integer> validSides = sideWithConnection(space);

            for (int s : validSides) {
                Position connectPoint = space.connectPoint(space.p, s);
                // recursive method.
                addMulti(world, connectPoint, s, spaceBuilt);
            }
        }
    }

    public static boolean isOverlap(Square space, Position bottomLeft, Position topRight) {
        return !(space.bottomLeft.x > topRight.x || space.bottomLeft.y > topRight.y || space.topRight.x < bottomLeft.x || space.topRight.y < bottomLeft.y);
    }

    public static boolean isOutOfBoundary(Square space) {
        return !(space.topRight.x < WIDTH && space.topRight.y < HEIGHT && space.bottomLeft.x >= 0 && space.bottomLeft.y >= 0);
    }

    public static boolean canAdd(Square space, ArrayList<Square> spaceBuilt) {
        for (Square sp: spaceBuilt) {
            if (isOverlap(space, sp.bottomLeft, sp.topRight)) {
                return false;
            }
            if (isOutOfBoundary(space)) {
                return false;
            }
        }
        return true;
    }

    public static void connectThroughWalls(TETile[][] world, Square space, Position prevConnectPoint) {
        world[prevConnectPoint.x][prevConnectPoint.y] = Tileset.FLOOR;
        world[space.actualConnectPos.x][space.actualConnectPos.y] = Tileset.FLOOR;
    }

    public static Position actualConnectPos(Position prevConnectPoint, int usedSide) {
        Position actualPos = null;
        if (usedSide == 3) {
            actualPos = new Position(prevConnectPoint.x, prevConnectPoint.y - 1);
        }
        if (usedSide == 4) {
            actualPos = new Position(prevConnectPoint.x + 1, prevConnectPoint.y);
        }
        if (usedSide == 1) {
            actualPos = new Position(prevConnectPoint.x, prevConnectPoint.y + 1);
        }
        if (usedSide == 2) {
            actualPos = new Position(prevConnectPoint.x - 1, prevConnectPoint.y);
        }
        return actualPos;
    }

    public static Position spacePos(Square space, Position originPos, Position prevConnectPoint) {
        Position connectPos = space.connectPoint(originPos, space.usedSide);
        Position actualConnectPos = actualConnectPos(prevConnectPoint, space.usedSide);
        space.actualConnectPos = actualConnectPos;
        return new Position(actualConnectPos.x - connectPos.x, actualConnectPos.y - connectPos.y);
    }

    public static Position currentSpacePos(Square currentSpace, Position prevConnectPoint, int sideNum) {
        Position currentPos = new Position(0, 0);
        if (sideNum == 1) {
            currentSpace.usedSide = 3;
        }
        if (sideNum == 2) {
            currentSpace.usedSide = 4;
        }
        if (sideNum == 3) {
            currentSpace.usedSide = 1;
        }
        if (sideNum == 4) {
            currentSpace.usedSide = 2;
        }
        currentPos = spacePos(currentSpace, currentPos, prevConnectPoint);
        return currentPos;
    }

    public static void setupWorld(int width, int height, long seed) {
        SEED = seed;
        RANDOM = new Random(SEED);
        WIDTH = width;
        HEIGHT = height;
    }

    public static void addBackground(TETile[][] world) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    public static Position entrancePos() {
        int xPos = randomPos(WIDTH / 4, (WIDTH * 3 / 4));
        int yPos = randomPos(HEIGHT / 6, HEIGHT / 3);
        return new Position(xPos, yPos);
    }

    public static void setupEntrance(Room entrance) {
        entrance.p = entrancePos();
        entrance.bottomLeft = entrance.setBottomLeft(entrance.p);
        entrance.topRight = entrance.setTopRight(entrance.p, entrance.width, entrance.height);
    }

    public static ArrayList<Integer> sideWithConnection(Square space) {
        int side = space.randomSideExpt();
        return validSides(space, side);
    }

    public static TETile[][] addWorld(int width, int height, long seed) {
        setupWorld(width, height, seed);
        // 1. add the background.
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        addBackground(world);
        // 2. add the entrance room and the locked door.
        Room entrance = new Room();
        setupEntrance(entrance);

        ArrayList<Square> spaceBuilt = new ArrayList<> ();
        addSpace(world, entrance, spaceBuilt);
        addLockDoor(world, entrance);

        // 3. at least one side to have connecting point.
        entrance.usedSide = 1;
        ArrayList<Integer> validSides = sideWithConnection(entrance);

        // 4. use recursion to create room(hallway) branches for the entrance on each valid side.
        for (int s : validSides) {
            Position connectPoint = entrance.connectPoint(entrance.p, s);
            // recursive method.
            addMulti(world, connectPoint, s, spaceBuilt);
        }
        return world;
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = addWorld(80, 30, 47873);
        ter.renderFrame(world);
    }
}
