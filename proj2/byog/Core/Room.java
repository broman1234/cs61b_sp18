package byog.Core;

public class Room extends Square{
    public Room() {
        super();
        this.maxSide = 8;
        this.minSide = 4;
        this.width = setWidth();
        this.height = setHeight();
    }
}
