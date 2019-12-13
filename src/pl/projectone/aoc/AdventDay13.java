package pl.projectone.aoc;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public abstract class AdventDay13 {

    public static final int WIDTH = 35;
    public static final int HEIGHT = 23;
    private static String[][] paintMap = new String[HEIGHT][WIDTH];
    private static BlockingQueue<Integer> robot = new ArrayBlockingQueue<Integer>(10);
    private static BlockingQueue<Integer> comp = new ArrayBlockingQueue<Integer>(10);
    private static ArrayList<Long> initialCodes = new ArrayList<>();

    public static void calculateAoC13(String file) {

        initialCodes = readDirections(file);
        for (int i = 0; i < 1000000; i++) {
            initialCodes.add((long) 0);
        }
        initMaps();


        Comp2 computer = new Comp2(initialCodes, comp, robot);
        Arkanoid robo = new Arkanoid(robot, comp, paintMap);

        ExecutorService executor = Executors.newCachedThreadPool();
        CompletionService<Integer> service = new ExecutorCompletionService<>(executor);

        Future<Integer> roboFuture = service.submit(robo);
        Future<Integer> compFuture = service.submit(computer);


        try {
            compFuture = service.take();
            System.out.println(" Comp exit code " + compFuture.get());
            //System.out.println(robot.size());
            roboFuture = service.take();
            System.out.println(" Robot exit code - blocks " + roboFuture.get());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        finally {
            executor.shutdown();
        }

        printPaintMap();
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

