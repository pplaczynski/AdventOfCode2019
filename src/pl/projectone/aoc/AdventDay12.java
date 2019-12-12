package pl.projectone.aoc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class AdventDay12 {

    private static ArrayList<Moon> moons = new ArrayList<>();
    private static ArrayList<Moon> original = new ArrayList<>();

    public static void calculateAoC12() {

        //TASK 1 -----------------------------------------------------------

        importMoons();
        System.out.println("Initial state: ------------------------------------");
        for (int i = 0; i < moons.size(); i++) {
            printCurrent(moons.get(i));
        }
        for (int s = 0; s < 1000; s++) {
            runOnce();
            System.out.println();
            /** print all intermediate steps
            for (int i = 0; i < moons.size(); i++) {
                printCurrent(moons.get(i));
            }
             */
        }
        calcEnergy();

        //TASK 2 ----------------------------------------------------------
        moons.clear();
        importMoons();

        int xCounter = 0;
        int yCounter = 0;
        int zCounter = 1;

        boolean xRot = false;
        boolean yRot = false;
        boolean zRot = false;

        while (true) {
            runOnce();
            if (!xRot) {
                xCounter++;
                xRot = compareX();
            }
            if (!yRot) {
                yCounter++;
                yRot = compareY();
            }
            if (!zRot) {
                zCounter++;
                zRot = compareZ();
            }
            if (xRot && yRot && zRot) break;
        }
        System.out.println("X rotations: " + xCounter + " Y rotations: " + yCounter + " Z rotations: " + zCounter);
        BigInteger bigX = new BigInteger(String.valueOf(xCounter));
        BigInteger bigY = new BigInteger(String.valueOf(yCounter));
        BigInteger bigZ = new BigInteger(String.valueOf(zCounter));
        System.out.println("Cycles to reset: " + lowestCommonMultiple(lowestCommonMultiple(bigX, bigY), bigZ));

    }

    private static BigInteger lowestCommonMultiple(BigInteger number1, BigInteger number2) {
        BigInteger gcd = number1.gcd(number2);
        BigInteger abs = number1.multiply(number2).abs();
        return abs.divide(gcd);
    }

    private static boolean compareX() {
        boolean result = true;
        for (int i = 0; i < moons.size(); i++) {
            if (moons.get(i).x != original.get(i).x) result = false;
            if (moons.get(i).xVel != original.get(i).xVel) result = false;
        }
        return result;
    }

    private static boolean compareY() {
        boolean result = true;
        for (int i = 0; i < moons.size(); i++) {
            if (moons.get(i).y != original.get(i).y) result = false;
            if (moons.get(i).yVel != original.get(i).yVel) result = false;
        }
        return result;
    }

    private static boolean compareZ() {
        boolean result = true;
        for (int i = 0; i < moons.size(); i++) {
            if (moons.get(i).z != original.get(i).z) result = false;
            if (moons.get(i).z != original.get(i).z) result = false;
        }
        return result;
    }

    private static void calcEnergy() {
        int kinetic = 0;
        int potential = 0;
        int total = 0;
        for (int i = 0; i < moons.size(); i++) {
            moons.get(i).potential = Math.abs(moons.get(i).x) + Math.abs(moons.get(i).y) + Math.abs(moons.get(i).z);
            moons.get(i).kinetic = Math.abs(moons.get(i).xVel) + Math.abs(moons.get(i).yVel) + Math.abs(moons.get(i).zVel);
        }
        for (int i = 0; i < moons.size(); i++) {
            potential = moons.get(i).potential;
            kinetic = moons.get(i).kinetic;
            total += potential * kinetic;
            System.out.println("Moon" + i + " potential: " + potential + " kinetic: " + kinetic + " total: " + potential*kinetic);
        }
        System.out.println("Total: " + total);
    }

    private static void runOnce() {
        for (int i = 0; i < moons.size(); i++) {
            for (int j = i + 1; j < moons.size(); j++) {
                changeVelocity(moons.get(i), moons.get(j));
            }
            changePosition(moons.get(i));
        }
    }

    private static void printCurrent(Moon m) {
        System.out.println("Moon" + m.id + " x: " + m.x + " y: " + m.y + " z: " + m.z + " vel: x: " + m.xVel + " y: " + m.yVel + " z: " + m.zVel);
    }

    private static void importMoons() {

        moons.add(new Moon(14, 4, 5, 0));
        moons.add(new Moon(12, 10, 8, 1));
        moons.add(new Moon(1, 7, -10, 2));
        moons.add(new Moon(16, -5, 3, 3));

        original.add(new Moon(14, 4, 5, 0));
        original.add(new Moon(12, 10, 8, 1));
        original.add(new Moon(1, 7, -10, 2));
        original.add(new Moon(16, -5, 3, 3));

    }

    private static class Moon {

        public int id;
        public int x;
        public int y;
        public int z;
        public int xVel;
        public int yVel;
        public int zVel;
        public int kinetic;
        public int potential;

        public Moon(int x, int y, int z, int id) {

            this.id = id;
            this.x = x;
            this.y = y;
            this.z = z;
            this.xVel = 0;
            this.yVel = 0;
            this.zVel = 0;
            this.kinetic = 0;
            this.potential = 0;
        }

    }

    private static void changeVelocity(Moon moon1, Moon moon2) {
        if (moon1.x < moon2.x) {
            moon1.xVel++;
            moon2.xVel--;
        }
        else if (moon1.x > moon2.x) {
            moon1.xVel--;
            moon2.xVel++;
        }

        if (moon1.y < moon2.y) {
            moon1.yVel++;
            moon2.yVel--;
        }
        else if (moon1.y > moon2.y) {
            moon1.yVel--;
            moon2.yVel++;
        }

        if (moon1.z < moon2.z) {
            moon1.zVel++;
            moon2.zVel--;
        }
        else if (moon1.z > moon2.z) {
            moon1.zVel--;
            moon2.zVel++;
        }
    }

    private static void changePosition(Moon moon) {

        moon.x += moon.xVel;
        moon.y += moon.yVel;
        moon.z += moon.zVel;
    }

}
