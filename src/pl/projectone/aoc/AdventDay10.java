package pl.projectone.aoc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

public abstract class AdventDay10 {

    private static String[][] mainMap;
    //private static ArrayList<Asteroid> asteroids = new ArrayList<>();
    private static ArrayList<Radials> radials = new ArrayList<>();
    private static int xSize;
    private static int ySize;

    public static void calculateAoC10(String file) {

        readDirections(file);
        calculateRadials();
        Radials best = radials.stream().max((r1, r2) -> r1.visible - r2.visible).get();
        System.out.println(best.xCoor + "," + best.yCoor + " - visible - " + best.visible);
        printMap(best.createMap());
        System.out.println("-------------------------------------------------------");

        Map<Integer, Set<Astr>> temp = best.astr.stream().collect(groupingBy(Astr::getOrder, toSet()));
        int shoot = 0;
        for (int i = 0; i < temp.size(); i++) {
            //System.out.println(i + ":");
            var set = temp.get(i);
            ArrayList<Astr> temp2 = new ArrayList<>();
            set.forEach(s -> temp2.add(s));
            temp2.sort((s1, s2) -> Float.compare(s1.getRad(), s2.getRad()));
            for (int v = 0; v < temp2.size(); v++) {
                temp2.get(v).shotValue = shoot;
                if (shoot == 200) {
                    System.out.println("Task2 answer: coordinates - (" +
                            temp2.get(v).getX() + "," + temp2.get(v).getY() + ") - " +
                            (temp2.get(v).getX()*100+temp2.get(v).getY()));
                }
                shoot++;
            }
        }
    }

    private static void calculateRadials() {

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                if (mainMap[i][j].equals("#")) {
                    Radials r = new Radials(i,j);
                    r.countSin();
                    r.orderThem();
                    r.visible = (int) r.astr.stream().filter(x -> x.getOrder() == 1).count();
                    radials.add(r);
                }
            }
        }
    }

    private static void readDirections(String file) {

        ArrayList<String> inputs = new ArrayList<>();

        try (Scanner input = new Scanner(new FileInputStream(file), StandardCharsets.UTF_8)) {
            while (input.hasNext()) {
                inputs.add(input.nextLine());
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }
        mainMap = new String[inputs.get(0).length()][inputs.size()];

        xSize = inputs.get(0).length();
        ySize = inputs.size();

        for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                mainMap[j][i] = inputs.get(i).substring(j, j+1);
            }
        }

    }

    private static void printMap(String[][] map) {
        System.out.print("  ");
        for (int l = 0; l < xSize; l++) {
            if (l < 10) System.out.print(" ");
            System.out.print(l);
        }
        System.out.println();
        for (int y = 0; y < ySize; y++) {
            if (y < 10) System.out.print(" ");
            System.out.print(y);
            for (int x = 0; x < xSize; x++) {
                System.out.print(" ");
                System.out.print(map[x][y]);
            }
            System.out.println();
        }

    }

    private static class Radials {

        //public String[][] newMap = new String[xSize][ySize];
        //public Float[][] floatMap = new Float[xSize][ySize];
        public String[][] finalMap = new String[xSize][ySize];
        public int xCoor;
        public int yCoor;
        public ArrayList<Astr> astr = new ArrayList<>();
        public int visible = 0;

        public Radials(int xCoor, int yCoor) {
            this.xCoor = xCoor;
            this.yCoor = yCoor;
        }

        public void countSin() { //counts angles for every asteroid from this asteroid

            float angle;
            int distance;

            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {

                    if (mainMap[x][y].equals("#")) {

                        angle = (float) Math.toDegrees(Math.atan2(y - yCoor, x - xCoor));
                        distance = Math.abs(x - xCoor) + Math.abs(y - yCoor);
                        if (angle >= 0) angle += 90;
                        if (angle < 0) angle += 450;
                        if (angle >= 360) angle -= 360;
                        astr.add(new Astr(x, y, angle, distance));
                    }
                }
            }

            astr.sort((a1, a2) -> Float.compare(a1.getRad(), a2.getRad()));
        }

        public String[][] createMap() { //creates a printable map of this asteroid view

            String symbol = ".";
            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    finalMap[x][y] = symbol;
                }
            }
            for (int i = 0; i < astr.size(); i++) {
                var a = astr.get(i);
                if (a.getDistance() == 0) {
                    finalMap[a.getX()][a.getY()] = "@";
                }
                else if (a.getOrder() == 1) finalMap[a.getX()][a.getY()] = "#";
                else finalMap[a.getX()][a.getY()] = "o";
            }
            return finalMap;
        }

        public void orderThem() { // orders visibility rank based on the same angle and then distance. 1 is visible, others are not

            float r;
            int w = 0;

            ArrayList<Astr> temp = new ArrayList<>();

            for (int i = 0; i < astr.size(); i++) {
                r = astr.get(i).getRad();
                temp.add(astr.get(i));
                for (int z = i+1; z < astr.size(); z++) {
                    if (Float.compare(astr.get(z).getRad(), r) == 0) {
                        temp.add(astr.get(z));
                        w++;
                    }
                }
                temp.sort((a, b) -> a.getDistance() - b.getDistance());
                int order = 1;
                for (int g = 0; g < temp.size(); g++) {
                    if ((temp.get(g).getX() == xCoor) && (temp.get(g).getY() == yCoor)) {
                        temp.get(g).setOrder(0);
                        continue;
                    }
                    temp.get(g).setOrder(order);
                    order++;
                }
                temp.clear();
                i += w; // increases loop counter by all previously found asteroids with the same angle
                w = 0;
            }
        }
    }

    private static class Astr {

        private int x;
        private int y;
        private float rad;
        private int order;
        private int distance;
        public int shotValue = 0;

        public Astr(int x, int y, float rad, int distance) {
            this.x = x;
            this.y = y;
            this.rad = rad;
            this.order = 1;
            this.distance = distance;
        }

        public int getDistance() {
            return distance;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public float getRad() {
            return rad;
        }
    }

}
