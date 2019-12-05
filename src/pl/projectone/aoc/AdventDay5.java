package pl.projectone.aoc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class AdventDay5 {

    private static ArrayList<Integer> initialCodes = new ArrayList<>();
    private static ArrayList<Integer> calculatedCodes = new ArrayList<>();
    private static ArrayList<Integer> outputCodes = new ArrayList<>();


    public static void calculateAoC5(String file, int input) {

        readDirections(file);
        System.out.println(calculatedCodes.size());
        int position = 0;
        while (position < calculatedCodes.size()) {
            System.out.println("Position: " + position + " instr: " + calculatedCodes.get(position));
            int[] params = interpreter(calculatedCodes.get(position));
            position = runInstruction(params, position, input);
        }

    }

    private static void readDirections(String file) {

        try (Scanner input = new Scanner(new FileInputStream(file), StandardCharsets.UTF_8)) {
            String line = input.nextLine();
            String[] tempcodes = line.split(",");
            for (String s : tempcodes) {
                initialCodes.add(Integer.parseInt(s));
                calculatedCodes = initialCodes;
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }
    }

    private static int[] interpreter(int codeInt) {
        String code = Integer.toString(codeInt);
        int[] codes = {0, 0, 0, 0, 0};
        int offset = 5 - code.length();
        for (int i = 0; i < code.length(); i++) {
            codes[i + offset] = Integer.parseInt(code.substring(i, i+1));
        }
        return codes;
    }

    private static int runInstruction(int[] params, int position, int input) {

        int pos = position;

        if (params[4] == 1) {
            int a = 0;
            int b = 0;
            pos = position + 4;

            if (params[2] == 0) a = calculatedCodes.get(calculatedCodes.get(position + 1));
            else if (params[2] == 1) a = calculatedCodes.get(position + 1);
            else {
                System.out.println("Error: Param 1 = " + params[2]);
                System.exit(0);
            }

            if (params[1] == 0) b = calculatedCodes.get(calculatedCodes.get(position + 2));
            else if (params[1] == 1) b = calculatedCodes.get(position + 2);
            else {
                System.out.println("Error: Param 2 = " + params[1]);
                System.exit(0);
            }

            if (params[0] == 0) calculatedCodes.set(calculatedCodes.get(position + 3), a + b);
            else {
                System.out.println("Error: Param 1 = " + params[0]);
            }
        }
        else if (params[4] == 2) {
            int a = 0;
            int b = 0;
            pos = position + 4;

            if (params[2] == 0) a = calculatedCodes.get(calculatedCodes.get(position + 1));
            else if (params[2] == 1) a = calculatedCodes.get(position + 1);
            else {
                System.out.println("Error: Param 1 = " + params[2]);
                System.exit(0);
            }

            if (params[1] == 0) b = calculatedCodes.get(calculatedCodes.get(position + 2));
            else if (params[1] == 1) b = calculatedCodes.get(position + 2);
            else {
                System.out.println("Error: Param 2 = " + params[1]);
                System.exit(0);
            }

            if (params[0] == 0) calculatedCodes.set(calculatedCodes.get(position + 3), a * b);
            else {
                System.out.println("Error: Param 1 = " + params[0]);
            }
        }
        else if (params[4] == 3) {
            calculatedCodes.set(calculatedCodes.get(position+1), input);
            pos = position + 2;
        }
        else if (params[4] == 4) {
            pos = position + 2;
            if (params[2] == 0) {
                System.out.println(calculatedCodes.get(calculatedCodes.get(position + 1)));
                outputCodes.add(calculatedCodes.get(calculatedCodes.get(position + 1)));
            }
            else if (params[2] == 1) {
                System.out.println(calculatedCodes.get(position + 1));
                outputCodes.add(calculatedCodes.get(position + 1));
            }
            else System.out.println("Error Param 1 = " + params[2]);

        }
        else if (params[4] == 5) {
            int a = 0;
            int b = 0;

            if (params[2] == 0) a = calculatedCodes.get(calculatedCodes.get(position + 1));
            else if (params[2] == 1) a = calculatedCodes.get(position + 1);
            else {
                System.out.println("Error: Param 1 = " + params[2]);
                System.exit(0);
            }

            if (params[1] == 0) b = calculatedCodes.get(calculatedCodes.get(position + 2));
            else if (params[1] == 1) b = calculatedCodes.get(position + 2);
            else {
                System.out.println("Error: Param 2 = " + params[1]);
                System.exit(0);
            }

            if (a != 0) pos = b;
            else pos = position + 3;

        }
        else if (params[4] == 6) {
            int a = 0;
            int b = 0;

            if (params[2] == 0) a = calculatedCodes.get(calculatedCodes.get(position + 1));
            else if (params[2] == 1) a = calculatedCodes.get(position + 1);
            else {
                System.out.println("Error: Param 1 = " + params[2]);
                System.exit(0);
            }

            if (params[1] == 0) b = calculatedCodes.get(calculatedCodes.get(position + 2));
            else if (params[1] == 1) b = calculatedCodes.get(position + 2);
            else {
                System.out.println("Error: Param 2 = " + params[1]);
                System.exit(0);
            }

            if (a == 0) pos = b;
            else pos = position + 3;
        }
        else if (params[4] == 7) {
            int a = 0;
            int b = 0;
            pos = position + 4;

            if (params[2] == 0) a = calculatedCodes.get(calculatedCodes.get(position + 1));
            else if (params[2] == 1) a = calculatedCodes.get(position + 1);
            else {
                System.out.println("Error: Param 1 = " + params[2]);
                System.exit(0);
            }

            if (params[1] == 0) b = calculatedCodes.get(calculatedCodes.get(position + 2));
            else if (params[1] == 1) b = calculatedCodes.get(position + 2);
            else {
                System.out.println("Error: Param 2 = " + params[1]);
                System.exit(0);
            }

            int result = 0;
            if (a < b) result = 1;

            if (params[0] == 0) calculatedCodes.set(calculatedCodes.get(position + 3), result);
            else {
                System.out.println("Error: Param 1 = " + params[0]);
            }
        }
        else if (params[4] == 8) {
            int a = 0;
            int b = 0;
            pos = position + 4;

            if (params[2] == 0) a = calculatedCodes.get(calculatedCodes.get(position + 1));
            else if (params[2] == 1) a = calculatedCodes.get(position + 1);
            else {
                System.out.println("Error: Param 1 = " + params[2]);
                System.exit(0);
            }

            if (params[1] == 0) b = calculatedCodes.get(calculatedCodes.get(position + 2));
            else if (params[1] == 1) b = calculatedCodes.get(position + 2);
            else {
                System.out.println("Error: Param 2 = " + params[1]);
                System.exit(0);
            }

            int result = 0;
            if (a == b) result = 1;
            if (params[0] == 0) calculatedCodes.set(calculatedCodes.get(position + 3), result);
            else {
                System.out.println("Error: Param 1 = " + params[0]);
            }

        }
        else if ((params[4] == 9) && (params[3] == 9)) {
            System.out.println("Code 99 at position: " + pos + " terminating program...");
            System.exit(0);
        }
        else {

        }
        return pos;
    }

}
