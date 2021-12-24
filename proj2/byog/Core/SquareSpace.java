package byog.Core;

public interface SquareSpace {
    int setWidth();

    int setHeight();

    int getWidth();

    int getHeight();

    Position getPosition();

    Position setBottomLeft(Position p);

    Position setTopRight(Position position, int w, int h);

    Position getBottomLeft();

    Position getTopRight();

    Position getActualConnectPos();

    int randomSize();

    int randomSide();

    int randomSideExpt();

    boolean hasConnectPoint();

    Position connectPoint(Position p, int side);
}
