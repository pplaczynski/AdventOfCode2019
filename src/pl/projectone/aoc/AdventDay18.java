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

    public static void calculateAoC18() {

        readDirections("./inputs/day18/input1.txt");
        printMap();
        System.out.println("all keys: " + keysDoors.size());

        MRunner r = new MRunner(map, 0, new ArrayList<>(), xStart, yStart, 0, 0, keysDoors, results, 0);
        TreeMap<Integer, String> nmap = r.runner();
        Collections.sort(results);
        System.out.println("Results: " + results.get(0));
        nmap.forEach((k, v) -> System.out.println(v + " steps away " + k));

        /**
        keysDoors.forEach((k, v) -> System.out.println(k + " at [" + v.x + "," + v.y + "]"));

        keysDoors.forEach((k, v) -> {
            if (Character.isLowerCase(k.charAt(0))) System.out.println(k + " is key");
        });

        ExecutorService pool = Executors.newFixedThreadPool(5);

        MazeRunner18 runner = new MazeRunner18(map, xSize, ySize, 0, new ArrayList<>(), keysDoors.get("@").x, keysDoors.get("@").y, keysDoors.size(), 0, new HashSet<String>(), pool);
        new Thread(runner).start();
        pool.execute(runner);
        MRunner r = new MRunner(map, xSize, ySize, 0, new ArrayList<>(), keysDoors.get("@").x, keysDoors.get("@").y, keysDoors.size(), 0, new HashSet<String>(), 1000000);
        //r.runner();
        System.out.println("------------------------------------");
        findNodes();
        printMap();

        connect();

        No n = nodes.get(1);
        System.out.println("Main Node: " + n.x + "," + n.y);
        if (n.left != null) System.out.println("Left Node: " + n.left.x + "," + n.left.y);
        if (n.right != null) System.out.println("Right Node: " + n.right.x + "," + n.right.y);
        if (n.up != null) System.out.println("Up Node: " + n.up.x + "," + n.up.y);
        if (n.down != null) System.out.println("Down Node: " + n.down.x + "," + n.down.y);

        boolean optimized = false;
        int initial = nodes.size();

        while(!optimized) {
            System.out.println(" Nodes size: " + nodes.size());
            limitNodes();
            if (nodes.size() < initial) initial = nodes.size();
            else optimized = true;
        }

        System.out.println(" Nodes size: " + nodes.size());
        limitNodes();
        System.out.println(" Nodes size: " + nodes.size());
        limitNodes();
        System.out.println(" Nodes size: " + nodes.size());
        limitNodes();
        for (int i = 0; i < nodes.size(); i++) {
            map[nodes.get(i).y][nodes.get(i).x] = nodes.get(i).name;
        }
        printMap();

        n = nodes.get(1);
        System.out.println("Main Node: " + n.x + "," + n.y);
        if (n.left != null) System.out.println("Left Node: " + n.left.x + "," + n.left.y);
        if (n.right != null) System.out.println("Right Node: " + n.right.x + "," + n.right.y);
        if (n.up != null) System.out.println("Up Node: " + n.up.x + "," + n.up.y);
        if (n.down != null) System.out.println("Down Node: " + n.down.x + "," + n.down.y);

        n = nodes.get(0);
        System.out.println("Main Node: " + n.x + "," + n.y);
        if (n.left != null) System.out.println("Left Node: " + n.left.x + "," + n.left.y);
        if (n.right != null) System.out.println("Right Node: " + n.right.x + "," + n.right.y);
        if (n.up != null) System.out.println("Up Node: " + n.up.x + "," + n.up.y);
        if (n.down != null) System.out.println("Down Node: " + n.down.x + "," + n.down.y);
        */

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
