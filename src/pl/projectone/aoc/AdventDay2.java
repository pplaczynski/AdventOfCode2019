package pl.projectone.aoc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public abstract class AdventDay2 {

    private static ArrayList<Integer> codes = new ArrayList<>();

    public static void calculateAoC2(String file) {
        readDirections(file);
        ArrayList<Integer> finalCodes = calculateCodes(codes);
        System.out.println(finalCodes);
    }

    private static void readDirections(String file) {

        try (Scanner input = new Scanner(new FileInputStream(file), StandardCharsets.UTF_8)) {
            String line1 = input.nextLine();
            String[] tempcodes = line1.split(",");
            for (String s : tempcodes) {
                codes.add(Integer.parseInt(s));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }
    }

    public static ArrayList<Integer> calculateCodes(ArrayList<Integer> startingCodes) {

        for (int i = 0; i < startingCodes.size(); i += 4) {

            if (i >= startingCodes.size()) break;

            if (startingCodes.get(i) == 1) {
                if (startingCodes.get(i+1) >= startingCodes.size() || startingCodes.get(i+2) >= startingCodes.size()) {
                    System.out.println("Range out of bounds..., ending program");
                    System.exit(0);
                }
                else {
                    int result = startingCodes.get(startingCodes.get(i+1)) + startingCodes.get(startingCodes.get(i+2));
                    if (startingCodes.get(i+3) >= startingCodes.size()) {
                        for (int x = 0; x < startingCodes.get(i+3) - startingCodes.size() + 1; x++) {
                            startingCodes.add(99);
                        }
                    }
                    else {
                        startingCodes.set(startingCodes.get(i+3), result);
                    }
                }
            }
            else if (startingCodes.get(i) == 2) {
                int result = startingCodes.get(startingCodes.get(i+1)) * startingCodes.get(startingCodes.get(i+2));
                if (startingCodes.get(i+3) >= startingCodes.size()) {
                    for (int x = 0; x < startingCodes.get(i+3) - startingCodes.size() + 1; x++) {
                        startingCodes.add(99);
                    }
                }
                else {
                    startingCodes.set(startingCodes.get(i+3), result);
                }
            }

            else if (startingCodes.get(i) == 99) {
                System.out.println("Code 99, ending program...");
                System.out.println(startingCodes.toString());
                System.exit(0);
            }
            else {
                System.out.println("Unknown code " + startingCodes.get(i) + ", ending program...");
                //System.out.println(startingCodes.toString());
                System.exit(0);
            }
        }
        return startingCodes;
    }
}
