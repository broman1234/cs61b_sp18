package byog.Core;

public interface SquareSpace {
    int setWidth();

    int setHeight();

    int getWidth();

    int getHeight();

    int getUsedSide();

    Position getActualConnectPos();

    Position getPosition();

    Position setBottomLeft(Position p);

    Position setTopRight(Position position, int w, int h);

    public Position getBottomLeft();

    public Position getTopRight();

    int randomSize();

    int randomSide();

    int randomSideExpt();

    boolean hasConnectPoint();

    Position connectPoint(Position p, int side);
}
