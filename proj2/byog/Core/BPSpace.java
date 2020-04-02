package byog.Core;

import java.util.Random;

/*
 Binary Partition Space. Create random number of Space and split them into two
 boards, which left boards are defined as out side of the room, and right boards
 are insight.
 */
public class BPSpace {
    private static final int MIN_SIZE = 6;

    private int width;
    private int height;
    Position pos;
    BPSpace leftBoard;
    BPSpace rightBoard;
    Room room;

    public BPSpace(Position pos, int width, int height) {
        this.pos = pos;
        this.width = width;
        this.height = height;
        this.leftBoard = null;
        this.rightBoard = null;
        this.room = null;
    }

    public boolean partition(Random random) {
        //if already split, stop partition
        if (leftBoard != null) {
            return false;
        }

        //next direction to partition
        boolean horizontal;
        if (height > width) {
            horizontal = true;
        } else if (width > height) {
            horizontal = false;
        } else {
            horizontal = random.nextBoolean();
        }

        //Make sure that both boards can have MIN_SIZE.
        int max = (horizontal ? height : width) - MIN_SIZE;
        if (max <= MIN_SIZE) {
            return false;
        }

        //Generate split point
        int split = random.nextInt(max);
        //Adjust split point so there is at least MIN_SIZE in both partitions
        if (split < MIN_SIZE) {
            split = MIN_SIZE;
        }

        if (horizontal) {
            leftBoard = new BPSpace(pos, width, split);
            rightBoard = new BPSpace(new Position(pos.x, pos.y + split),
                    width, height - split);
        } else {
            leftBoard = new BPSpace(pos, split, height);
            rightBoard = new BPSpace(new Position(pos.x + split, pos.y),
                    width - split, height);
        }

        return true;
    }

    public void buildRoom(Random random) {
        //If current area already has boards areas, then we cannot build room in it
        //instead, go to check its boards
        if (leftBoard != null) {
            leftBoard.buildRoom(random);
            rightBoard.buildRoom(random);
        } else {
            int offSetX = (width - MIN_SIZE <= 0) ? 0 : random.nextInt(width - MIN_SIZE);
            int offSetY = (height - MIN_SIZE <= 0) ? 0 : random.nextInt(height - MIN_SIZE);

            //Room is at least one grid away from the left, right, top edges of current space.
            Position roomPos = new Position(pos.x + offSetX, pos.y + offSetY);
            int roomWidth = Math.max(random.nextInt(width - offSetX), MIN_SIZE);
            int roomHeight = Math.max(random.nextInt(height - offSetY), MIN_SIZE);
            room = new Room(roomPos, roomWidth, roomHeight);
        }
    }
}
