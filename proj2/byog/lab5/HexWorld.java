package byog.lab5;
//import org.junit.Test;
//import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 70;
    private static final int HEIGHT = 70;

    /*
     *Adds a hexagon of side length s to a given position(x, y) in the TETile world
     */
    public static void addHexagon(TETile[][] world, int x, int y, int s, TETile tile) {
        if (s < 2) {
            throw new IllegalArgumentException("Hexagon must be at least size 2.");
        }
        addLowerHalf(world, x, y, s, tile);
        addUpperHalf(world, x, y + s * 2 - 1, s, tile); //redefine the start point
    }

    /*
     Constructs the lower half of the hexagon from bottom to middle.
     */
    public static void addLowerHalf(TETile[][] world, int x, int y, int s, TETile tile) {
        //the height of the half hexagon is the same as the size
        int height = s;

        //draw every raw of the hexagon from bottom to middle
        for (int i = 0; i < height; i += 1) {
            for (int j = 0; j < s + 2 * i; j += 1) {
                //decide the total number of pixel at every raw
                //the initial position (x, y) starts at the left side of the
                //bottom, therefore when raw increases i, the start point will
                //move to the (-i, i) compared to current start point
                world[(x - i) + j][y + i] = tile;
            }
        }
    }

    public static void addUpperHalf(TETile[][] world, int x, int y, int s, TETile tile) {
        //the same method as addLowerHalf, from up to the middle
        int depth = s;

        for (int i = 0; i < depth; i += 1) {
            for (int j = 0; j < s + 2 * i; j += 1) {
                world[(x - i) + j][y - i] = tile;
            }
        }
    }

    // Creates a tesselation of Hexagons of same side length s, which has centre
    // column position as (x, y)
    public static void addTesselationHexagons(TETile[][] world, int x, int y, int s) {
        for (int dis = -s + 1; dis < s; dis += 1) {
            for (int i = 0; i < 2 * s - 1; i += 1) {
                addColumnOfHexagons(world, x, y, s, dis);
            }
        }
    }

    /*
    总体思想：将大六边形竖排分割成 2s-1 列，以最低点坐标为起点(x, y)，从左至右依次loop出每一列最下六边形的
    起始坐标，并按照列次画出
    @Param dis is the distance away from centre, left is negative, right is positive
     */
    private static void addColumnOfHexagons(TETile[][] world, int x, int y, int s, int dis) {
        int numOfHexagons = 2 * s - 1 - Math.abs(dis);
        int heightOfEachHax = 2 * s;
        int startX = calcStartX(x, s, dis);
        int startY = calcStartY(y, s, dis);
        TETile tile;

        for (int i = 0; i < numOfHexagons; i += 1) {
            startY += heightOfEachHax;
            tile = randomTile();
            addHexagon(world, startX, startY, s, tile);
        }
    }

    /*
    calculates x start coordinate for column of hexagons
     */
    private static int calcStartX(int startX, int size, int dis) {
        int offset = 2 * Math.abs(dis) * (size - 1) + Math.abs(dis);
        if (dis >= 0) {
            return startX + offset;
        } else {
            return startX - offset;
        }
    }

    /*
    calculates y start coordinate for column of hexagons
     */
    private static int calcStartY(int startY, int size, int dis) {
        int offset = size * Math.abs(dis);
        if (dis == 0) {
            return startY;
        } else {
            return startY + offset;
        }
    }

    /* Picks a RANDOM tile with a 20% change of being
     *  a wall, 20% chance of being a flower, 20% chance
     *  of being a grass, 20% chance of being a tree,
     * and 20% chance of being empty space.
     */
    private static TETile randomTile() {
        Random dice = new Random();
        int tileNum = dice.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.FLOOR;
            case 4: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

       //create and initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        //test the method addHexagon
        //HexWorld.addHexagon(world, 20, 0, 3, Tileset.WALL);
        //HexWorld.addHexagon(world, 30, 5, 2, Tileset.TREE);
        HexWorld.addTesselationHexagons(world, 29, 0, 3);
        ter.renderFrame(world);

    }
}
