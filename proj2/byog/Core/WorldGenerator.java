package byog.Core;

//import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/*
 基本思想：
 Step 1：将世界初始为所有tiles均为WALL背景
 Step 2：定义Room class，生成随机数量的ROOMs，其大小位置均随机，然后创建Queue收集ROOMs用于连接
 Step 3：依次将Queue中的ROOM块压出添加到地图上，每次取两块rooms，计算中心点，并连接成floor
 Step 4：将redundant的WALL块重新设置成NOTHING，完成背景和墙面的分割
 Step 5：在建立的Room class中随机选取一个room加入人物
 Optional: Step 6：生成随机数量的障碍物，并制定死亡规则
 */

public class WorldGenerator {

    //Test
    /*
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        Random random = new Random();
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        NewWorld.createWorld(world, random);
        ter.renderFrame(world);
    }
     */

    //Create a random world
    public static Position createWorld(TETile[][] world, Random random) {
        setWorldBackGroundAsWalls(world); //Step 1
        Queue<Room> rooms = placeRoomsToWorld(world, random); // Step 2
        connectRoomsInWorld(world, rooms, random); // Step 3
        removeRedundantWalls(world); // Step 4
        addMonster(world, rooms, random);
        return addPlayer(world, rooms, random); // Step 5

    }

    //Add random nums of monster to the world, within random place
    private static void addMonster(TETile[][] world, Queue<Room> rooms, Random random) {
        List<Room> birthPlaces = new ArrayList<>();
        for (Room room : rooms) {
            birthPlaces.add(room);
        }
        int count = rooms.size();
        while (count > 0) {
            int i = random.nextInt(count);
            Room birthRoom = birthPlaces.get(i);
            //Make sure that monster does not born in wall.
            int monsterX = birthRoom.pos.x + 1 + random.nextInt(birthRoom.width - 2);
            int monsterY = birthRoom.pos.y + 1 + random.nextInt(birthRoom.height - 2);
            world[monsterX][monsterY] = Tileset.GRASS;
            count -= 1;
        }
    }

    //Add an avatar to the world, within a random room
    //Return position of the avatar
    private static Position addPlayer(TETile[][] world, Queue<Room> rooms, Random random) {
        List<Room> birthPlaces = new ArrayList<>();
        for (Room room : rooms) {
            birthPlaces.add(room);
        }

        int i = random.nextInt(birthPlaces.size());
        Room birthRoom = birthPlaces.get(i);
        //Make sure that avatar does not born in wall.
        int playerX = birthRoom.pos.x + 1 + random.nextInt(birthRoom.width - 2);
        int playerY = birthRoom.pos.y + 1 + random.nextInt(birthRoom.height - 2);
        world[playerX][playerY] = Tileset.PLAYER;

        return new Position(playerX, playerY);
    }


    //Connect rooms using floor tile hallways with no walls
    //Note that hallways is built either from left or from bottom
    private static void connectRoomsInWorld(TETile[][] world, Queue<Room> rooms, Random random) {
        Queue<Room> toBeConnected = new LinkedList<>();

        //it would changed the rooms itself after loop if toBeConnected is set equal to rooms
        for (Room room : rooms) {
            toBeConnected.offer(room);
        }

        while (toBeConnected.size() > 1) {
            Room roomA = toBeConnected.poll();
            Room roomB = toBeConnected.poll();

            //calculate centre position of each room.
            Position roomACentre = new Position(roomA.pos.x + (roomA.width / 2),
                    roomA.pos.y + (roomA.height / 2));
            Position roomBCentre = new Position(roomB.pos.x + (roomB.width / 2),
                    roomB.pos.y + (roomB.height / 2));

            //Choose left room's x position as horizontal hallway's start x position
            //Choose lower room's y position as horizontal hallway's start y position
            int horzStartX = Math.min(roomACentre.x, roomBCentre.x);
            int horzEndX = Math.max(roomACentre.x, roomBCentre.x);
            int horzLength = horzEndX - horzStartX;
            int horzStartY = Math.min(roomACentre.y, roomBCentre.y);
            Position horzHallwayPos = new Position(horzStartX, horzStartY);

            //Choose lower room's y position as vertical hallway's start y position
            //Choose higher room's x position as vertical hallway's start x position
            int vertStartY = Math.min(roomACentre.y, roomBCentre.y);
            int vertEndY = Math.max(roomACentre.y, roomBCentre.y);
            int vertLength = vertEndY - vertStartY;
            int vertStartX = (roomACentre.y >= roomBCentre.y) ? roomACentre.x : roomBCentre.x;
            Position vertHallwayPos = new Position(vertStartX, vertStartY);

            //Build hallways only use floor tile
            addFloorRowToWorld(world, horzLength, horzHallwayPos);
            addFloorColToWorld(world, vertLength, vertHallwayPos);

            //Pick a random room from these two connected rooms and reinsert it into queue,
            //thus guaranteeing already connected rooms can be connected to other rooms


            int i = random.nextInt(2);
            switch (i) {
                default:
                case 0:
                    toBeConnected.offer(roomA);
                    break;
                case 1 :
                    toBeConnected.offer(roomB);
                    break;
            }


        }
    }


    //Create Random Room with random size and random position, and return an Queue
    //of Rooms prepared for connected
    private static Queue<Room> placeRoomsToWorld(TETile[][] world, Random random) {
        List<BPSpace> space = new LinkedList<>(); //to store the partition space
        Queue<BPSpace> queue = new LinkedList<>(); //to store the space used to be partition
        Queue<Room> rooms = new LinkedList<>(); //to store the placed rooms prepared for connection

        //set place length as world.length - 1, save place for HUD.
        //set the start point root
        BPSpace root = new BPSpace(new Position(0, 0), world.length, world[0].length - 1);
        space.add(root);
        queue.offer(root);

        int num = 15 + random.nextInt(10); //A suitable num of rooms
        while (num > 0 && !queue.isEmpty()) {
            BPSpace toPartition = queue.poll();
            if (toPartition.partition(random)) {
                space.add(toPartition.leftBoard);
                space.add(toPartition.rightBoard);
                //try to partition every board
                queue.offer(toPartition.leftBoard);
                queue.offer(toPartition.rightBoard);
            }
            num -= 1;
        }
        root.buildRoom(random);
        for (BPSpace subspace : space) {
            if (subspace.room != null) {
                addRoomToWorld(world, subspace.room);
                rooms.offer(subspace.room);
            }
        }
        return rooms;
    }

    //Add a room to the world
    private static void addRoomToWorld(TETile[][] world, Room room) {
        Position pos = room.pos;
        int width = room.width;
        int height = room.height;

        Position botWallPos = pos;
        Position topWallPos = new Position(pos.x, pos.y + height - 1);
        Position leftWallPos = pos;
        Position rightWallPos = new Position(pos.x + width - 1, pos.y);

        addWallRowToWorld(world, width, botWallPos);
        addWallRowToWorld(world, width, topWallPos);
        addWallColToWorld(world, height, leftWallPos);
        addWallColToWorld(world, height, rightWallPos);

        for (int h = 1; h < height - 1; h += 1) {
            Position floorPos = new Position(pos.x + 1, pos.y + h);
            addFloorRowToWorld(world, width - 2, floorPos);
        }
    }

    // Add a row of wall tile to the world, from left to right.
    private static void addWallRowToWorld(TETile[][] world, int length, Position pos) {
        addTileRowToWorld(world, length, pos, Tileset.WALL);
    }

    // Add a column of wall tile to the world, from bottom to up.
    private static void addWallColToWorld(TETile[][] world, int length, Position pos) {
        addTileColToWorld(world, length, pos, Tileset.WALL);
    }

    // Add a row of floor tile to the world, from left to right.
    private static void addFloorRowToWorld(TETile[][] world, int length, Position pos) {
        addTileRowToWorld(world, length, pos, Tileset.FLOOR);
    }

    // Add a column of floor tile to the world, from bottom to up.
    private static void addFloorColToWorld(TETile[][] world, int length, Position pos) {
        addTileColToWorld(world, length, pos, Tileset.FLOOR);
    }

    // Add a row of tile, from left to right.
    private static void addTileRowToWorld(TETile[][] world, int length, Position pos, TETile tile) {
        for (int i = 0; i < length; i += 1) {
            world[pos.x + i][pos.y] = tile;
        }
    }

    // Add a column of specific tile with specific length to the world, from bottom to up.
    private static void addTileColToWorld(TETile[][] world, int length, Position pos, TETile tile) {
        for (int i = 0; i < length; i += 1) {
            world[pos.x][pos.y + i] = tile;
        }
    }

    //set world background as wall tiles
    private static void setWorldBackGroundAsWalls(TETile[][] world) {
        for (int i = 0; i < world.length; i += 1) {
            for (int j = 0; j < world[0].length; j += 1) {
                world[i][j] = Tileset.WALL;
            }
        }
    }

    //Replace redundant walls with nothing.
    //If a wall tile's all eight neighbours are walls, it is redundant
    private static void removeRedundantWalls(TETile[][] world) {
        //traversal all the tiles and create a new neighbours array
        //set the checked point as (0, 0)
        int[][] neighbours = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int i = 0; i < world.length; i += 1) {
            for (int j = 0; j < world[0].length; j += 1) {
                if (world[i][j].equals(Tileset.WALL)) {
                    int floorNum = 0;
                    for (int[] nbr : neighbours) {
                        //start from the first item
                        int adjX = i + nbr[0];
                        int adjY = j + nbr[1];
                        if (adjX >= 0 && adjX < world.length) {
                            if (adjY >= 0 && adjY < world[0].length) {
                                if (world[adjX][adjY].equals(Tileset.FLOOR)) {
                                    floorNum += 1;
                                }
                            }
                        }
                    }
                    //Check whether the current wall tile has floor neighbours
                    //If not, then it was redundant.
                    if (floorNum == 0) {
                        world[i][j] = Tileset.NOTHING;
                    }
                }
            }
        }
    }
}
