package pl.projectone.aoc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

public class MRunner {

    ArrayList<String> keys = new ArrayList<>(); // order of keys and opened doors
    HashMap<String, Integer> k = new HashMap<>();
    TreeMap<Integer, String> tm = new TreeMap<>();
    ArrayList<String> doors = new ArrayList<>();
    int steps = 0; // steps taken
    int currX;
    int currY;
    int dir;
    int local;
    int initial = 0;
    String[][] map;
    String name;
    private static HashMap<String, Point> coors;
    ArrayList<Integer> results;
    int mode;

    public MRunner(String[][] map, int steps, ArrayList<String> keys, int currX, int currY, int dir, int local, HashMap<String, Point> coors, ArrayList<Integer> r, int mode) {
        this.mode = mode;
        results = r;
        this.coors = coors;
        this.local = local;
        this.map = map;
        this.keys.addAll(keys);
        this.steps = steps;
        this.initial = steps;
        this.currX = currX;
        this.currY = currY;
        name = map[currY][currX];
        this.dir = dir; // 1 up, 2 down, 3 right, 4 left

    }

    public TreeMap<Integer, String> runner() {

        //System.out.println("Starting at: " + currX + "," + currY + " name " + map[currY][currX] + " keys " + keys.size());

        Boolean[] dirs = {false, false, false, false};
        boolean end = false;
        int moves = 0;
        int oldX;
        int oldY;
        boolean found = false;

        while(!end) {

            //System.out.println("Currently at " + currX + "," + currY);
            if (Character.isLowerCase(map[currY][currX].charAt(0))) {
                if (keys.size() == coors.size()) {
                    System.out.println("Found everything after " + steps + " steps" + keys.toString() +" k " + keys.size() + " c " + coors.size());
                    results.add(steps);
                    found = true;
                    end = true;
                    break;
                }
                if ((local > 0) && !keys.contains(map[currY][currX])) {
                    //k.put(map[currY][currX], local); // put found steps and local steps;
                    tm.put(local, map[currY][currX]);
                }
            }


            if (!map[currY-1][currX].equals("#")) {
                if (dir != 2) {
                    if (map[currY-1][currX].equals(".") || Character.isLowerCase(map[currY-1][currX].charAt(0)) || map[currY-1][currX].equals("@")) {
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
                    if (map[currY+1][currX].equals(".") || Character.isLowerCase(map[currY+1][currX].charAt(0)) || map[currY+1][currX].equals("@")) {
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
                if (dir != 4) {
                    //System.out.println("Checking " + map[currY][currX-1] + " keys " + keys.toString());
                    if (map[currY][currX-1].equals(".") || Character.isLowerCase(map[currY][currX-1].charAt(0)) || map[currY][currX-1].equals("@")) {
                        moves++;
                        dirs[2] = true;
                    }
                    else if (keys.contains(map[currY][currX-1].toLowerCase())) {
                        moves++;
                        dirs[2] = true;
                    }
                }
            }

            if (!map[currY][currX+1].equals("#")) {
                if (dir != 3) {
                    if (map[currY][currX+1].equals(".") || Character.isLowerCase(map[currY][currX+1].charAt(0)) || map[currY][currX+1].equals("@")) {
                        moves++;
                        dirs[3] = true;
                    }
                    else if (keys.contains(map[currY][currX+1].toLowerCase())) {
                        moves++;
                        dirs[3] = true;
                    }
                }
            }

            if (moves == 0) {
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
                    steps++; // update steps
                    local++;
                    runner++;
                }
                else {
                    MRunner r = new MRunner(map, steps + 1, keys, oldX, oldY-1, 1, local, coors, results, 1);
                    //k.putAll(r.runner());
                    tm.putAll(r.runner());
                }
            }
            if (dirs[1]) { // one possible move down
                if (runner == 0) {
                    currY += 1; // move down
                    dir = 2; // current direction down
                    steps++; // update steps
                    local++;
                    runner++;
                }
                else {
                    MRunner r = new MRunner(map, steps + 1, keys, oldX, oldY+1, 2, local, coors, results, 1);
                    //k.putAll(r.runner());
                    tm.putAll(r.runner());
                }
            }
            if (dirs[2]) { // one possible move left
                if (runner == 0) {
                    currX -= 1; // move left
                    dir = 3; // current direction left
                    steps++; // update steps
                    local++;
                    runner++;
                }
                else {
                    MRunner r = new MRunner(map, steps + 1, keys, oldX-1, oldY, 3, local, coors, results, 1);
                    //k.putAll(r.runner());
                    tm.putAll(r.runner());
                }
            }
            if (dirs[3]) { // one possible move right
                if (runner == 0) {
                    currX += 1; // move right
                    dir = 4; // current direction right
                    steps++; // update steps
                    local++;
                    runner++;
                }
                else {
                    MRunner r = new MRunner(map, steps + 1, keys, oldX+1, oldY, 4, local, coors, results, 1);
                    //k.putAll(r.runner());
                    tm.putAll(r.runner());
                }
            }

            dirs[0] = false;
            dirs[1] = false;
            dirs[2] = false;
            dirs[3] = false;
            moves = 0;
        }

        //System.out.println("Finalized search from key " + name);


        /**
        if (!found && (k.size() > 0) && (mode == 0)) {
            String a = k.entrySet().stream().min((v1, v2) -> Integer.compare(v1.getValue(), v2.getValue())).get().getKey();
            int v = k.entrySet().stream().min((v1, v2) -> Integer.compare(v1.getValue(), v2.getValue())).get().getValue();
            System.out.println("Jumping to " + a + " with value " + v + " from " + name);
            ArrayList<String> temp = new ArrayList<>();
            temp.addAll(keys);
            temp.add(a);
            //System.out.println("Starting search from " + k + " with keys " + keys.toString() + " final keys " + temp.toString());
            MRunner r = new MRunner(map, v + initial, temp, coors.get(a).x, coors.get(a).y, 0, 0, coors, results, 0);
            r.runner();
        }
         */


        /**
        if (!found && (mode == 0)) {
            k.forEach((k, v) -> {

                if (results.size() > 0) {
                    Collections.sort(results);
                    if (v + initial < results.get(0)) {
                        ArrayList<String> temp = new ArrayList<>();
                        temp.addAll(keys);
                        temp.add(k);
                        //System.out.println("Jumping to " + k + " with value " + v + " from " + name);
                        //System.out.println("Starting search from " + k + " with keys " + keys.toString() + " final keys " + temp.toString());
                        MRunner r = new MRunner(map, v + initial, temp, coors.get(k).x, coors.get(k).y, 0, 0, coors, results, 0);
                        r.runner();
                    }
                }
                else {
                    ArrayList<String> temp = new ArrayList<>();
                    temp.addAll(keys);
                    temp.add(k);
                    //System.out.println("Jumping to " + k + " with value " + v + " from " + name);
                    //System.out.println("Starting search from " + k + " with keys " + keys.toString() + " final keys " + temp.toString());
                    MRunner r = new MRunner(map, v + initial, temp, coors.get(k).x, coors.get(k).y, 0, 0, coors, results, 0);
                    r.runner();
                }

            });
        }
        */

        /**
        if (!found && (mode == 0)) {

            tm.forEach((v, k) -> {

                if (results.size() > 0) {
                    Collections.sort(results);
                    if (v + initial < results.get(0)) {
                        ArrayList<String> temp = new ArrayList<>();
                        temp.addAll(keys);
                        temp.add(k);
                        //System.out.println("Jumping to " + k + " with value " + v + " from " + name);
                        //System.out.println("Starting search from " + k + " with keys " + keys.toString() + " final keys " + temp.toString());
                        MRunner r = new MRunner(map, v + initial, temp, coors.get(k).x, coors.get(k).y, 0, 0, coors, results, 0);
                        r.runner();
                    }
                }
                else {
                    ArrayList<String> temp = new ArrayList<>();
                    temp.addAll(keys);
                    temp.add(k);
                    //System.out.println("Jumping to " + k + " with value " + v + " from " + name);
                    //System.out.println("Starting search from " + k + " with keys " + keys.toString() + " final keys " + temp.toString());
                    MRunner r = new MRunner(map, v + initial, temp, coors.get(k).x, coors.get(k).y, 0, 0, coors, results, 0);
                    r.runner();
                }

            });
        }
         */


        return tm;
    }
}
