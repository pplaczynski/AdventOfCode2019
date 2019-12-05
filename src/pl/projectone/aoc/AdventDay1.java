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
        int fuelMass = calculateFuel(masses);
        System.out.println(fuelMass);
        System.out.println(fuelForFuel(fuelMass));
        System.out.println("F2: " + calcF2(masses));
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

    private static int fuelForFuel(int initialMass) {
        boolean condition = true;
        int finalMass = initialMass;
        int tempMass = initialMass;
        while(condition) {
            tempMass = tempMass/3 - 2;
            if (tempMass > 0) finalMass += tempMass;
            else condition = false;
        }
        return finalMass;
    }

    private static int calcF2(ArrayList<Integer> masses) {
        int totalFuel = 0;
        int moduleFuel = 0;
        int totalmoduleFuel = 0;
        int f4f = 0;
        for (int i = 0; i < masses.size(); i++) {
            moduleFuel = (masses.get(i)/3) - 2;
            totalmoduleFuel += moduleFuel;
            f4f = fuelForFuel(moduleFuel);
            totalFuel += f4f;
        }
        return totalFuel > 0 ? totalFuel : 0;
    }
}
