package pl.projectone.aoc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

public abstract class AdventDay7part2 {

    private static ArrayList<Integer> initialCodes = new ArrayList<>();
    private static ArrayList<Integer> calculatedCodes = new ArrayList<>();
    private static HashMap<String, Integer> inputCombinations = new HashMap<>();
    private static int thrusterOutput = 0;

    private static BlockingQueue<Integer> amplifier1 = new ArrayBlockingQueue<Integer>(1000);
    private static BlockingQueue<Integer> amplifier2 = new ArrayBlockingQueue<Integer>(1000);
    private static BlockingQueue<Integer> amplifier3 = new ArrayBlockingQueue<Integer>(1000);
    private static BlockingQueue<Integer> amplifier4 = new ArrayBlockingQueue<Integer>(1000);
    private static BlockingQueue<Integer> amplifier5 = new ArrayBlockingQueue<Integer>(1000);


    public static void calculateThrusters(String file, int mode) {

        initialCodes = readDirections(file);
        calculatedCodes = initialCodes;
        if (mode == 1) generateInputs1();
        else generateInputs2();
        try {
            amplifier1.put(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //calcForK("97568", file);
        //calcForK("98765", file);

        inputCombinations.forEach((k, v) -> {
            calcForK(k, file);
            try {
                amplifier1.clear();
                amplifier2.clear();
                amplifier3.clear();
                amplifier4.clear();
                amplifier5.clear();
                amplifier1.put(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        System.out.println("here");
        findMax(inputCombinations);
    }

    public static void calcForK(String k, String file) {
        List<Callable<Integer>> amplifiers = new ArrayList<>();
        List<Future<Integer>> threads = new ArrayList<>();
        ExecutorService executor = Executors.newCachedThreadPool();
        CompletionService<Integer> rounds = new ExecutorCompletionService<>(executor);

        initialCodes = readDirections(file);

        System.out.println("obliczamy dla " + k);

        amplifiers.add(new Amplifier(Integer.parseInt(k.substring(0, 1)), 1, true, k, readDirections(file), amplifier1, amplifier2, inputCombinations));
        amplifiers.add(new Amplifier(Integer.parseInt(k.substring(1, 2)), 2, true, k, readDirections(file), amplifier2, amplifier3, inputCombinations));
        amplifiers.add(new Amplifier(Integer.parseInt(k.substring(2, 3)), 3, true, k, readDirections(file), amplifier3, amplifier4, inputCombinations));
        amplifiers.add(new Amplifier(Integer.parseInt(k.substring(3, 4)), 4, true, k, readDirections(file), amplifier4, amplifier5, inputCombinations));
        amplifiers.add(new Amplifier(Integer.parseInt(k.substring(4, 5)), 5, true, k, readDirections(file), amplifier5, amplifier1, inputCombinations));

        for (Callable<Integer> amp : amplifiers) {
            threads.add(rounds.submit(amp));
        }

        try {
            for (Future<Integer> f : threads) {
                f = rounds.take();
                f.get();
                //System.out.println("czekamy na future");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

    public static void findMax(HashMap<String, Integer> inputCombinations) {
        int max = Collections.max(inputCombinations.values());
        Optional<String> key = inputCombinations.entrySet().stream().
                filter(e -> e.getValue() == max).
                map(e -> e.getKey()).findFirst();
        System.out.println(key.orElse("not found") + " value: " + inputCombinations.get(key.get()));
        thrusterOutput = inputCombinations.get(key.get());
    }

    private static ArrayList<Integer> readDirections(String file) {

        ArrayList<Integer> result = new ArrayList<>();
        try (Scanner input = new Scanner(new FileInputStream(file), StandardCharsets.UTF_8)) {
            String line = input.nextLine();
            String[] tempcodes = line.split(",");
             for (String s : tempcodes) {
                result.add(Integer.parseInt(s));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }
        return result;
    }

    private static void generateInputs1() {
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
                            inputCombinations.put(combination, 0);
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
        System.out.println("hash " + inputCombinations.size());
    }


}
