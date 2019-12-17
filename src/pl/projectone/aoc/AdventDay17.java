package pl.projectone.aoc;

import com.sun.net.httpserver.Headers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class AdventDay17 {

    private static final int WIDTH = 80;
    private static final int HEIGHT = 45;
    private static BlockingQueue<Integer> robot = new ArrayBlockingQueue<Integer>(100);

    public static void calculateAoC17() {

        ArrayList<Integer> initialCodes = new ArrayList<>();

        int[] main = {65,44,66,44,65,44,67,44,66,44,67,44,65,44,66,44,65,44,67,10};
        int[] a = {82,44,54,44,76,44,49,48,44,82,44,56,44,82,44,56,10};
        int[] b = {82,44,49,50,44,76,44,56,44,76,44,49,48,10};
        int[] c = {82,44,49,50,44,76,44,49,48,44,82,44,54,44,76,44,49,48,10};

        try {
            for (int i = 0; i < main.length; i++) robot.put(main[i]);
            for (int i = 0; i < a.length; i++) robot.put(a[i]);
            for (int i = 0; i < b.length; i++) robot.put(b[i]);
            for (int i = 0; i < c.length; i++) robot.put(c[i]);
            robot.put(110);
            robot.put(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int[][] map = new int[HEIGHT][WIDTH];

        initialCodes = readDirections("./inputs/day17/input2.txt");
        for (int i = 0; i < 1000000; i++) {
            initialCodes.add(0);
        }

        Computer17.run(initialCodes, map, robot);
        //Part 1 below + change input file to 1
        //printMap(map);
        //intersections(map);
        //ArrayList<String> path = calcPath(map);
        //calcMovements(path);
    }

    private static void calcMovements(ArrayList<String> path) {

        String a = "";
        String b = "";
        String c = "";
        String fullPath = "";
        for (int o = 0; o < path.size(); o++) fullPath += path.get(o);

        for (int i = 1; i < 20; i++) {
            a = fullPath.substring(0, i);
            String tempPathA = fullPath.replace(a, "");
            for (int j = 1; j < 20; j++) {
                b = tempPathA.substring(0, j);
                String tempPathB = tempPathA.replace(b, "");
                for (int k = 1; k < tempPathB.length(); k++) {
                    c = tempPathB.substring(0, k);
                    String tempPathC = tempPathB.replace(c, "");
                    if (tempPathC.length() == 0) {
                        String pattern = fullPath.replace(a, "A");
                        pattern = pattern.replace(b, "B");
                        pattern = pattern.replace(c, "C");
                        if (pattern.length() <= 10) System.out.println("Found a: " + a + " , b: " + b + " , c: " + c + "---- pattern: " + pattern);
                    }
                    c = "";
                }
                b = "";
            }
            a = "";
        }
    }

    private static ArrayList<String> calcPath(int[][] map) {

        int x = 0;
        int y = 0;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (map[j][i] == 94) {
                    x = i;
                    y = j;
                }
            }
        }
        System.out.println("Found starting point: " + x + "," + y);

        int steps = 0;
        boolean end = false;
        int direction = 1; // 1 - up, 2 - down, 3 - right, 4 - left;
        ArrayList<String> path = new ArrayList<>();

        // # = 35, . = 46, NL = 10, , = 44, R = 82, L = 76, A = 65, B = 66, C = 67

        while(!end) {

            // heading up
            if ((direction == 1) && (map[y-1][x] == 35)) {
                y--;
                steps++;
                continue;
            }
            else if ((direction == 1) && (map[y][x+1] == 35)) {
                if (steps > 0) path.add(Integer.toString(steps));
                steps = 0;
                path.add("R");
                direction = 3;
                continue;
            }
            else if ((direction == 1) && (map[y][x-1] == 35)) {
                if (steps > 0) path.add(Integer.toString(steps));
                steps = 0;
                path.add("L");
                direction = 4;
                continue;
            }

            // heading down
            if ((direction == 2) && (map[y+1][x] == 35)) {
                y++;
                steps++;
                continue;
            }
            else if ((direction == 2) && (map[y][x-1] == 35)) {
                if (steps > 0) path.add(Integer.toString(steps));
                steps = 0;
                path.add("R");
                direction = 4;
                continue;
            }
            else if ((direction == 2) && (map[y][x+1] == 35)) {
                if (steps > 0) path.add(Integer.toString(steps));
                steps = 0;
                path.add("L");
                direction = 3;
                continue;
            }

            // heading right
            if ((direction == 3) && (map[y][x+1] == 35)) {
                x++;
                steps++;
                continue;
            }
            else if ((direction == 3) && (map[y+1][x] == 35)) {
                if (steps > 0) path.add(Integer.toString(steps));
                steps = 0;
                path.add("R");
                direction = 2; // right from right is down
                continue;
            }
            else if ((direction == 3) && (map[y-1][x] == 35)) {
                if (steps > 0) path.add(Integer.toString(steps));
                steps = 0;
                path.add("L");
                direction = 1; // left from right is up
                continue;
            }

            // heading left
            if ((direction == 4) && (map[y][x-1] == 35)) {
                x--;
                steps++;
                continue;
            }
            else if ((direction == 4) && (map[y-1][x] == 35)) {
                if (steps > 0) path.add(Integer.toString(steps));
                steps = 0;
                path.add("R");
                direction = 1; // right from left is up
                continue;
            }
            else if ((direction == 4) && (map[y+1][x] == 35)) {
                if (steps > 0) path.add(Integer.toString(steps));
                steps = 0;
                path.add("L");
                direction = 2; // left from left is down
                continue;
            }

            if (steps > 0) path.add(Integer.toString(steps));
            System.out.println("Path search finished !");
            end = true;

        }

        String finalPath = "";
        for (int i = 0; i < path.size(); i++) finalPath += path.get(i);
        System.out.println("Final path: " + finalPath);

        return path;
    }

    private static void intersections(int[][] map) {
        int sum = 0;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if ((map[j][i] == 35) && (map[j][i-1] == 35) && (map[j][i+1] == 35) && (map[j-1][i] == 35) && (map[j+1][i] == 35)) {
                    sum += (i-1) * (j-1);
                    System.out.println("Found intersection at: " + (i-1) + "," + (j-1)  + " sum = " + ((i-1)*(j-1)) + " total sum: " + sum);

                }
            }
        }
        System.out.println("Total sum: " + sum);
    }

    private static void printMap(int[][] map) {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                System.out.print(Character.toString((char) map[i][j]));
            }
            System.out.println();
        }
    }

    private static ArrayList<Integer> readDirections(String file) {

        ArrayList<Integer> result = new ArrayList<>();
        try (Scanner input = new Scanner(new FileInputStream(file), StandardCharsets.UTF_8)) {
            String line = input.nextLine();
            String[] tempcodes = line.split(",");
            for (String s : tempcodes) {
                result.add(Integer.parseInt(s));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }
        return result;
    }

}
