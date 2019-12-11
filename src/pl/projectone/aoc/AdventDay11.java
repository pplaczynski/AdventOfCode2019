package pl.projectone.aoc;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public abstract class AdventDay11 {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    private static String[][] paintMap = new String[WIDTH][HEIGHT];
    private static int[][] visitMap = new int[WIDTH][HEIGHT];
    private static BlockingQueue<Integer> robot = new ArrayBlockingQueue<Integer>(1000);
    private static BlockingQueue<Integer> comp = new ArrayBlockingQueue<Integer>(1000);
    private static ArrayList<Long> initialCodes = new ArrayList<>();

    public static void calculateAoC11() {

        initialCodes = readDirections("./inputs/day11/input1.txt");
        for (int i = 0; i < 1000000; i++) {
            initialCodes.add((long) 0);
        }
        initMaps();

        paintMap[WIDTH/2][HEIGHT/2] = "#";
        CompDay11 computer = new CompDay11(initialCodes, comp, robot);
        RobotDay11 robo = new RobotDay11(robot, comp, WIDTH/2, HEIGHT/2, paintMap, visitMap);

        ExecutorService executor = Executors.newCachedThreadPool();
        CompletionService<Integer> service = new ExecutorCompletionService<>(executor);

        Future<Integer> roboFuture = service.submit(robo);
        Future<Integer> compFuture = service.submit(computer);


        try {
            roboFuture = service.take();
            roboFuture.get();
            compFuture = service.take();
            compFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        finally {
            executor.shutdown();
        }

        printPaintMap();
        System.out.println();
        //printVisitMap();
        System.out.println("Visited at least once: " + countVisited());
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

    private static void printPaintMap() {

        for (int h = 0; h < WIDTH; h++) {
            for (int w = 0; w < HEIGHT; w++ ) {
                System.out.print(paintMap[w][h]);
            }
            System.out.println();
        }

    }

    private static void printVisitMap() {

        for (int w = 0; w < WIDTH; w++) {
            for (int h = 0; h < HEIGHT; h++ ) {
                System.out.print(visitMap[w][h]);
            }
            System.out.println();
        }
    }

    private static void initMaps() {
        for (int w = 0; w < WIDTH; w++) {
            for (int h = 0; h < HEIGHT; h++ ) {
                paintMap[w][h] = ".";
                visitMap[w][h] = 0;
            }
        }
    }

    private static int countVisited() {
        int result = 0;
        for (int w = 0; w < WIDTH; w++) {
            for (int h = 0; h < HEIGHT; h++ ) {
                if (visitMap[w][h] > 0) result++;
            }
        }
        return result;
    }


}
