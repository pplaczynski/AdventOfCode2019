package pl.projectone.aoc;

import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class AdventDay3 {

    private static String[] wire1;
    private static String[] wire2;
    private static ArrayList<Coordinates> horizontalWire1 = new ArrayList<>();
    private static ArrayList<Coordinates> verticalWire1 = new ArrayList<>();
    private static ArrayList<Coordinates> horizontalWire2 = new ArrayList<>();
    private static ArrayList<Coordinates> verticalWire2 = new ArrayList<>();
    private static ArrayList<Pair<Integer, Integer>> crosspoints = new ArrayList<>();

    public static void calculateAoC3(String file) {
        readDirections(file);
        layWire(wire1, 1);
        layWire(wire2, 2);
        findCrosspoints();
        calculateShortest();
    }

    private static void readDirections(String file) {

        try (Scanner input = new Scanner(new FileInputStream(file), StandardCharsets.UTF_8)) {
            String line1 = input.nextLine();
            wire1 = line1.split(",");
            String line2 = input.nextLine();
            wire2 = line2.split(",");
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }
    }

    private static void layWire(String[] wireMoves, int wire) {
        String direction = "";
        int distance = 0;
        int currentX = 0;
        int currentY = 0;

        for (int i = 0; i < wireMoves.length; i++) {
            direction = wireMoves[i].substring(0, 1);
            distance = Integer.parseInt(wireMoves[i].substring(1));

            //calculate coordinates - we want lower x or y to be xStart or yStart
            if (direction.equals("R")) {
                if (wire == 1) {
                    horizontalWire1.add(new Coordinates(currentX, currentY, currentX+distance, currentY));
                }
                else {
                    horizontalWire2.add(new Coordinates(currentX, currentY, currentX+distance, currentY));
                }
                currentX += distance;
            }
            if (direction.equals("L")) {
                if (wire == 1) {
                    horizontalWire1.add(new Coordinates(currentX-distance, currentY, currentX, currentY));
                }
                else {
                    horizontalWire2.add(new Coordinates(currentX-distance, currentY, currentX, currentY));
                }
                currentX -= distance;
            }
            if (direction.equals("U")) {
                if (wire == 1) {
                    verticalWire1.add(new Coordinates(currentX, currentY, currentX, currentY+distance));
                }
                else {
                    verticalWire2.add(new Coordinates(currentX, currentY, currentX, currentY+distance));
                }
                currentY += distance;
            }
            if (direction.equals("D")) {
                if (wire ==1) {
                    verticalWire1.add(new Coordinates(currentX, currentY-distance, currentX, currentY));
                }
                else {
                    verticalWire2.add(new Coordinates(currentX, currentY-distance, currentX, currentY));
                }
                currentY -= distance;
            }
        }
    }

    private static void findCrosspoints() {

        for (Coordinates hLine : horizontalWire1) {
            for (Coordinates vLine : verticalWire2) {

                //for vertical lines xStart and xEnd are the same
                //for horizontal lines yStart and yEnd are the same
                if ((vLine.xStart >= hLine.xStart) &&
                        (vLine.xEnd <= hLine.xEnd) &&
                        (vLine.yStart <= hLine.yStart) &&
                        (vLine.yEnd >= hLine.yEnd)) {
                    if ((vLine.xStart != 0) && (hLine.yStart != 0))
                        crosspoints.add(new Pair(vLine.xStart, hLine.yStart));
                    //x from vertical, y from horizontal as they are constant
                }
            }
        }

        for (Coordinates hLine : horizontalWire2) {
            for (Coordinates vLine : verticalWire1) {

                //for vertical lines xStart and xEnd are the same
                //for horizontal lines yStart and yEnd are the same
                if ((vLine.xStart >= hLine.xStart) &&
                        (vLine.xEnd <= hLine.xEnd) &&
                        (vLine.yStart <= hLine.yStart) &&
                        (vLine.yEnd >= hLine.yEnd)) {
                    if ((vLine.xStart != 0) && (hLine.yStart != 0))
                        crosspoints.add(new Pair(vLine.xStart, hLine.yStart));
                    //x from vertical, y from horizontal as they are constant
                }
            }
        }
    }

    private static void calculateShortest() {
        int shortestPath = 10000000;
        int currentPath = 0;
        int xShortest = 0;
        int yShortest = 0;
        for (Pair cross : crosspoints) {
            currentPath = Math.abs((Integer) cross.getKey()) + Math.abs((Integer) cross.getValue());
            if (currentPath < shortestPath) {
                shortestPath = currentPath;
                xShortest = (Integer) cross.getKey();
                yShortest = (Integer) cross.getValue();
            }
        }
        System.out.println("Shortest path found for crosspoint [" + xShortest +
                "," + yShortest + "] - distance = " + shortestPath);
    }

    public static void showHorizontals() {
        System.out.println("Showing horizontal lines wire1: ");
        for (Coordinates cH: horizontalWire1)
            System.out.println("[" + cH.xStart + "," + cH.yStart + "]" +
                    "[" + cH.xEnd + "," + cH.yEnd + "]");
        System.out.println("Showing horizontal lines wire2: ");
        for (Coordinates cH: horizontalWire2)
            System.out.println("[" + cH.xStart + "," + cH.yStart + "]" +
                    "[" + cH.xEnd + "," + cH.yEnd + "]");
    }

    public static void showVerticals() {
        System.out.println("Showing vertical lines wire1: ");
        for (Coordinates cH: verticalWire1)
            System.out.println("[" + cH.xStart + "," + cH.yStart + "]" +
                    "[" + cH.xEnd + "," + cH.yEnd + "]");
        System.out.println("Showing vertical lines wire2: ");
        for (Coordinates cH: verticalWire2)
            System.out.println("[" + cH.xStart + "," + cH.yStart + "]" +
                    "[" + cH.xEnd + "," + cH.yEnd + "]");

    }

    public static void showCrosspoints() {
        System.out.println("Showing crosspoints: ");
        for (Pair c : crosspoints) {
            System.out.println("[" + c.getKey() + "," + c.getValue() + "]");
        }
    }

    public static class Coordinates {

        int xStart;
        int yStart;
        int xEnd;
        int yEnd;

        public Coordinates(int xStart, int yStart, int xEnd, int yEnd) {
            this.xStart = xStart;
            this.yStart = yStart;
            this.xEnd = xEnd;
            this.yEnd = yEnd;
        }
    }


}
