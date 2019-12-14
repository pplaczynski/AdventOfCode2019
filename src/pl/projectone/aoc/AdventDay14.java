package pl.projectone.aoc;

import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public abstract class AdventDay14 {

    private static HashMap<String, Recepie> recepies = new HashMap<>();
    private static HashMap<String, Long> ing = new HashMap<>();
    private static HashMap<String, Long> rests = new HashMap<>();
    private static long ore = 0L;

    public static void calculateAoC14() {

        long target = 1000000000000L;
        boolean war = false;
        int counter = 3400000;

        while (!war) {
            recepies.clear();
            ing.clear();
            rests.clear();
            readDirections("./inputs/day14/input1.txt");
            calculateIng("FUEL", counter);
            calcORE();
            System.out.println("Found: " + ore + " for counter: " + counter);
            counter++;
            if (ore >= target) break;
            ore = 0L;
        }
        recepies.clear();
        ing.clear();
        rests.clear();
        readDirections("./inputs/day14/input3.txt");

        ore = 0L;
        calculateIng("FUEL", 1);
        calcORE();
        System.out.println("Total ore = " + ore);
    }

    private static void calcORE() {

        ing.forEach((k, v) -> {
            if (recepies.get(k).ingredients.get(0).getKey().equals("ORE")) { // count only final ones with ORE
                long multiplier = needed(recepies.get(k).quantity, v);
                ore += multiplier * recepies.get(k).ingredients.get(0).getValue() / recepies.get(k).quantity;
            }
        });
    }

    private static void calculateIng(String ingredient, long q) {

        long quantity = q;
        long needed = 0;
        Recepie r = recepies.get(ingredient);
        if (rests.get(r.id) >= quantity) { // if I need 1 and rest is 8 than take 1 from rests and you need 0;(
            rests.replace(r.id, rests.get(r.id) - quantity);
        }
        else { // if we used all rest then 0 it
            quantity -= rests.get(r.id);
            needed = needed(r.quantity, quantity);
            rests.replace(r.id, needed - quantity);
        }
        if (r.ingredients.get(0).getKey().equals("ORE")) {
            if (ing.containsKey(r.id)) ing.replace(r.id, needed + ing.get(r.id));
            else ing.put(r.id, needed);
        }
        else {
            if (ing.containsKey(r.id)) ing.replace(r.id, needed + ing.get(r.id));
            else ing.put(r.id, needed);

            for (int i = 0; i < r.ingredients.size(); i++) {

                calculateIng(r.ingredients.get(i).getKey(), r.ingredients.get(i).getValue() * needed/r.quantity);
            }
        }
    }

    private static long needed(long minimal, long wanted) {

        if (minimal >= wanted) return minimal;
        else {
            long base = wanted / minimal;
            long rest = Math.floorMod(wanted, minimal);
            if (rest > 0) rest = 1;
            return (base + rest) * minimal;
        }
    }

    private static void readDirections(String file) {

        try (Scanner input = new Scanner(new FileInputStream(file), StandardCharsets.UTF_8)) {
            while (input.hasNextLine()) {
                String line = input.nextLine();
                String main = line.substring(line.indexOf("=") + 3).stripLeading();
                String[] tempcodes = line.substring(0, line.indexOf("=") - 1).split(",");
                Recepie r = new Recepie(main.substring(main.indexOf(" ")+1), Long.parseLong(main.substring(0, main.indexOf(" "))));

                for (String s : tempcodes) {
                    String temp = s.stripLeading();
                    r.addIngredient(new Pair(temp.substring(temp.indexOf(" ")+1), Long.parseLong(temp.substring(0, temp.indexOf(" ")))));
                }
                recepies.put(r.id, r);
                rests.put(r.id, 0L);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }
    }

    private static class Recepie {

        String id;
        Long quantity;
        ArrayList<Pair<String, Long>> ingredients = new ArrayList<>();

        public Recepie(String id, Long quantity) {
            this.id = id;
            this.quantity = quantity;
        }

        public void addIngredient(Pair<String, Long> ingredient) {
            ingredients.add(ingredient);
        }
    }
}
