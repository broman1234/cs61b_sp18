package byog.Core;

public class Hallway extends Square{
    public Hallway() {
        super();
        this.maxSide = 7;
        this.minSide = 3;
        this.width = setWidth();
        this.height = setHeight();
    }
}
