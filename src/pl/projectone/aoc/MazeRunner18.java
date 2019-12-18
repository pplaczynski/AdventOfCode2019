package pl.projectone.aoc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;

public class MazeRunner18 implements Runnable {

    String[][] map; // my own copy of map
    ArrayList<String> order = new ArrayList<>(); // order of keys and opened doors
    HashSet<String> keys = new HashSet<>();
    int steps; // steps taken
    int currX;
    int currY;
    int all;
    int dir;
    int xSize;
    int ySize;
    ExecutorService pool;

    public MazeRunner18(String[][] m, int mX, int mY, int steps,
                        ArrayList<String> o, int currX, int currY,
                        int all, int dir, HashSet<String> k, ExecutorService p) {

        map = new String[mY][mX];
        for (int y = 0; y < mY; y++) {
            for (int x = 0; x < mX; x++) {
                map[y][x] = m[y][x];
            }
        }
        for (int i = 0; i < o.size(); i++) order.add(o.get(i));
        k.forEach(v -> keys.add(v));

        this.pool = p;
        this.steps = steps;
        this.currX = currX;
        this.currY = currY;
        this.all = all;
        this.dir = dir; // 1 up, 2 down, 3 right, 4 left
        xSize = mX;
        ySize = mY;
    }

    @Override
    public void run() {

        Boolean[] dirs = {false, false, false, false};
        boolean end = false;
        int moves = 0;
        int oldX;
        int oldY;

        while(!end) {

            if ((!map[currY][currX].equals("#")) && (!map[currY][currX].equals("."))) {
                order.add(map[currY][currX]);
                if (order.size() == all) {
                    System.out.println(Thread.currentThread().getId() + "Found everything after - " + steps + " steps ---------------- !!!!!!!!");
                    //System.out.println(order.toString());
                    end = true;
                    break;
                }
                if (Character.isLowerCase(map[currY][currX].charAt(0))) keys.add(map[currY][currX]);
                map[currY][currX] = ".";
                dir = 0; // after taking the key you can backtrack;
            }

            if (!map[currY-1][currX].equals("#")) {
                if (dir != 2) {
                    if (map[currY-1][currX].equals(".") || Character.isLowerCase(map[currY-1][currX].charAt(0))) {
                        moves++;
                        dirs[0] = true;
                    }
                    else if (keys.contains(map[currY-1][currX].toLowerCase())) {
                        moves++;
                        dirs[0] = true;
                    }
                }
            }

            if (!map[currY+1][currX].equals("#")) {
                if (dir != 1) {
                    if (map[currY+1][currX].equals(".") || Character.isLowerCase(map[currY+1][currX].charAt(0))) {
                        moves++;
                        dirs[1] = true;
                    }
                    else if (keys.contains(map[currY+1][currX].toLowerCase())) {
                        moves++;
                        dirs[1] = true;
                    }
                }
            }

            if (!map[currY][currX-1].equals("#")) {
                //System.out.println(Thread.currentThread().getId() + "  " + currX + "," + currY + "checking left - " + map[currY][currX-1]);
                if (dir != 4) {
                    if (map[currY][currX-1].equals(".") || Character.isLowerCase(map[currY][currX-1].charAt(0))) {
                        moves++;
                        dirs[2] = true;
                    }
                    else if (keys.contains(map[currY][currX-1].toLowerCase())) {
                        //System.out.println(Thread.currentThread().getId() + "trying to open: " + map[currY][currX-1]);
                        moves++;
                        dirs[2] = true;
                    }
                    //else System.out.println(Thread.currentThread().getId() + " no path");
                }
            }

            if (!map[currY][currX+1].equals("#")) {
                //System.out.println(Thread.currentThread().getId() + "  " + currX + "," + currY + "checking right - " + map[currY][currX+1]);
                if (dir != 3) {
                    if (map[currY][currX+1].equals(".") || Character.isLowerCase(map[currY][currX+1].charAt(0))) {
                        moves++;
                        dirs[3] = true;
                    }
                    else if (keys.contains(map[currY][currX+1].toLowerCase())) {
                        //System.out.println(Thread.currentThread().getId() + "trying to open: " + map[currY][currX+1]);
                        moves++;
                        dirs[3] = true;
                    }
                    //else System.out.println(Thread.currentThread().getId() + " no path");
                }
            }

            if (moves == 0) {
                //System.out.println(Thread.currentThread().getId() + "Finished after - " + steps + " found: " + order.size() + " moves " + moves + " keys " + keys.toString());
                //System.out.println(order.toString());
                end = true;
                break;
            }

            int runner = 0;
            oldX = currX;
            oldY = currY;

            if (dirs[0]) { // one possible move up
                if (runner == 0) {
                    currY -= 1; // move up
                    dir = 1; // current direction up
                    steps += 1; // update steps
                    runner += 1;
                }
                else {
                    pool.execute(new MazeRunner18(map, xSize, ySize, steps, order, oldX, oldY-1, all, 1, keys, pool));
                    //new Thread(new MazeRunner18(map, xSize, ySize, steps, order, oldX, oldY-1, all, 1, keys)).start();
                }
            }
            if (dirs[1]) { // one possible move down
                if (runner == 0) {
                    currY += 1; // move down
                    dir = 2; // current direction down
                    steps += 1; // update steps
                    runner += 1;
                }
                else {
                    pool.execute(new MazeRunner18(map, xSize, ySize, steps, order, oldX, oldY+1, all, 2, keys, pool));
                    //new Thread(new MazeRunner18(map, xSize, ySize, steps, order, oldX, oldY+1, all, 2, keys)).start();
                }
            }
            if (dirs[2]) { // one possible move left
                if (runner == 0) {
                    currX -= 1; // move left
                    dir = 3; // current direction left
                    steps += 1; // update steps
                    runner += 1;
                }
                else {
                    pool.execute(new MazeRunner18(map, xSize, ySize, steps, order, oldX-1, oldY, all, 3, keys, pool));
                    //new Thread(new MazeRunner18(map, xSize, ySize, steps, order, oldX-1, oldY, all, 3, keys)).start();
                }
            }
            if (dirs[3]) { // one possible move right
                if (runner == 0) {
                    currX += 1; // move right
                    dir = 4; // current direction right
                    steps += 1; // update steps
                    runner += 1;
                }
                else {
                    pool.execute(new MazeRunner18(map, xSize, ySize, steps, order, oldX+1, oldY, all, 4, keys, pool));
                    //new Thread(new MazeRunner18(map, xSize, ySize, steps, order, oldX+1, oldY, all, 4, keys)).start();
                }
            }

            dirs[0] = false;
            dirs[1] = false;
            dirs[2] = false;
            dirs[3] = false;
            moves = 0;
        }
    }
}
