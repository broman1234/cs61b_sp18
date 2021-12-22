package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.ArrayList;
import java.util.Random;

public class MapGenerator {
    static int WIDTH = 80;
    static int HEIGHT = 30;

    static long SEED = 1;
    static Random RANDOM = new Random(SEED);

    // 1. how to generate a single rectangle of random size;
    //    room[i][j]
    //    hallway[i][j]
    //    * i and j should be random;
    //    * position: topright.x < world.width && topright.y < world.height &&
    //               bottomleft.x > 0 && bottomleft.y > 0;
    // 2. how to prevent two rectangles overlapping;
    //    * bottomleft1.x > topright2.x || bottomleft1.y > topright2.y
    //    || topright1.x < bottomleft2.x || topright1.y < bottomleft2.y
    // 3. a room can be connected to both rooms and hallways.
    //    a hallway can be connected to both rooms and hallways.
    // 4. a room --> a room or a hallway
    //    * use random for each side of a room to decide if that side should connect to
    //      a room or a hallway.
    //    * find the connecting point on chosen sides of the room for
    //      adding new rooms or hallways.
    //    * use random to decide which type of object (room or hallway)
    //      to connect with the room.
    //    * generate the room or the hallway,
    //      randomly generate the connecting position on the shape, calculate the corner position of the shape,
    //      it has to meet the requirement:
    //      on top and bottom side of the parent room, bottomleft.x < connectPoint.x && topright.x > connectPoint.x
    //      on left and right side of the parent room, bottomleft.y < connectPoint.y && topright.y > connectPoint.y
    // 5. a hallway --> a room or a hallway:
    //    * use random for each side of a room to decide if that side should connect to
    //      a room or a hallway.
    //    * find the connecting point on chosen sides of the room for
    //      adding new rooms or hallways.
    //    * use random to decide which type of object (room or hallway)
    //      to connect with the room.
    //    * generate the room or the hallway,

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
        while (!canAdd(currentRoom, spaceBuilt) && i < 2) {
            currentRoom = new Room();
            currentRoom.p = currentRoomPos(currentRoom, prevConnectPoint, side);
            currentRoom.bottomLeft = currentRoom.setBottomLeft(currentRoom.p);
            currentRoom.topRight = currentRoom.setTopRight(currentRoom.p, currentRoom.width, currentRoom.height);
            i += 1;
        }
        if (canAdd(currentRoom, spaceBuilt)) {
            addSpace(world, currentRoom);
            world[prevConnectPoint.x][prevConnectPoint.y] = Tileset.FLOOR;
            world[currentRoom.actualConnectPos.x][currentRoom.actualConnectPos.y] = Tileset.FLOOR;

            // add the room into the space arraylist.
            spaceBuilt.add(currentRoom);

            int validS = currentRoom.randomSideExpt();
            ArrayList<Integer> validSides = validSides(currentRoom, validS);

            for (int s : validSides) {
                Position connectPoint = currentRoom.connectPoint(currentRoom.p, s);
                // recursive method.
                addMulti(world, connectPoint, s, spaceBuilt);
            }
        }
    }

    public static boolean canAdd(SquareSpace space, ArrayList<SquareSpace> spaceBuilt) {
        for (SquareSpace sp: spaceBuilt) {
            Position bottomLeft = sp.getBottomLeft();
            Position topRight = sp.getTopRight();
            if (!(space.getBottomLeft().x > topRight.x || space.getBottomLeft().y > topRight.y ||
                    space.getTopRight().x < bottomLeft.x || space.getTopRight().y < bottomLeft.y)) {
                return false;
            }
            if (!(space.getTopRight().x < WIDTH && space.getTopRight().y < HEIGHT &&
                    space.getBottomLeft().x > 0 && space.getBottomLeft().y > 0)) {
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

    public static void addMultiHallway(TETile[][] world, Position prevConnectPoint, int side, ArrayList<SquareSpace> spaceBuilt) {
        // create a hallway.
        Hallway currentHall = new Hallway();
        setupHall(currentHall, prevConnectPoint, side);

        // if overlapped or out of boundary, try to randomly generate the hallway two more times to meet the requirements.
        int i = 0;
        while (!canAdd(currentHall, spaceBuilt) && i < 2) {
            currentHall = new Hallway();
            currentHall.p = currentHallPos(currentHall, prevConnectPoint, side);
            currentHall.bottomLeft = currentHall.setBottomLeft(currentHall.p);
            currentHall.topRight = currentHall.setTopRight(currentHall.p, currentHall.width, currentHall.height);
            i += 1;
        }

        // if not overlapped or out of boundary, add the hallway created above.
        if (canAdd(currentHall, spaceBuilt)) {
            addSpace(world, currentHall);
            world[prevConnectPoint.x][prevConnectPoint.y] = Tileset.FLOOR;
            world[currentHall.actualConnectPos.x][currentHall.actualConnectPos.y] = Tileset.FLOOR;

            // add the hallway into the space arraylist.
            spaceBuilt.add(currentHall);

            // decide which sides of the hallway to have branches.
            int validS = currentHall.randomSideExpt();
            ArrayList<Integer> validSides = validSides(currentHall, validS);

            // use recursion to add rooms or hallways for each valid side.
            for (int s : validSides) {
                Position connectPoint = currentHall.connectPoint(currentHall.p, s);
                // recursive method.
                addMulti(world, connectPoint, s, spaceBuilt);
            }
        }
    }

    public static Position currentHallPos(Hallway currentHall, Position prevConnectPoint, int sideNum) {
        Position currentPos = new Position(0, 0);
        if (sideNum == 1) {
            currentHall.usedSide = 3;
            Position connectPos = currentHall.connectPoint(currentPos, currentHall.usedSide);
            Position actualConnectPos = new Position(prevConnectPoint.x, prevConnectPoint.y - 1);
            currentHall.actualConnectPos = actualConnectPos;

            currentPos = new Position(actualConnectPos.x - connectPos.x, actualConnectPos.y - connectPos.y);
        }
        if (sideNum == 2) {
            currentHall.usedSide = 4;
            Position connectPos = currentHall.connectPoint(currentPos, currentHall.usedSide);
            Position actualConnectPos = new Position(prevConnectPoint.x + 1, prevConnectPoint.y);
            currentHall.actualConnectPos = actualConnectPos;

            currentPos = new Position(actualConnectPos.x - connectPos.x, actualConnectPos.y - connectPos.y);
        }
        if (sideNum == 3) {
            currentHall.usedSide = 1;
            Position connectPos = currentHall.connectPoint(currentPos, currentHall.usedSide);
            Position actualConnectPos = new Position(prevConnectPoint.x, prevConnectPoint.y + 1);
            currentHall.actualConnectPos = actualConnectPos;

            currentPos = new Position(actualConnectPos.x - connectPos.x, actualConnectPos.y - connectPos.y);
        }
        if (sideNum == 4) {
            currentHall.usedSide = 2;
            Position connectPos = currentHall.connectPoint(currentPos, currentHall.usedSide);
            Position actualConnectPos = new Position(prevConnectPoint.x - 1, prevConnectPoint.y);
            currentHall.actualConnectPos = actualConnectPos;

            currentPos = new Position(actualConnectPos.x - connectPos.x, actualConnectPos.y - connectPos.y);
        }
        return currentPos;
    }

    public static Position currentRoomPos(Room currentRoom, Position prevConnectPoint, int sideNum) {
        Position currentPos = new Position(0, 0);
        if (sideNum == 1) {
            currentRoom.usedSide = 3;
            Position connectPos = currentRoom.connectPoint(currentPos, currentRoom.usedSide);
            Position actualConnectPos = new Position(prevConnectPoint.x, prevConnectPoint.y - 1);
            currentRoom.actualConnectPos = actualConnectPos;

            currentPos = new Position(actualConnectPos.x - connectPos.x, actualConnectPos.y - connectPos.y);
        }
        if (sideNum == 2) {
            currentRoom.usedSide = 4;
            Position connectPos = currentRoom.connectPoint(currentPos, currentRoom.usedSide);
            Position actualConnectPos = new Position(prevConnectPoint.x + 1, prevConnectPoint.y);
            currentRoom.actualConnectPos = actualConnectPos;

            currentPos = new Position(actualConnectPos.x - connectPos.x, actualConnectPos.y - connectPos.y);
        }
        if (sideNum == 3) {
            currentRoom.usedSide = 1;
            Position connectPos = currentRoom.connectPoint(currentPos, currentRoom.usedSide);
            Position actualConnectPos = new Position(prevConnectPoint.x, prevConnectPoint.y + 1);
            currentRoom.actualConnectPos = actualConnectPos;

            currentPos = new Position(actualConnectPos.x - connectPos.x, actualConnectPos.y - connectPos.y);
        }
        if (sideNum == 4) {
            currentRoom.usedSide = 2;
            Position connectPos = currentRoom.connectPoint(currentPos, currentRoom.usedSide);
            Position actualConnectPos = new Position(prevConnectPoint.x - 1, prevConnectPoint.y);
            currentRoom.actualConnectPos = actualConnectPos;

            currentPos = new Position(actualConnectPos.x - connectPos.x, actualConnectPos.y - connectPos.y);
        }
        return currentPos;
    }

    public static void setupWorld(int width, int height, long seed) {
        SEED = seed;
        RANDOM = new Random(SEED);
        WIDTH = width;
        HEIGHT = height;
    }

    public static TETile[][] addWorld(int width, int height, long seed) {
        setupWorld(width, height, seed);
        // 1. add the background.
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // 2. add the entrance room and the locked door.
        Room entrance = new Room();
        int xPos = randomPos(WIDTH / 4, (WIDTH * 3 / 4));
        int yPos = randomPos(HEIGHT / 6, HEIGHT / 3);
        entrance.p = new Position(xPos, yPos);
        entrance.bottomLeft = entrance.setBottomLeft(entrance.p);
        entrance.topRight = entrance.setTopRight(entrance.p, entrance.width, entrance.height);
        addSpace(world, entrance);
        addLockDoor(world, entrance);

        // 3. store the entrance into a space arraylist
        ArrayList<SquareSpace> spaceBuilt = new ArrayList<> ();
        spaceBuilt.add(entrance);

        // 4. at least one side to have connecting point.
        entrance.usedSide = 1;
        int side = entrance.randomSideExpt();
        ArrayList<Integer> validSides = validSides(entrance, side);

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
