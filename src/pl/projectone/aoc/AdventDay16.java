package pl.projectone.aoc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class AdventDay16 {

    private static ArrayList<Integer> inputList = new ArrayList<>();
    private static ArrayList<Integer> newList = new ArrayList<>();
    private static ArrayList<Integer> newList2 = new ArrayList<>();
    private static int[] pattern = {1, 0, -1, 0};
    private static int index = 0;

    public static void calculateAoc16() {

        // part 1
        /**
        readDirections("./inputs/day16/input1.txt");
        System.out.println("Initial list -----------------");
        for (int i = 0; i < inputList.size(); i++) System.out.print(inputList.get(i));
        System.out.println("\n------------------------------");
        calculate(100);
        System.out.println();
        */

        // part 2
        readDirections2("./inputs/day16/input1.txt");
        System.out.println("index - " + index);
        calculate3(100);

    }

    private static void calculate3(int loops) {

        int sum = 0;

        for (int i = index; i < inputList.size(); i++) {
            newList.add(inputList.get(i));
        }

        for (int l = 0; l < loops; l++) {

            for (int x = 0; x < newList.size(); x++) {
                sum += newList.get(x);
            }
            System.out.println("Loop -------------- " + l + " ---- base sum = " + sum);
            newList2.add(Math.floorMod(Math.abs(sum), 10));

            for (int x = 1; x < newList.size(); x++) {
                sum -= newList.get(x-1);
                newList2.add(Math.floorMod(Math.abs(sum), 10));
            }
            sum = 0;
            newList.clear();
            for (int z = 0; z < newList2.size(); z++) newList.add(newList2.get(z));
            newList2.clear();
        }
        for (int h = 0; h < 8; h++) System.out.print(newList.get(h));
    }

    private static void calculate(int loops) {

        for (int l = 0; l < loops; l++) {

            int counter = 0;
            int sum = 0;
            int digit = 0;
            int converted = 0;
            int repeat = 0;

            for (int i = 0; i < inputList.size(); i++) {
                System.out.println("Loop " + i + " ----------------------------------");
                for (int j = i; j < inputList.size(); j++) {

                    digit = inputList.get(j) * pattern[counter];
                    sum += digit;
                    repeat++;
                    if (repeat > i) {
                        counter++;
                        repeat = 0;
                    }
                    if (counter > 3) counter = 0; // return to beginning of pattern
                }
                if (Math.abs(sum) > 9) {
                    sum = Math.floorMod(Math.abs(sum), 10);
                }
                else sum = Math.abs(sum);
                newList.add(sum);
                sum = 0;
                counter = 0;
                repeat = 0;
            }
            inputList.clear();
            for (int x = 0; x < newList.size(); x++) {
                inputList.add(newList.get(x));
            }
            newList.clear();
        }
    }

    private static void readDirections2(String file) {

        ArrayList<Long> result = new ArrayList<>();
        try (Scanner input = new Scanner(new FileInputStream(file), StandardCharsets.UTF_8)) {
            String line = input.nextLine();
            index = Integer.parseInt(line.substring(0, 7));
            for (int x = 0; x < 10000; x++) {
                for (int i = 0; i < line.length(); i++) {
                    inputList.add(Integer.parseInt(line.substring(i, i+1)));
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }
    }

    private static void readDirections(String file) {

        ArrayList<Long> result = new ArrayList<>();
        try (Scanner input = new Scanner(new FileInputStream(file), StandardCharsets.UTF_8)) {
            String line = input.nextLine();
            for (int i = 0; i < line.length(); i++) {
                inputList.add(Integer.parseInt(line.substring(i, i+1)));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }
    }

}
