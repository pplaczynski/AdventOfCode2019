package pl.projectone.aoc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class AdventDay1 {

    private static ArrayList<Integer> masses;

    public static void calculateAoC3(String file) {
        readDirections(file);
        System.out.println(calculateFuel(masses));
    }

    private static void readDirections(String file) {

        masses = new ArrayList<>();

        try (Scanner input = new Scanner(new FileInputStream(file), StandardCharsets.UTF_8)) {
            while (input.hasNext()) {
                masses.add(Integer.parseInt(input.nextLine()));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }
    }

    private static int calculateFuel(ArrayList<Integer> masses) {

        int fuel = 0;
        for (int i = 0; i < masses.size(); i++) {
            fuel += (masses.get(i)/3) - 2;
        }
        return fuel > 0 ? fuel : 0;
    }
}
