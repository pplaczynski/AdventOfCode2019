package pl.projectone.aoc;

import javafx.util.Pair;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.*;

public abstract class AdventDay18 {

    private static int ySize = 0;
    private static int xSize = 0;
    private static int yStart = 0;
    private static int xStart = 0;
    private static String[][] map;
    private static HashMap<String, Point> keysDoors = new HashMap<>();
    private static ArrayList<No> nodes = new ArrayList<>();
    private static ArrayList<Integer> results = new ArrayList<>();
    private static HashMap<String, ArrayList<Connection18>> nod = new HashMap<>();
    private static ArrayList<String> myKeys = new ArrayList<>();

    public static void calculateAoC18() {

        readDirections("./inputs/day18/input1.txt");
        printMap();
        System.out.println("all keys: " + keysDoors.size());

        MRunner r = new MRunner(map, 0, new ArrayList<>(), xStart, yStart, 0, 0, keysDoors, results, 0, new ArrayList<String>());
        nod.put("@", r.runner());

        keysDoors.forEach((k, v) -> {
            MRunner ru = new MRunner(map, 0, new ArrayList<>(), v.x, v.y, 0, 0, keysDoors, results, 0, new ArrayList<String>());
            nod.put(k, ru.runner());
        });

        nod.forEach((k, v) -> {
            System.out.println("Key " + k + " connections " + v.size());

        });

        results.add(10000000);
        calcDijk("@", new ArrayList<String>(), 0);
        Collections.sort(results);
        System.out.println("Best result " + results.get(0));



    }

    private static void calcDijk(String key, ArrayList<String> keys, int dist) {

        int d = dist;
        ArrayList<String> locK = new ArrayList<>();
        locK.addAll(keys);
        ArrayList<Connection18> cons = nod.get(key);
        if (locK.size() == nod.size() - 1) {
            System.out.println("Found all in " + d + " steps!");
            results.add(d);
        }

        Collections.sort(results);
        cons.sort((c1, c2) -> c1.distance - c2.distance);

        for (int i = 0; i < cons.size(); i++) {
            Connection18 c = cons.get(i);
            if (!locK.contains(c.name)) {

                if (results.get(0) > d + c.distance) {
                    boolean pass = true;
                    for (int l = 0; l < c.doors.size(); l++) {
                        if (!locK.contains(c.doors.get(l).toLowerCase())) {
                            pass = false;
                            break;
                        }
                    }
                    if (pass) {
                        //System.out.println("Calculating for " + c.name + " keys gathered " + locK.toString());
                        ArrayList<String> temp = new ArrayList<>();
                        temp.addAll(locK);
                        temp.add(c.name);
                        calcDijk(c.name, temp, d + c.distance);
                    }
                }
            }
        }
    }

    private static class No {

        int id;
        int x;
        int y;
        String name;
        No left;
        No right;
        No up;
        No down;

        public No(String name, int x, int y, int id) {
            this.x = x;
            this.y = y;
            this.name = name;
            this.id = id;
        }
    }

    private static void limitNodes() {

        int num = 0;

        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).name.equals("%")) {
                if (nodes.get(i).left != null) num++;
                if (nodes.get(i).right != null) num++;
                if (nodes.get(i).up != null) num++;
                if (nodes.get(i).down != null) num++;
                //System.out.println("Node: " + nodes.get(i).x + "," + nodes.get(i).y + " connected: " + num);

                if (num == 1) {
                    //System.out.println("Found node to optimize: " + nodes.get(i).x + "," + nodes.get(i).y);
                    if (nodes.get(i).left != null) {
                        nodes.get(i).left.right = null;
                        map[nodes.get(i).y][nodes.get(i).x] = ".";
                        nodes.remove(i);
                    }
                    if (nodes.get(i).right != null) {
                        nodes.get(i).right.left = null;
                        map[nodes.get(i).y][nodes.get(i).x] = ".";
                        nodes.remove(i);
                    }
                    if (nodes.get(i).up != null) {
                        nodes.get(i).up.down = null;
                        map[nodes.get(i).y][nodes.get(i).x] = ".";
                        nodes.remove(i);
                    }
                    if (nodes.get(i).down != null) {
                        nodes.get(i).down.up = null;
                        map[nodes.get(i).y][nodes.get(i).x] = ".";
                        nodes.remove(i);
                    }
                }
            }
            num = 0;
        }
    }

    private static void connect() {

        for (int i = 0; i < nodes.size(); i++) {

            int x = nodes.get(i).x;
            int y = nodes.get(i).y;

            Optional<No> nod = nodes.stream().filter(n -> n.y == y).filter(n -> n.x > x).min((n1, n2) -> n1.x - n2.x);
            nodes.get(i).right = nod.orElse(null);
            if (nodes.get(i).right != null) {
                for (int z = x + 1; z < nodes.get(i).right.x; z++) {
                    if (map[y][z].equals("#")) {
                        nodes.get(i).right = null;
                        break;
                    }
                }
            }

            nod = nodes.stream().filter(n -> n.y == y).filter(n -> n.x < x).max((n1, n2) -> n1.x - n2.x);
            nodes.get(i).left = nod.orElse(null);
            if (nodes.get(i).left != null) {
                for (int z = x - 1; z > nodes.get(i).left.x; z--) {
                    if (map[y][z].equals("#")) {
                        nodes.get(i).left = null;
                        break;
                    }
                }
            }

            nod = nodes.stream().filter(n -> n.x == x).filter(n -> n.y < y).max((n1, n2) -> n1.y - n2.y);
            nodes.get(i).up = nod.orElse(null);
            if (nodes.get(i).up != null) {
                for (int z = y - 1; z > nodes.get(i).up.y; z--) {
                    if (map[z][x].equals("#")) {
                        nodes.get(i).up = null;
                        break;
                    }
                }
            }

            nod = nodes.stream().filter(n -> n.x == x).filter(n -> n.y > y).min((n1, n2) -> n1.y - n2.y);
            nodes.get(i).down = nod.orElse(null);
            if (nodes.get(i).down != null) {
                for (int z = y + 1; z < nodes.get(i).down.y; z++) {
                    if (map[z][x].equals("#")) {
                        nodes.get(i).down = null;
                        break;
                    }
                }
            }
        }
    }

    private static void findNodes() {

        int id = 0;
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {

                if (map[y][x].equals("#")) continue;
                else if (!map[y][x].equals(".")) {
                    nodes.add(new No(map[y][x], x, y, id));
                    id++;
                }
                else {

                    boolean up = false;
                    boolean down = false;
                    boolean right = false;
                    boolean left = false;

                    if (!map[y-1][x].equals("#")) up = true;
                    if (!map[y+1][x].equals("#")) down = true;
                    if (!map[y][x+1].equals("#")) right = true;
                    if (!map[y][x-1].equals("#")) left = true;

                    if ((up && right) || (up && left) || (down && right) || (down && left) ||
                            (up && down && right) || (up && down && left) || (left && right && up) || (left && right && down)) {
                        nodes.add(new No("%", x, y, id));
                        id++;
                    }
                }
            }
        }

        for (int i = 0; i < nodes.size(); i++) {
            map[nodes.get(i).y][nodes.get(i).x] = nodes.get(i).name;
        }
        System.out.println("Number of nodes before optimization: " + nodes.size());
    }

    private static void printMap() {
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                System.out.print(map[y][x]);
            }
            System.out.println();
        }
    }

    private static String[][] readDirections(String file) {

        ArrayList<String> lines = new ArrayList<>();
        try (Scanner input = new Scanner(new FileInputStream(file), StandardCharsets.UTF_8)) {
            while (input.hasNextLine()) {
                lines.add(input.nextLine());
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }

        ySize = lines.size();
        xSize = lines.get(0).length();
        map = new String[ySize][xSize];
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                map[y][x] = lines.get(y).substring(x, x+1);
                if(Character.isLowerCase(map[y][x].charAt(0))) keysDoors.put(map[y][x], new Point(x, y));
                if (map[y][x].equals("@")) {
                    xStart = x;
                    yStart = y;
                }
                //if ((!map[y][x].equals("#")) && (!map[y][x].equals("."))) {
                //    keysDoors.put(map[y][x], new Point(x, y));
                //}
            }
        }
        return map;
    }


}
