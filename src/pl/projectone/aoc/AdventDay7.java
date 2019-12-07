package pl.projectone.aoc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AdventDay7 {

    private static ArrayList<Integer> initialCodes = new ArrayList<>();
    private static ArrayList<Integer> calculatedCodes = new ArrayList<>();
    private static ArrayList<Integer> outputCodes = new ArrayList<>();
    private static int thrusterOutput = 0;
    private static HashMap<String, Integer> inputCombinations = new HashMap<>();
    private static HashMap<String, Integer> inputCombinations2 = new HashMap<>();

    public static void calculateAoC7(String file) {

        /**
        generateInputs();
        calcThrusters(file, inputCombinations, 1);
        findMax(inputCombinations);
        System.out.println("loop 2");
        System.out.println(calculatedCodes.get(28));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int tempa = thrusterOutput;
         */



        thrusterOutput = 0;
        generateInputs2();
        calcThrusters(file, inputCombinations2, 2);
        findMax(inputCombinations2);
        //System.out.println(tempa + " sum: " + (tempa + thrusterOutput));

    }

    private static void calcThrusters(String file, HashMap<String, Integer> inputCombinations, int mode) {

        Integer thrusterValue = 0;

        inputCombinations.forEach((k, v) -> {

            System.out.println("combination--------------- " + k);
            String temp = k;
            if (mode == 2) readDirections(file);


            for (int iter = 0; iter < 5; iter++) {
                System.out.println("loop " + iter + " thursters: " + thrusterOutput);
                readDirections(file);
                System.out.println(calculatedCodes.get(28));
                int input = Integer.parseInt(temp.substring(iter, iter + 1));
                int position = 0;
                while (position < calculatedCodes.size()) {
                    if (position > 0) input = thrusterOutput;
                    System.out.println("Position: " + position + " instr: " + calculatedCodes.get(position));
                    int[] params = interpreter(calculatedCodes.get(position));
                    position = runInstruction(params, position, input);
                    //System.out.println(" pos " + position + " inp " + input);
                }
            }
            inputCombinations.put(k, thrusterOutput);
            //v = thrusterOutput;
            thrusterOutput = 0;
        });

        inputCombinations.forEach((k, v) -> System.out.println(k + " : " + v));
    }

    public static void findMax(HashMap<String, Integer> inputCombinations) {
        int max = Collections.max(inputCombinations.values());
        Optional<String> key = inputCombinations.entrySet().stream().
                filter(e -> e.getValue() == max).
                map(e -> e.getKey()).findFirst();
        System.out.println(key.orElse("not found") + " value: " + inputCombinations.get(key.get()));
        thrusterOutput = inputCombinations.get(key.get());
    }

    private static void generateInputs() {
        boolean[] unique = {false, false, false, false, false};

        for (int t1 = 0; t1 < 5; t1++) {
            unique[t1] = true;
            for (int t2 = 0; t2 < 5; t2++) {
                if (unique[t2]) continue;
                else unique[t2] = true;
                for (int t3 = 0; t3 < 5; t3++) {
                    if (unique[t3]) continue;
                    else unique[t3] = true;
                    for (int t4 = 0; t4 < 5; t4++) {
                        if (unique[t4]) continue;
                        else unique[t4] = true;
                        for (int t5 = 0; t5 < 5; t5++) {
                            if (unique[t5]) continue;
                            else unique[t5] = true;
                            String combination =
                                    Integer.toString(t1) +
                                            Integer.toString(t2) +
                                            Integer.toString(t3) +
                                            Integer.toString(t4) +
                                            Integer.toString(t5);
                            inputCombinations.put(combination, 0);
                            //System.out.println(combination);
                            unique[t5] = false;
                        }
                        unique[t4] = false;
                    }
                    unique[t3] = false;
                }
                unique[t2] = false;
            }
            unique[t1] = false;
        }
        //inputCombinations.forEach((k, v) -> System.out.println(k));
        System.out.println("hash " + inputCombinations.size());
    }

    private static void generateInputs2() {
        boolean[] unique = {false, false, false, false, false};

        for (int t1 = 5; t1 < 10; t1++) {
            unique[t1-5] = true;
            for (int t2 = 5; t2 < 10; t2++) {
                if (unique[t2-5]) continue;
                else unique[t2-5] = true;
                for (int t3 = 5; t3 < 10; t3++) {
                    if (unique[t3-5]) continue;
                    else unique[t3-5] = true;
                    for (int t4 = 5; t4 < 10; t4++) {
                        if (unique[t4-5]) continue;
                        else unique[t4-5] = true;
                        for (int t5 = 5; t5 < 10; t5++) {
                            if (unique[t5-5]) continue;
                            else unique[t5-5] = true;
                            String combination =
                                    Integer.toString(t1) +
                                            Integer.toString(t2) +
                                            Integer.toString(t3) +
                                            Integer.toString(t4) +
                                            Integer.toString(t5);
                            inputCombinations2.put(combination, 0);
                            //System.out.println(combination);
                            unique[t5-5] = false;
                        }
                        unique[t4-5] = false;
                    }
                    unique[t3-5] = false;
                }
                unique[t2-5] = false;
            }
            unique[t1-5] = false;
        }
        //inputCombinations.forEach((k, v) -> System.out.println(k));
        System.out.println("hash " + inputCombinations2.size());
    }

    private static void readDirections(String file) {

        try (Scanner input = new Scanner(new FileInputStream(file), StandardCharsets.UTF_8)) {
            String line = input.nextLine();
            String[] tempcodes = line.split(",");
            initialCodes.clear();
            calculatedCodes.clear();
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

        int a = 0;
        int b = 0;

        if (params[4] != 9) {
            System.out.println(params[4] + " reading a");
            if (position < calculatedCodes.size() - 1) {
                if (params[2] == 0) {
                    if (calculatedCodes.get(position + 1) < calculatedCodes.size()) {
                        System.out.println("size: " + calculatedCodes.size() + " address: " + calculatedCodes.get(position + 1));
                        a = calculatedCodes.get(calculatedCodes.get(position + 1));
                    }
                }
                else if (params[2] == 1) {
                    if (position + 1 < calculatedCodes.size()) {
                        a = calculatedCodes.get(position + 1);
                    }
                }
                else {
                    System.out.println("Error: Param 1 = " + params[2]);
                    System.exit(0);
                }
            }
        }

        if ((params[4] == 1) ||
                (params[4] == 2) ||
                (params[4] == 5) ||
                (params[4] == 6) ||
                (params[4] == 7) ||
                (params[4] == 8)) {

            System.out.println(params[4] + " reading b");

            if (position < calculatedCodes.size() - 2) {
                if (params[1] == 0) {
                    if (calculatedCodes.get(position + 2) < calculatedCodes.size()) {
                        b = calculatedCodes.get(calculatedCodes.get(position + 2));
                    }
                }
                else if (params[1] == 1) {
                    if (position + 2 < calculatedCodes.size()) {
                        b = calculatedCodes.get(position + 2);
                    }
                }
                else {
                    System.out.println("Error: Param 2 = " + params[1]);
                    System.exit(0);
                }
            }
        }

        if (params[0] != 0) System.out.println("Error: Param 1 = " + params[0]);

        if (params[4] == 1) {
            pos = position + 4;
            calculatedCodes.set(calculatedCodes.get(position + 3), a + b);
        }
        else if (params[4] == 2) {
            pos = position + 4;
            calculatedCodes.set(calculatedCodes.get(position + 3), a * b);
        }
        else if (params[4] == 3) {
            //System.out.println("wlazłem chcę " + (position+1));
            System.out.println(calculatedCodes.get(position+1));
            System.out.println("Position 28 -- " + calculatedCodes.get(28));
            calculatedCodes.set(calculatedCodes.get(position+1), input);
            pos = position + 2;
            //System.out.println("3 - old pos: " + position + " new pos: " + pos + " input: " + input);
        }
        else if (params[4] == 4) {
            pos = position + 2;
            System.out.println(a);
            outputCodes.add(a);
            thrusterOutput = a;
        }
        else if (params[4] == 5) {
            if (a == 0) pos = position + 3;
            else pos = b;
            System.out.println("5 - instr jump - " + a + " jum to " + b);
        }
        else if (params[4] == 6) {
            if (a == 0) pos = b;
            else pos = position + 3;
        }
        else if (params[4] == 7) {
            pos = position + 4;
            int result = 0;
            if (a < b) result = 1;
            calculatedCodes.set(calculatedCodes.get(position + 3), result);
        }
        else if (params[4] == 8) {
            pos = position + 4;

            int result = 0;
            if (a == b) result = 1;
            calculatedCodes.set(calculatedCodes.get(position + 3), result);
        }
        else if ((params[4] == 9) && (params[3] == 9)) {
            System.out.println("Code 99 at position: " + pos + " terminating program...");
            pos = calculatedCodes.size();
            //System.exit(0);
        }
        else {
            System.out.println("Unknown code, exiting program...");
            System.exit(0);
        }
        return pos;
    }



}
