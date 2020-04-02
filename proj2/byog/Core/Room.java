package byog.Core;

public class Room {
    int width;
    int height;
    Position pos;

    public Room(Position pos, int width, int height) {
        this.width = width;
        this.height = height;
        this.pos = pos;
    }
}
