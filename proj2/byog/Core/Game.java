package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Random;
import java.awt.Font;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;
    private static final int TILE_SIZE = 16;

    private TETile[][] world = new TETile[WIDTH][HEIGHT];
    private StringBuilder record = new StringBuilder();
    private Position playerPos = new Position(0, 0);
    int HEARTNUM = 5;
    boolean gameOver = false;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void playWithKeyboard() {
        InputSource source = new KeyBoardInputSource();
        ter.initialize(WIDTH, HEIGHT);
        drawStartMenu();
        while (!gameOver) {
            if (record.length() > 0) { //Make mouse-info displayed be real-time
                ter.renderFrame(world);
                heartInfo();
                tileInfo(new Position((int) StdDraw.mouseX(), (int) StdDraw.mouseY()));
            }
            if (StdDraw.hasNextKeyTyped()) {
                char action = source.getNextKey();
                takeAction(source, action);
            }
        }
        drawEndMenu();
    }

    // Take the action based on input source type.
    private void takeAction(InputSource source, char action) {
        record.append(action);
        // Create a new world.
        if (action == 'N') {
            long seed = inputSeed(source);
            record.append(seed);
            record.append('S'); //Set the stop sign of the seed
            Random random = new Random(seed);
            playerPos = WorldGenerator.createWorld(world, random);
            // If interacts with string, do not render.
            if (source.getClass().equals(KeyBoardInputSource.class)) {
                ter.renderFrame(world);
            }
        } else if (action == ':') { // Save and Quit.
            char nextAction = source.getNextKey();
            if (nextAction == 'Q') {
                record.deleteCharAt(record.length() - 1); // Remove ':' from record.
                save(record.toString()); // Save current world state to the file.
                StdDraw.clear(StdDraw.BLACK);
                Font font = new Font("Monaco", Font.BOLD, 60);
                StdDraw.setFont(font);
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.text(WIDTH / 2, HEIGHT / 2, "World Save Successful");
                StdDraw.show();
                StdDraw.pause(2000);
                if (source.getClass().equals(KeyBoardInputSource.class)) {
                    System.exit(0);
                }
            }
        } else if (action == 'L') { // Load saved world.
            record.deleteCharAt(record.length() - 1); // Remove 'L' from the record.
            String savedRecord = load();
            if (savedRecord.equals("")) {
                StdDraw.clear(StdDraw.BLACK);
                Font font = new Font("Monaco", Font.BOLD, 60);
                StdDraw.setFont(font);
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.text(WIDTH / 2, HEIGHT * 2 / 3, "There is No File to Load");
                StdDraw.show();
                StdDraw.pause(2000);
                drawStartMenu();
            } else {
                world = playWithInputString(savedRecord); // Load saved world.
                if (source.getClass().equals(KeyBoardInputSource.class)) {
                    ter.renderFrame(world);
                }
            }
        } else if (action == 'W') { // move avatar upward if there is no wall
            int x = playerPos.x;
            int y = playerPos.y + 1;
            if (world[x][y].equals(Tileset.FLOOR)) {
                world[x][y] = Tileset.PLAYER; //Update world
                world[playerPos.x][playerPos.y] = Tileset.FLOOR;
                playerPos = new Position(x, y); //Update player
                if (source.getClass().equals(KeyBoardInputSource.class)) {
                    ter.renderFrame(world);
                }
            } else {
                if (HEARTNUM == 0) {
                    gameOver = true;
                } else {
                    HEARTNUM -= 1;
                }
            }
        } else if (action == 'S') { // move avatar downward if there is no wall
            int x = playerPos.x;
            int y = playerPos.y - 1;
            if (world[x][y].equals(Tileset.FLOOR)) {
                world[x][y] = Tileset.PLAYER; //Update world
                world[playerPos.x][playerPos.y] = Tileset.FLOOR;
                playerPos = new Position(x, y); //Update player
                if (source.getClass().equals(KeyBoardInputSource.class)) {
                    ter.renderFrame(world);
                }
            } else {
                if (HEARTNUM == 0) {
                    gameOver = true;
                } else {
                    HEARTNUM -= 1;
                }
            }
        } else if (action == 'A') { // move avatar upward if there is no wall
            int x = playerPos.x - 1;
            int y = playerPos.y;
            if (world[x][y].equals(Tileset.FLOOR)) {
                world[x][y] = Tileset.PLAYER; //Update world
                world[playerPos.x][playerPos.y] = Tileset.FLOOR;
                playerPos = new Position(x, y); //Update player
                if (source.getClass().equals(KeyBoardInputSource.class)) {
                    ter.renderFrame(world);
                }
            } else {
                if (HEARTNUM == 0) {
                    gameOver = true;
                } else {
                    HEARTNUM -= 1;
                }
            }
        } else if (action == 'D') { // move avatar upward if there is no wall
            int x = playerPos.x + 1;
            int y = playerPos.y;
            if (world[x][y].equals(Tileset.FLOOR)) {
                world[x][y] = Tileset.PLAYER; //Update world
                world[playerPos.x][playerPos.y] = Tileset.FLOOR;
                playerPos = new Position(x, y); //Update player
                if (source.getClass().equals(KeyBoardInputSource.class)) {
                    ter.renderFrame(world);
                }
            } else {
                if (HEARTNUM == 0) {
                    gameOver = true;
                } else {
                    HEARTNUM -= 1;
                }
            }
        }
    }

    //Save data to a file
    private static void save(String record) {
        File f = new File("./save_data.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(record);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    //Load data from a file
    private static String load() {
        File f = new File("./save_data.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (String) os.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        //In case no file has been saved yet, we return an empty string.
        return "";
    }


    //Draw the end Menu if the LIFENUM goes to 0
    private void drawEndMenu() {
        StdDraw.clear(StdDraw.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 60);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT * 2 / 3, "GAME OVER");
        font = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "You have no heart left");
        StdDraw.show();
    }

    // Draw the start menu when player interacts with keyboard.
    private void drawStartMenu() {
        StdDraw.clear(StdDraw.BLACK);
        // Draw title.
        Font font = new Font("Monaco", Font.BOLD, 60);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT * 3 / 4, "CS61B: The Game");
        // Draw menu options.
        font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT * 6 / 10, "New World (N)");
        StdDraw.text(WIDTH / 2, HEIGHT * 5 / 10, "Load World (L)");
        StdDraw.text(WIDTH / 2, HEIGHT * 4 / 10, "Quit (Q)");
        //Draw game instructions
        font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT * 1 / 4, "Hit the wall will lose your heart");
        // Reset font size to TeRenderer's default size.
        font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);
        StdDraw.show();
    }

    //Display the description of the pointed by mouse
    private void tileInfo(Position mousePos) {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textLeft(1, HEIGHT - 1, world[mousePos.x][mousePos.y].description());
        StdDraw.show();
    }

    //Display the left num of Heart
    private void heartInfo() {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textRight(WIDTH, HEIGHT - 1, "Heart " + HEARTNUM);
        StdDraw.show();
    }

    // Help to generate the seed of the world.
    private long inputSeed(InputSource source) {
        // Display the typing interface.
        if (source.getClass().equals(KeyBoardInputSource.class)) {
            drawSeed("");
        }
        // Display seed typed in.
        StringBuilder seedRecord = new StringBuilder();
        long seed = 0L;
        while (source.possibleNextInput()) {
            char next = source.getNextKey();
            if (next != 'S') {
                seed = seed * 10 + Character.getNumericValue(next);
                seedRecord.append(next);
                if (source.getClass().equals(KeyBoardInputSource.class)) {
                    drawSeed(seedRecord.toString());
                    StdDraw.show();
                }
            } else {
                break;
            }
        }
        return seed;
    }

    // Display the seed typed by the player when interact with keyboard.
    private void drawSeed(String s) {
        StdDraw.clear(StdDraw.BLACK);
        // Draw instruction.
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT * 6 / 10, "Please type a seed, press 'S' to confirm");
        // Display seed typed.
        StdDraw.text(WIDTH / 2, HEIGHT * 5 / 10, s);
        // Reset font size to TeRenderer's default size.
        font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);
        StdDraw.show();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // DONE: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        InputSource source = new StringInputSource(input);
        while (source.possibleNextInput()) {
            char action = source.getNextKey();
            takeAction(source, action);
        }
        TETile[][] finalWorldFrame = world;
        return finalWorldFrame;
    }
}
