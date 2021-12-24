package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.ArrayList;
import java.util.Random;

public class MapGenerator {
    static int WIDTH = 80;
    static int HEIGHT = 30;
    static final int GENERATIONLIMIT = 5;
    static long SEED = 1;
    static Random RANDOM = new Random(SEED);

    public static void addSpace(TETile[][] world, SquareSpace space) {
        for (int i = 0; i < space.getWidth(); i += 1) {
            for (int j = 0; j < space.getHeight(); j += 1) {
                if (i == 0 || i == space.getWidth() - 1 || j == 0 || j == space.getHeight() - 1) {
                    world[i + space.getPosition().x][j + space.getPosition().y] = Tileset.WALL;
                } else {
                    world[i + space.getPosition().x][j + space.getPosition().y] = Tileset.FLOOR;
                }
            }
        }
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

    public static ArrayList<Integer> validSides(SquareSpace space, int side) {
        ArrayList<Integer> validSides = new ArrayList<> ();
        validSides.add(side);
        for (int i = 1; i < 5; i += 1) {
            if (i != side && space.hasConnectPoint()) {
                validSides.add(i);
            }
        }
        return validSides;
    }

    public static void addMulti(TETile[][] world, Position prevConnectPoint, int side, ArrayList<SquareSpace> spaceBuilt) {
        if (isRoom()) {
            addMultiRoom(world, prevConnectPoint, side, spaceBuilt);
        } else {
            addMultiHallway(world, prevConnectPoint, side, spaceBuilt);
        }
    }

    public static void setupRoom(Room room, Position prevConnectPoint, int side) {
        room.p = currentRoomPos(room, prevConnectPoint, side);
        room.bottomLeft = room.setBottomLeft(room.p);
        room.topRight = room.setTopRight(room.p, room.width, room.height);
    }

    public static void addMultiRoom(TETile[][] world, Position prevConnectPoint, int side, ArrayList<SquareSpace> spaceBuilt) {
        Room currentRoom = new Room();
        setupRoom(currentRoom, prevConnectPoint, side);
        int i = 0;
        while (!canAdd(currentRoom, spaceBuilt) && i < GENERATIONLIMIT) {
            currentRoom = new Room();
            setupRoom(currentRoom, prevConnectPoint, side);
            i += 1;
        }
        if (canAdd(currentRoom, spaceBuilt)) {
            addSpace(world, currentRoom);
            connectThroughWalls(world, currentRoom, prevConnectPoint);

            // add the room into the space arraylist.
            spaceBuilt.add(currentRoom);

            ArrayList<Integer> validSides = sideWithConnection(currentRoom);

            for (int s : validSides) {
                Position connectPoint = currentRoom.connectPoint(currentRoom.p, s);
                // recursive method.
                addMulti(world, connectPoint, s, spaceBuilt);
            }
        }
    }

    public static boolean isOverlap(SquareSpace space, Position bottomLeft, Position topRight) {
        return !(space.getBottomLeft().x > topRight.x || space.getBottomLeft().y > topRight.y || space.getTopRight().x < bottomLeft.x || space.getTopRight().y < bottomLeft.y);
    }

    public static boolean isOutOfBoundary(SquareSpace space) {
        return !(space.getTopRight().x < WIDTH && space.getTopRight().y < HEIGHT && space.getBottomLeft().x > 0 && space.getBottomLeft().y > 0);
    }

    public static boolean canAdd(SquareSpace space, ArrayList<SquareSpace> spaceBuilt) {
        for (SquareSpace sp: spaceBuilt) {
            Position bottomLeft = sp.getBottomLeft();
            Position topRight = sp.getTopRight();
            if (isOverlap(space, bottomLeft, topRight)) {
                return false;
            }
            if (isOutOfBoundary(space)) {
                return false;
            }
        }
        return true;
    }

    public static void setupHall(Hallway hallway, Position prevConnectPoint, int side) {
        hallway.p = currentHallPos(hallway, prevConnectPoint, side);
        hallway.bottomLeft = hallway.setBottomLeft(hallway.p);
        hallway.topRight = hallway.setTopRight(hallway.p, hallway.width, hallway.height);
    }

    public static void connectThroughWalls(TETile[][] world, SquareSpace space, Position prevConnectPoint) {
        world[prevConnectPoint.x][prevConnectPoint.y] = Tileset.FLOOR;
        world[space.getActualConnectPos().x][space.getActualConnectPos().y] = Tileset.FLOOR;
    }

    public static void addMultiHallway(TETile[][] world, Position prevConnectPoint, int side, ArrayList<SquareSpace> spaceBuilt) {
        // create a hallway.
        Hallway currentHall = new Hallway();
        setupHall(currentHall, prevConnectPoint, side);

        // if overlapped or out of boundary, try to randomly generate the hallway two more times to meet the requirements.
        int i = 0;
        while (!canAdd(currentHall, spaceBuilt) && i < GENERATIONLIMIT) {
            currentHall = new Hallway();
            setupHall(currentHall, prevConnectPoint, side);
            i += 1;
        }

        // if not overlapped or out of boundary, add the hallway created above.
        if (canAdd(currentHall, spaceBuilt)) {
            addSpace(world, currentHall);
            connectThroughWalls(world, currentHall, prevConnectPoint);

            // add the hallway into the space arraylist.
            spaceBuilt.add(currentHall);

            // decide which sides of the hallway to have branches.
            ArrayList<Integer> validSides = sideWithConnection(currentHall);

            // use recursion to add rooms or hallways for each valid side.
            for (int s : validSides) {
                Position connectPoint = currentHall.connectPoint(currentHall.p, s);
                // recursive method.
                addMulti(world, connectPoint, s, spaceBuilt);
            }
        }
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

    public static Position currentHallPos(Hallway currentHall, Position prevConnectPoint, int sideNum) {
        Position currentPos = new Position(0, 0);
        if (sideNum == 1) {
            currentHall.usedSide = 3;
        }
        if (sideNum == 2) {
            currentHall.usedSide = 4;
        }
        if (sideNum == 3) {
            currentHall.usedSide = 1;
        }
        if (sideNum == 4) {
            currentHall.usedSide = 2;
        }
        currentPos = hallCurrentPos(currentHall, currentPos, prevConnectPoint);
        return currentPos;
    }

    public static Position hallCurrentPos(Hallway hallway, Position originPos, Position prevConnectPoint) {
        Position connectPos = hallway.connectPoint(originPos, hallway.usedSide);
        Position actualConnectPos = actualConnectPos(prevConnectPoint, hallway.usedSide);
        hallway.actualConnectPos = actualConnectPos;
        return new Position(actualConnectPos.x - connectPos.x, actualConnectPos.y - connectPos.y);
    }

    public static Position roomCurrentPos(Room room, Position originPos, Position prevConnectPoint) {
        Position connectPos = room.connectPoint(originPos, room.usedSide);
        Position actualConnectPos = actualConnectPos(prevConnectPoint, room.usedSide);
        room.actualConnectPos = actualConnectPos;
        return new Position(actualConnectPos.x - connectPos.x, actualConnectPos.y - connectPos.y);
    }

    public static Position currentRoomPos(Room currentRoom, Position prevConnectPoint, int sideNum) {
        Position currentPos = new Position(0, 0);
        if (sideNum == 1) {
            currentRoom.usedSide = 3;
        }
        if (sideNum == 2) {
            currentRoom.usedSide = 4;
        }
        if (sideNum == 3) {
            currentRoom.usedSide = 1;
        }
        if (sideNum == 4) {
            currentRoom.usedSide = 2;
        }
        currentPos = roomCurrentPos(currentRoom, currentPos, prevConnectPoint);
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

    public static ArrayList<Integer> sideWithConnection(SquareSpace space) {
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
        addSpace(world, entrance);
        addLockDoor(world, entrance);

        // 3. store the entrance into a new space arraylist
        ArrayList<SquareSpace> spaceBuilt = new ArrayList<> ();
        spaceBuilt.add(entrance);

        // 4. at least one side to have connecting point.
        entrance.usedSide = 1;
        ArrayList<Integer> validSides = sideWithConnection(entrance);

        // 5. use recursion to create room(hallway) branches for the entrance on each valid side.
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
        TETile[][] world = addWorld(80, 30, 886);
        ter.renderFrame(world);
    }
}
