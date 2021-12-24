package byog.Core;

public class Hallway implements SquareSpace{
    int maxSide = 10;
    int minSide = 3;
    int bottomSideNum = 1;
    int rightSideNum = 2;
    int topSideNum = 3;
    int leftSideNum = 4;
    int usedSide;
    Position actualConnectPos;
    int width = setWidth();
    int height = setHeight();
    Position p;
    Position bottomLeft;
    Position topRight;

    public int setWidth() {
        int wid = MapGenerator.RANDOM.nextInt(2);
        switch (wid) {
            case 0: return minSide;
            case 1: return randomSize();
            default: return -1;
        }
    }

    public int setHeight() {
        if (width == minSide) {
            return randomSize();
        }
        return minSide;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Position getPosition() {
        return p;
    }


    public Position setBottomLeft(Position p) {
        return p;
    }

    public Position setTopRight(Position position, int w, int h) {
        int x = position.x + w - 1;
        int y = position.y + h - 1;
        return new Position(x, y);
    }

    public Position getBottomLeft() {
        return bottomLeft;
    }

    public Position getTopRight() {
        return topRight;
    }

    public Position getActualConnectPos() {
        return actualConnectPos;
    }

    public int randomSize() {
        return MapGenerator.RANDOM.nextInt(maxSide - minSide + 1) + minSide;
    }

    public int randomSide() {
        int sideNum = MapGenerator.RANDOM.nextInt(4);
        switch (sideNum) {
            case 0: return bottomSideNum;
            case 1: return rightSideNum;
            case 2: return topSideNum;
            case 3: return leftSideNum;
            default: return -1;
        }
    }

    public int randomSideExpt() {
        int side = usedSide;
        while (side == usedSide) {
            side = randomSide();
        }
        return side;
    }

    public boolean hasConnectPoint() {
        int point = MapGenerator.RANDOM.nextInt(2);
        return point == 1;
    }

    public Position connectPoint(Position p, int side) {
        int xPos = 0, yPos = 0;
        if (side == 1) {
            xPos = MapGenerator.randomPos(1, width - 2);
            yPos = 0;
        }
        if (side == 2) {
            xPos = width - 1;
            yPos = MapGenerator.randomPos(1, height - 2);

        }
        if (side == 3) {
            xPos = MapGenerator.randomPos(1, width - 2);
            yPos = height - 1;
        }
        if (side == 4) {
            xPos = 0;
            yPos = MapGenerator.randomPos(1, height - 2);
        }
        return new Position(xPos + p.x, yPos + p.y);
    }
}
