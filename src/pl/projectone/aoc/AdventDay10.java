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
    private static ArrayList<Asteroid> asteroids = new ArrayList<>();
    private static ArrayList<Radials> radials = new ArrayList<>();
    private static int xSize;
    private static int ySize;

    public static void calculateAoC10() {

        readDirections("./inputs/day10/input2.txt");
        //printMap(mainMap);
        createAsteroids();
        //listAsteroids();
        //System.out.println("(" + asteroids.get(0).getX() + "," + asteroids.get(0).getY() + ")");
        calculateRadials();
        Radials best = radials.stream().max((r1, r2) -> r1.visible - r2.visible).get();
        System.out.println(best.xCoor + "," + best.yCoor + " - visible - " + best.visible);
        radials.sort((r1, r2) -> r1.visible - r2.visible);
        System.out.println(radials.get(0).xCoor + "," + radials.get(0).yCoor + " - visible - " + radials.get(0).visible);
        for (int x = 0; x < radials.size(); x++) System.out.println(radials.get(x).xCoor + "," + radials.get(x).yCoor + " - visible - " + radials.get(x).visible);
        System.out.println("-------------------------------------------------------");


        for (int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).calcClosest();
            asteroids.get(i).calcVisible();
            //System.out.println("i " + i + "(" + asteroids.get(i).getX() + "," + asteroids.get(i).getY() + ") - " + asteroids.get(i).getSees());
            //asteroids.get(i).printMyMap();
        }
        //asteroids.get(0).calcClosest();
        //asteroids.get(0).printClosest();
        //asteroids.get(0).calcVisible();
        //asteroids.get(0).printMyMap();

        asteroids.sort((a1, a2) -> a2.getSees() - a1.getSees());

        //asteroids.get(0).printClosest();
        //System.out.println("xsize: " + xSize + " ysize: " + ySize);
        //asteroids.get(0).calcVisible();
        asteroids.get(0).printMyMap();
        //asteroids.get(0).printClosest();
        //asteroids.get(0).calcVisible();
        //printMap(mainMap);

        System.out.println("-----------------------------");


        //Radials r = new Radials(asteroids.get(0).getX(),asteroids.get(0).getY());
        Radials r = new Radials(0,1);
        r.countSin();
        //printMap(r.getNewMap());
        r.orderThem();

        System.out.println(r.getAstr().stream().filter(x -> x.getOrder() == 1).count());

        printMap(r.createMap());

        int maxOrder = r.getAstr().stream().max((a1, a2) -> a1.getOrder() - a2.getOrder()).get().getOrder();

        Map<Integer, Set<Astr>> temp = r.getAstr().stream().collect(groupingBy(Astr::getOrder, toSet()));
        int shoot = 0;
        for (int i = 0; i < temp.size(); i++) {
            System.out.println(i + ":");
            var set = temp.get(i);
            ArrayList<Astr> temp2 = new ArrayList<>();
            set.forEach(s -> temp2.add(s));
            temp2.sort((s1, s2) -> Float.compare(s1.getRad(), s2.getRad()));
            for (int v = 0; v < temp2.size(); v++) {
                temp2.get(v).shotValue = shoot;
                if (shoot == 200) System.out.println(temp2.get(v).getX() + "," + temp2.get(v).getY() + " --------------------");
                shoot++;
            }
        }

        for (int i = 0; i < r.astr.size(); i++) {
            var a = r.astr.get(i);
            System.out.println("(" + a.x + "," + a.y + ") - " + a.shotValue);
        }


        for (int z = 0; z < xSize; z++) {
            for (int z2 = 0; z2 < ySize; z2++) {
                if (!asteroids.get(0).myMap[z][z2].equals(r.finalMap[z][z2])) System.out.println("not ok " + z + "," + z2 + " ---------------------");
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
                    r.visible = (int) r.getAstr().stream().filter(x -> x.getOrder() == 1).count();
                    radials.add(r);
                }
            }
        }
    }

    private static void listAsteroids() {
        for (int i = 0; i < asteroids.size(); i++)
            System.out.println("(" + asteroids.get(i).getX() + "," + asteroids.get(i).getY() + ") - sees: " + asteroids.get(i).getSees());
    }

    private static void createAsteroids() {

        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                if (mainMap[x][y].equals("#")) asteroids.add(new Asteroid(x, y));
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
        System.out.println("pierwsza: " + inputs.get(0).length() + " druga " + inputs.size());
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

    private static class Asteroid {

        private int x;
        private int y;
        private int sees;
        private ArrayList<PointPair> closest;
        private ArrayList<Asteroid> visible;
        private String[][] myMap;

        public Asteroid(int x, int y) {
            this.x = x;
            this.y = y;
            this.sees = 0;
            this.closest = new ArrayList<>();
            this.visible = new ArrayList<>();
            this.myMap = new String[xSize][ySize];
            for (int xd = 0; xd < xSize; xd++){
                for (int yd = 0; yd < ySize; yd++) {
                    myMap[xd][yd] = mainMap[xd][yd];
                }
            }
            myMap[x][y] = "@";
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getSees() {
            return sees;
        }

        public ArrayList<PointPair> getClosest() {
            return closest;
        }

        public void setSees(int sees) {
            this.sees = sees;
        }

        public void calcClosest() {
            int distance = 0;
            for (int a = 0; a < asteroids.size(); a++) {

                Asteroid tempA = asteroids.get(a);
                if ((tempA.getX() == this.x) && (tempA.getY() == this.y)) continue;
                distance = Math.abs(this.x - tempA.getX()) + Math.abs(this.y - tempA.getY());
                closest.add(new PointPair(distance, tempA));
            }
            closest.sort((c1, c2) -> c1.getDistance() - c2.getDistance());
        }

        public void printClosest() {
            for (PointPair p : closest) {
                System.out.println(
                        "Distance - " + p.getDistance() +
                                " point: (" + p.getAsteroid().getX() +
                                "," + p.getAsteroid().getY() + ")");
            }

        }

        public void calcVisible() {
            int vectorX = -100;
            int vectorY = -100;
            int currX = -100;
            int currY = -100;
            int counter = 0;
            for (PointPair a : closest) {

                Asteroid as = a.getAsteroid();
                //System.out.println("checking x: " + as.getX() + ", " + as.getY());

                if (myMap[as.getX()][as.getY()].equals("#")) {
                    visible.add(as);
                    counter += 1;
                    //System.out.println("Added: (" + as.getX() + "," + as.getY() + ") - counter: " + counter);
                    vectorX = as.getX() - this.x;
                    vectorY = as.getY() - this.y;
                    for (int z = (Math.abs(vectorX) < Math.abs(vectorY) ? Math.abs(vectorX) : Math.abs(vectorY)); z > 1 ; z--) {
                        if ((Math.floorMod(vectorX, z) == 0) && (Math.floorMod(vectorY, z) == 0)) {
                            //System.out.println("dividing vector: " + vectorX + "," + vectorY + " by " + z);
                            vectorX = vectorX/z;
                            vectorY = vectorY/z;
                        }
                    }
                    int signX = 1;
                    if  (vectorX < 0) signX = -1;
                    int signY = 1;
                    if  (vectorY < 0) signY = -1;

                    if (vectorX == 0) vectorY = 1 * signY;
                    if (vectorY == 0) vectorX = 1 * signX;
                    if (Math.abs(vectorX) == Math.abs(vectorY)) {
                        vectorX = 1 * signX;
                        vectorY = 1 * signY;
                    }
                    currX = as.getX() + vectorX;
                    currY = as.getY() + vectorY;
                    while ((currX < xSize) && (currY < ySize) && (currX >= 0) && (currY >= 0)) {
                        //if ((currX == 14) && (currY == 7))
                        //    System.out.println("x" + this.x + "y" + this.y + "curr X: " + currX + " curry: " + currY + " vecX: " + vectorX + " vecY" + vectorY);
                        if (myMap[currX][currY].equals("#")) {
                            myMap[currX][currY] = "o";
                        }
                        currX += vectorX;
                        currY += vectorY;
                    }
                }
            }
            sees = visible.size();
        }

        public void printMyMap() {
            printMap(myMap);
            System.out.println("(" + x + "," + y + ") - Visible: " + sees);
        }

        public void calcVisible2() {

            int vectorX = 0;
            int vectorY = 0;
            int currX = this.x;
            int currY = this.y;
            boolean found = false;
            int counter = 0;

            int i;
            int j;

            //up - right
            for (i = this.x; i < xSize; i++) {
                for (j = this.y; j >= 0; j--) {

                    if (myMap[i][j].equals("#") && !found) {
                        counter++;
                        found = true;
                    }
                }
            }



        }

        @Override
        public boolean equals(Object obj) {
            Asteroid a = (Asteroid) obj;
            if ((x == a.getX()) && (y == a.getY())) return true;
            else return false;
        }
    }

    private static class PointPair {
        private int distance;
        private Asteroid asteroid;

        public PointPair(int distance, Asteroid a) {
            this.distance = distance;
            this.asteroid = a;
        }

        public Asteroid getAsteroid() {
            return asteroid;
        }

        public int getDistance() {
            return distance;
        }
    }

    private static class Radials {

        public String[][] newMap = new String[xSize][ySize];
        public Float[][] floatMap = new Float[xSize][ySize];
        public String[][] finalMap = new String[xSize][ySize];
        public int xCoor;
        public int yCoor;
        public ArrayList<Astr> astr = new ArrayList<>();
        public int visible = 0;

        public Radials(int xCoor, int yCoor) {
            this.xCoor = xCoor;
            this.yCoor = yCoor;
            for (int i = 0; i < xSize; i++) {
                for (int j = 0; j < ySize; j++) {
                    newMap[i][j] = ".";
                }
            }
            newMap[xCoor][yCoor] = "@";
        }

        public void countSin() {

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

                        newMap[x][y] = Float.toString(angle);
                        floatMap[x][y] = angle;
                        astr.add(new Astr(x, y, angle, distance));
                    }
                }
            }

            astr.sort((a1, a2) -> (int) (a1.getRad() - a2.getRad()));
            //for (Astr a : astr) System.out.println("(" + a.getX() + "," + a.getY() + ") - angle: " + a.getRad());
        }

        public String[][] createMap() {

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

        public ArrayList<Astr> getAstr() {
            return astr;
        }

        public void orderThem() {

            float r;
            int d;
            int w = 0;

            ArrayList<Astr> temp = new ArrayList<>();

            for (int i = 0; i < astr.size(); i++) {
                r = astr.get(i).getRad();
                temp.add(astr.get(i));
                for (int z = i+1; z < astr.size(); z++) {
                    if (astr.get(z).getRad() == r) {
                        temp.add(astr.get(z));
                        w++;
                        //System.out.println(w);
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
                order = 1;
                i += w;
                w = 0;
            }
            //for (Astr a : astr) System.out.println("(" + a.getX() + "," + a.getY() + ") - angle: " + a.getRad() + " or " + a.getOrder() + " dis " + a.getDistance());
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
