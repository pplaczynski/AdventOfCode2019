package pl.projectone.aoc;

import javafx.beans.WeakInvalidationListener;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public abstract class AdventDay8 {

    private static final int WIDTH = 25;
    private static final int HEIGHT = 6;
    private static List<ArrayList<Integer>> images = new ArrayList<>();
    private static ArrayList<Integer> inputData = new ArrayList<>();
    private static ArrayList<Integer> finalData = new ArrayList<>();

    public static void calculateAoC8(String file) {

        readData(file);
        divideLayers();
        //for (int i = 0; i < images.size(); i++) System.out.println(images.get(i).toString());
        findMinZero();
        calculateFinalLayer();
        for (int i = 0; i < images.size(); i++) System.out.println(images.get(i).toString());
        System.out.println("--------------------------");
        System.out.println(finalData.toString());
        System.out.println("--------------------------");
        System.out.println(finalData.size());
        drawPicture();
    }

    private static void drawPicture() {
        int lineCounter = 0;
        for (int i = 0; i < finalData.size(); i++) {
            if (finalData.get(i) == 0) System.out.print(" ");
            if (finalData.get(i) == 1) System.out.print("*");
            if (finalData.get(i) == 2) System.out.print(" ");
            lineCounter +=1;
            if (lineCounter == (WIDTH)) {
                System.out.println();
                lineCounter = 0;
            }
        }
    }

    private static void calculateFinalLayer() {

        int pixel;
        int tempPixel;

        for (int y = 0; y < images.get(0).size(); y++) {
            pixel = images.get(0).get(y);
            finalData.add(y, pixel);
        }

        for (int i = 0; i < images.get(0).size(); i++) {
            if (finalData.get(i) > 1) {
                for (int x = 1; x < images.size(); x++) {
                    tempPixel = images.get(x).get(i);
                    if (tempPixel < 2) {
                        finalData.set(i, tempPixel);
                        break;
                    }
                }
            }
        }
    }

    private static void findMinZero() {
        long max = 100000000;
        long  multiply = 0;
        int index = 0;
        for (int i = 0; i < images.size(); i++) {
            long zeros = images.get(i).stream().filter(x -> x == 0).count();
            long ones = images.get(i).stream().filter(x -> x == 1).count();
            long twos = images.get(i).stream().filter(x -> x == 2).count();
            if (zeros < max) {
                index = i;
                max = zeros;
                multiply = ones * twos;
            }
            if (zeros == max)
                if ((ones * twos) > multiply) index = i;
        }
        System.out.println("Checksum at: " + index + " - " +
                images.get(index).stream().filter(x -> x == 1).count() *
                images.get(index).stream().filter(x -> x == 2).count());
    }
    private static void divideLayers() {
        int layers = inputData.size()/(WIDTH*HEIGHT);
        int counter = 0;
        int layer = 0;
        for (int l = 0; l < layers; l++) {
            images.add(new ArrayList<>());
        }
        for (int i = 0; i < inputData.size(); i++) {
            images.get(layer).add(inputData.get(i));
            counter += 1;
            if (counter == WIDTH * HEIGHT) {
                layer += 1;
                counter = 0;
            }
        }
    }
    private static void readData(String file) {
        int i;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while((i = reader.read()) != -1) {
                inputData.add(Character.getNumericValue(i));
            }
        }
        catch (IOException e) {
            System.out.println("File not found");
            System.exit(0);
        }
    }
}
