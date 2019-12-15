package pl.projectone.aoc;

import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public abstract class AdventDay15 {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 80;
    public static int runnerID = 1;
    private static String[][] paintMap = new String[HEIGHT][WIDTH];
    private static BlockingQueue<Integer> robot = new ArrayBlockingQueue<Integer>(10);
    private static BlockingQueue<Integer> comp = new ArrayBlockingQueue<Integer>(10);
    private static ArrayList<Long> initialCodes = new ArrayList<>();

    public static void calculateAoC15() {

        initialCodes = readDirections("./inputs/day15/input1.txt");
        for (int i = 0; i < 1000000; i++) {
            initialCodes.add((long) 0);
        }
        initMaps();


        Comp2 computer = new Comp2(initialCodes, comp, robot);
        MazeRunner robo = new MazeRunner(robot, comp, WIDTH/2, HEIGHT/2, paintMap);

        ExecutorService executor = Executors.newCachedThreadPool();
        CompletionService<Integer> service = new ExecutorCompletionService<>(executor);

        Future<Integer> roboFuture = service.submit(robo);
        Future<Integer> compFuture = service.submit(computer);


        try {
            compFuture = service.take();
            System.out.println(" Comp exit code " + compFuture.get());
            //roboFuture = service.take();
            //System.out.println(" Robot exit code - blocks " + roboFuture.get());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        finally {
            executor.shutdown();
        }

        printPaintMap();
        //floodMap(WIDTH/2, HEIGHT/2, 2, 0, runnerID);
        spreadMap(22, 52, 4, 0, runnerID);
        printPaintMap();
    }

    private static int floodMap(int originX, int originY, int direction, int steps, int id) {

        // direction 1 up, 2 down, 3 left, 4 right
        int possibleMoves = 0;
        Boolean[] dirs = {false, false, false, false};
        int dir = direction;
        int s = steps;
        int x = originX;
        int y = originY;
        int oldX;
        int oldY;
        boolean end = false;
        int myID = id;
        id++;

        ArrayList<Pair<Integer, Integer>> path = new ArrayList<>();

        while (!end) {

            if (paintMap[y-1][x].equals(".")) { // check up
                if (dir != 2) {
                    possibleMoves++;
                    dirs[0] = true;
                }
            }
            else if (paintMap[y-1][x].equals("O")) System.out.println("Found oxygen at: " + x + "," + (y-1) + "after: " + (s+1) + "----------------------");

            if (paintMap[y+1][x].equals(".")) { // check down
                if (dir != 1) {
                    possibleMoves++;
                    dirs[1] = true;
                }
            }
            else if (paintMap[y+1][x].equals("O")) System.out.println("Found oxygen at: " + x + "," + (y+1) + "after: " + (s+1) + "----------------------");

            if (paintMap[y][x-1].equals(".")) { // check left
                if (dir != 4) {
                    possibleMoves++;
                    dirs[2] = true;
                }
            }
            else if (paintMap[y][x-1].equals("O")) System.out.println("Found oxygen at: " + (x-1) + "," + y + "after: " + (s+1) + "----------------------");

            if (paintMap[y][x+1].equals(".")) { // check right
                if (dir != 3) {
                    possibleMoves++;
                    dirs[3] = true;
                }
            }
            else if (paintMap[y][x+1].equals("O")) System.out.println("Found oxygen at: " + (x+1) + "," + y + "after: " + (s+1) + "----------------------");


            if (possibleMoves == 0) {
                end = true;
                System.out.println("No moves possible, finished at: " + x + "," + y + " after " + s + " steps");
                break;
            }
            if (paintMap[y][x].equals("O")) {
                end = true;
                System.out.println("Found oxygen at: " + x + "," + y + " after " + s + " steps-----------------------------------");
                break;
            }

            int runner = 0;
            oldX = x;
            oldY = y;

            if (dirs[0]) { // one possible move up
                if (runner == 0) {
                    y -= 1; // move up
                    dir = 1; // current direction up
                    s += 1; // update steps
                    runner += 1;
                }
                else floodMap(oldX, oldY-1, 1, s, id);

            }
            if (dirs[1]) { // one possible move down
                if (runner == 0) {
                    y += 1; // move down
                    dir = 2; // current direction left
                    s += 1; // updte steps
                    runner += 1;
                }
                else floodMap(oldX, oldY+1, 2, s, id);
            }
            if (dirs[2]) { // one possible move left
                if (runner == 0) {
                    x -= 1; // move left
                    dir = 3; // current direction left
                    s += 1; // update steps
                    runner += 1;
                }
                else floodMap(oldX-1, oldY, 3, s, id);
            }
            if (dirs[3]) { // one possible moves right
                if (runner == 0) {
                    x += 1; // move right
                    dir = 4; // current direction right
                    s += 1; // update steps
                    runner += 1;
                }
                else floodMap(oldX+1, oldY, 4, s, id);
            }

            dirs[0] = false;
            dirs[1] = false;
            dirs[2] = false;
            dirs[3] = false;
            possibleMoves = 0;
            runner = 1;
        }
        return 0;
    }

    private static int spreadMap(int originX, int originY, int direction, int steps, int id) {
        // direction 1 up, 2 down, 3 left, 4 right
        int possibleMoves = 0;
        Boolean[] dirs = {false, false, false, false};
        int dir = direction;
        int s = steps;
        int x = originX;
        int y = originY;
        int oldX;
        int oldY;
        boolean end = false;
        int myID = id;
        id++;

        while (!end) {

            paintMap[y][x] = "o";

            if (paintMap[y-1][x].equals(".")) { // check up
                if (dir != 2) {
                    possibleMoves++;
                    dirs[0] = true;
                }
            }
            if (paintMap[y+1][x].equals(".")) { // check down
                if (dir != 1) {
                    possibleMoves++;
                    dirs[1] = true;
                }
            }
            if (paintMap[y][x-1].equals(".")) { // check left
                if (dir != 4) {
                    possibleMoves++;
                    dirs[2] = true;
                }
            }
            if (paintMap[y][x+1].equals(".")) { // check right
                if (dir != 3) {
                    possibleMoves++;
                    dirs[3] = true;
                }
            }

            if (possibleMoves == 0) {
                end = true;
                System.out.println("Bot " + myID + " - no moves possible, finished at: " + x + "," + y + " after " + s + " steps");
                break;
            }

            int runner = 0;
            oldX = x;
            oldY = y;

            if (dirs[0]) { // one possible move up
                if (runner == 0) {
                    y -= 1; // move up
                    dir = 1; // current direction up
                    s += 1; // update steps
                    runner += 1;
                }
                else spreadMap(oldX, oldY-1, 1, s, id);

            }
            if (dirs[1]) { // one possible move down
                if (runner == 0) {
                    y += 1; // move down
                    dir = 2; // current direction left
                    s += 1; // updte steps
                    runner += 1;
                }
                else spreadMap(oldX, oldY+1, 2, s, id);
            }
            if (dirs[2]) { // one possible move left
                if (runner == 0) {
                    x -= 1; // move left
                    dir = 3; // current direction left
                    s += 1; // update steps
                    runner += 1;
                }
                else spreadMap(oldX-1, oldY, 3, s, id);
            }
            if (dirs[3]) { // one possible moves right
                if (runner == 0) {
                    x += 1; // move right
                    dir = 4; // current direction right
                    s += 1; // update steps
                    runner += 1;
                }
                else spreadMap(oldX+1, oldY, 4, s, id);
            }

            dirs[0] = false;
            dirs[1] = false;
            dirs[2] = false;
            dirs[3] = false;
            possibleMoves = 0;
            runner = 1;
        }
        return 0;
    }

    private static ArrayList<Long> readDirections(String file) {

        ArrayList<Long> result = new ArrayList<>();
        try (Scanner input = new Scanner(new FileInputStream(file), StandardCharsets.UTF_8)) {
            String line = input.nextLine();
            String[] tempcodes = line.split(",");
            for (String s : tempcodes) {
                result.add(Long.parseLong(s));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }
        return result;
    }

    public static void printPaintMap() {

        for (int h = 0; h < HEIGHT; h++) {
            for (int w = 0; w < WIDTH; w++ ) {
                System.out.print(paintMap[h][w]);
            }
            System.out.println();
        }
    }

    private static void initMaps() {
        for (int w = 0; w < WIDTH; w++) {
            for (int h = 0; h < HEIGHT; h++ ) {
                paintMap[h][w] = " ";
            }
        }
    }

}
