package pl.projectone.aoc;

import javax.sound.midi.Soundbank;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class AdventDay9 {

    private static int baseRef = 0;

    private static ArrayList<Long> initialCodes = new ArrayList<>();
    private static ArrayList<Long> calculatedCodes = new ArrayList<>();
    private static ArrayList<Long> outputCodes = new ArrayList<>();

    public static void calculateAoC9(String file, long input) {

        readDirections(file);
        System.out.println(calculatedCodes.size());
        for (int i = 0; i < 1000000; i++) {
            calculatedCodes.add((long) 0);
        }
        System.out.println(calculatedCodes.size());

        int position = 0;
        while (position < calculatedCodes.size()) {
            System.out.println("Position: " + position + " instr: " + calculatedCodes.get(position));
            int[] params = interpreter(calculatedCodes.get(position));
            position = (int) runInstruction(params, position, input);
        }

    }

    private static void readDirections(String file) {

        try (Scanner input = new Scanner(new FileInputStream(file), StandardCharsets.UTF_8)) {
            String line = input.nextLine();
            String[] tempcodes = line.split(",");
            for (String s : tempcodes) {
                initialCodes.add(Long.parseLong(s));
                calculatedCodes = initialCodes;
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }
    }

    private static int[] interpreter(long codeInt) {
        String code = Long.toString(codeInt);
        int[] codes = {0, 0, 0, 0, 0};
        int offset = 5 - code.length();
        for (int i = 0; i < code.length(); i++) {
            codes[i + offset] = Integer.parseInt(code.substring(i, i+1));
        }
        return codes;
    }

    private static long runInstruction(int[] params, int position, long input) {

        long pos = position;
        long a0 = -1234;
        long a1 = -1234;
        long a2 = -1234;
        long b0 = -1234;
        long b1 = -1234;
        long b2 = -1234;
        long c0 = -1234;
        long c1 = -1234;
        long c2 = -1234;

        if (position < calculatedCodes.size() - 2) {
            a1 = calculatedCodes.get(position+1);
            if (params[2] == 0) a0 = calculatedCodes.get((int) a1);
            if (params[2] == 2) a2 = calculatedCodes.get((int) a1 + baseRef);
        }

        if (position < calculatedCodes.size() - 3) {
            b1 = calculatedCodes.get(position+2);
            if (params[1] == 0) b0 = calculatedCodes.get((int) b1);
            if (params[1] == 2) b2 = calculatedCodes.get((int) b1 + baseRef);
        }
        if (position < calculatedCodes.size() - 4) {
            c1 = calculatedCodes.get(position+3);
            if (params[0] == 0) c0 = calculatedCodes.get((int) c1);
            if (params[0] == 2) c2 = calculatedCodes.get((int) c1 + baseRef);
        }
        if (params[0] > 2) {
            System.out.println("Error, param 0 = " + params[0]);
            System.exit(0);
        }
        if (params[1] > 2) {
            System.out.println("Error, param 0 = " + params[1]);
            System.exit(0);
        }
        if (params[2] > 2) {
            System.out.println("Error, param 0 = " + params[2]);
            System.exit(0);
        }

        long param2 = 0;
        long param1 = 0;
        long param0 = 0;

        //System.out.println("A0: " + a0 + " A1: " + a1 + " A2: " + a2 +
        //        " B0: " + b0 + " B1: " + b1 + " B2: " + b2 +
        //        " C0: " + c0 + " C1: " + c1 + " C2: " + c2);

        if (params[4] == 1) {
            pos = position + 4;
            if (params[2] == 0) param2 = a0;
            else if (params[2] == 1) param2 = a1;
            else param2 = a2;

            if (params[1] == 0) param1 = b0;
            else if (params[1] == 1) param1 = b1;
            else param1 = b2;

            if (params[0] == 0) param0 = c1;
            else if (params[0] == 1) param0 = position+3;
            else param0 = c1+baseRef;

            calculatedCodes.set((int) param0, param2 + param1);
            //System.out.println("Adding: A: " + param2 + " B: " + param1 + " Writing to: " + param0);
        }
        else if (params[4] == 2) {
            pos = position + 4;
            if (params[2] == 0) param2 = a0;
            else if (params[2] == 1) param2 = a1;
            else param2 = a2;

            if (params[1] == 0) param1 = b0;
            else if (params[1] == 1) param1 = b1;
            else param1 = b2;

            if (params[0] == 0) param0 = c1;
            else if (params[0] == 1) param0 = position+3;
            else param0 = c1+baseRef;

            calculatedCodes.set((int) param0, param2 * param1);
            //System.out.println("Multiplying: A: " + param2 + " B: " + param1 + " Writing to: " + param0);

        }
        else if (params[4] == 3) {
            pos = position + 2;
            if (params[2] == 0) param2 = a1;
            else if (params[2] == 1) param2 = position+1;
            else param2 = a1+baseRef;

            calculatedCodes.set((int) param2, input);
            //System.out.println("Input: A: " + input + " Writing to: " + param2);

        }
        else if (params[4] == 4) {
            pos = position + 2;
            if (params[2] == 0) param2 = a0;
            else if (params[2] == 1) param2 = a1;
            else param2 = a2;

            System.out.println("Output: A: " + param2);
            outputCodes.add(param2);

        }
        else if (params[4] == 5) {

            if (params[2] == 0) param2 = a0;
            else if (params[2] == 1) param2 = a1;
            else param2 = a2;

            if (params[1] == 0) param1 = b0;
            else if (params[1] == 1) param1 = b1;
            else param1 = b2;

            if (param2 != 0) pos = param1;
            else pos = position + 3;

            //System.out.println("Checking a: " + param2 + " jumping to: " + pos + " baseRef: " + baseRef + " pos+2 " + calculatedCodes.get(position + 2));
        }
        else if (params[4] == 6) {

            if (params[2] == 0) param2 = a0;
            else if (params[2] == 1) param2 = a1;
            else param2 = a2;

            if (params[1] == 0) param1 = b0;
            else if (params[1] == 1) param1 = b1;
            else param1 = b2;

            if (param2 == 0) pos = param1;
            else pos = position + 3;

            //System.out.println("Checking a: " + param2 + " jumping to: " + pos + " baseRef: " + baseRef + " pos+2 " + calculatedCodes.get(position + 2));

        }
        else if (params[4] == 7) {
            pos = position + 4;

            if (params[2] == 0) param2 = a0;
            else if (params[2] == 1) param2 = a1;
            else param2 = a2;

            if (params[1] == 0) param1 = b0;
            else if (params[1] == 1) param1 = b1;
            else param1 = b2;

            if (params[0] == 0) param0 = c1;
            else if (params[0] == 1) param0 = position+3;
            else param0 = c1+baseRef;

            long result = 0;
            if (param2 < param1) result = 1;

            calculatedCodes.set((int) param0, result);
            //System.out.println("Comparing: " + param2 + " < " + param1 + " - result = " + result);

        }
        else if (params[4] == 8) {
            pos = position + 4;

            if (params[2] == 0) param2 = a0;
            else if (params[2] == 1) param2 = a1;
            else param2 = a2;

            if (params[1] == 0) param1 = b0;
            else if (params[1] == 1) param1 = b1;
            else param1 = b2;

            if (params[0] == 0) param0 = c1;
            else if (params[0] == 1) param0 = position+3;
            else param0 = c1+baseRef;

            long result = 0;
            if (param2 == param1) result = 1;

            calculatedCodes.set((int) param0, result);
            //System.out.println("Comparing: " + param2 + " = " + param1 + " - result = " + result);
        }
        else if ((params[4] == 9) && (params[3] == 9)) {
            System.out.println("Code 99 at position: " + pos + " terminating program...");
            pos = calculatedCodes.size();
        }
        else if (params[4] == 9){
            pos = position + 2;

            if (params[2] == 0) param2 = a0;
            else if (params[2] == 1) param2 = a1;
            else param2 = a2;

            //System.out.println("BaseRef before: " + baseRef + " BaseRef after: " + (baseRef + param2));
            baseRef += param2;
        }
        else {
            System.out.println("Unknown code, exiting program...");
            System.exit(0);
        }
        return pos;
    }

}
