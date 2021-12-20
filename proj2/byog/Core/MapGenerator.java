package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.awt.event.TextListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class MapGenerator {
    static final int WIDTH = 80;
    static final int HEIGHT = 35;

    static final long SEED = 2873123;
    static final Random RANDOM = new Random(SEED);


    // 1. how to generate a single rectangle of random size;
    //    room[i][j]
    //    hallway[i][j]: i == 0 or j == 0
    //    * i and j should be random;
    //    * position: topright.x < world.width && topright.y < world.height &&
    //               bottomleft.x > 0 && bottomleft.y > 0;
    // 2. how to prevent two rectangles overlapping;
    //    * bottomleft1.x > topright2.x || bottomleft1.y > topright2.y
    //    || topright1.x < bottomleft2.x || topright1.y < bottomleft2.y
    // 3. a room can be connected to boths rooms and hallways.
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
    // 5. how to generate a hallway: consider two types of hallway: ___, |.
    // 6. a hallway --> a room or a hallway:
    //    * use random for each side of a room to decide if that side should connect to
    //      a room or a hallway.
    //    * find the connecting point on chosen sides of the room for
    //      adding new rooms or hallways.
    //    * use random to decide which type of object (room or hallway)
    //      to connect with the room.
    //    * generate the room or the hallway,

    // process:
    // 1. generate and add a single room.
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
        return RANDOM.nextInt(max - min) + min;
    }

    public static boolean isRoom() {
        int shapeNum = RANDOM.nextInt(2);
        return shapeNum == 1;
    }

    public static ArrayList<Integer> validSides(Room room, int side) {
        ArrayList<Integer> validSides = new ArrayList<> ();
        validSides.add(side);
        for (int i = 1; i < 5; i += 1) {
            if (i != side && room.hasConnectPoint()) {
                validSides.add(i);
            }
        }
        return validSides;
    }

    // recursive method.
    public static void addMulti() {
        if (isRoom()) {
            addMultiRoom();
        } else {
            addMultiHallway();
        }
    }

    public static void addMultiRoom(TETile[][] world, Position prevConnectPoint, int side, ArrayList<SquareSpace> spaceBuilt) {
        Room currentRoom = new Room();
        currentRoom.p = currentRoomPos(currentRoom, prevConnectPoint, side);
        currentRoom.bottomLeft = currentRoom.setBottomLeft(currentRoom.p);
        currentRoom.topRight = currentRoom.setTopRight(currentRoom.p, currentRoom.width, currentRoom.height);
        if (!isOverlap(currentRoom, spaceBuilt)) {
            addSpace(world, currentRoom);
            world[currentRoom.actualConnectPos.x][currentRoom.actualConnectPos.y] = Tileset.FLOOR;
        }
        // add the current room into the space arraylist.
        spaceBuilt.add(currentRoom);
        // continue tomorrow.
        int validS = currentRoom.randomSideExpt();
        ArrayList<Integer> validSides = validSides(currentRoom, validS);

        for (int s : validSides) {

        }
    }

    public static boolean isOverlap(SquareSpace space, ArrayList<SquareSpace> spaceBuilt) {
        for (SquareSpace sp: spaceBuilt) {
            Position bottomLeft = sp.getBottomLeft();
            Position topRight = sp.getTopRight();
            if (!(space.getBottomLeft().x > topRight.x || space.getBottomLeft().y > topRight.y ||
                    space.getTopRight().x < bottomLeft.x || space.getTopRight().y < bottomLeft.y)) {
                return true;
            }
            if (space.getTopRight().x < WIDTH && space.getTopRight().y < HEIGHT &&
                    space.getBottomLeft().x > 0 && space.getBottomLeft().y > 0) {
                return true;
            }
        }
        return false;
    }

    public static void addMultiHallway(Hallway prevHallway, Position connectPoint, int side) {

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

    // 2. find the connecting point on random sides of the room for
    //    adding new rooms or hallways.
    // 3. use random to decide which type of object (room or hallway)
    //    to connect with the room.
    // 4. generate the room or the hallway, it has to meet the requirement:
    //    on top and bottom side of the parent room, bottomleft.x < connectPoint.x && topright.x > connectPoint.x
    //    on left and right side of the parent room, bottomleft.y < connectPoint.y && topright.y > connectPoint.y
    // 5.
    public static void main(String[] args) {
        // 1. add the background.
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // 2. add the start room and the locked door.
        Room entrance = new Room();
        int xPos = randomPos(25, 45);
        int yPos = randomPos(5, 10);
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

        // 5. use recursion to create branch rooms or hallways.
        for (int s : validSides) {
            Position connectPoint = entrance.connectPoint(entrance.p, s);
            world[connectPoint.x][connectPoint.y] = Tileset.FLOOR;
            // recursive method.
        }
        ter.renderFrame(world);
    }
}
