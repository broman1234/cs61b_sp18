package byog.Core;

import java.io.Serializable;

class Position implements Serializable {
    int x;
    int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
