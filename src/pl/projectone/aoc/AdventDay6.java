package pl.projectone.aoc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public abstract class AdventDay6 {

    private static HashMap<String, Planet> planetMap = new HashMap<>();
    private static ArrayList<String> inputs;

    public static void calculateAoC6(String file) {

        readDirections(file);
        createPlanets();
        listPlanets();
        printTotals();
        findPath();
    }

    private static void readDirections(String file) {

        inputs = new ArrayList<>();

        try (Scanner input = new Scanner(new FileInputStream(file), StandardCharsets.UTF_8)) {
            while (input.hasNext()) {
                inputs.add(input.nextLine());
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }
    }

    private static void listPlanets() {
        int totalIndirects = 0;
        int totalDirects = 0;
        planetMap.forEach((k, v) -> System.out.println(v.toString()));
    }

    private static void printTotals() {
        int totalIndirects = 0;
        int totalDirects = 0;
        totalDirects = planetMap.entrySet().stream().mapToInt(e -> e.getValue().getDirectOrbit()).sum();
        totalIndirects = planetMap.entrySet().stream().mapToInt(e -> e.getValue().getIndirectOrbit()).sum();
        System.out.println(
                "Total directs: " + totalDirects +
                " Total indirects: " + totalIndirects +
                " Total: " + (totalDirects + totalIndirects));
    }

    private static void createPlanets() {

        Planet readParent;
        Planet readSatellite;

        for (int i = 0; i < inputs.size(); i++) {
            String[] pl = inputs.get(i).split("\\)");
            if (planetMap.containsKey(pl[0])) {
                readParent = planetMap.get(pl[0]);

                if (planetMap.containsKey(pl[1])) {
                    readSatellite = planetMap.get(pl[1]);
                    readSatellite.setParent(pl[0]);
                    readParent.setDirect(pl[1], readSatellite.getChildren());
                    planetMap.replace(pl[0], readParent);
                    planetMap.replace(pl[1], readSatellite);
                }
                else {
                    readSatellite = new Planet(pl[1], pl[0]);
                    planetMap.put(pl[1], readSatellite);
                    readParent.setDirect(pl[1], 0);
                    planetMap.replace(pl[0], readParent);
                }
            }
            else {
                readParent = new Planet(pl[0], null);

                if (planetMap.containsKey(pl[1])) {
                    readSatellite = planetMap.get(pl[1]);
                    readSatellite.setParent(pl[0]);
                    readParent.setDirect(pl[1], readSatellite.getChildren());
                    planetMap.replace(pl[1], readSatellite);
                }
                else {
                    readSatellite = new Planet(pl[1], pl[0]);
                    readSatellite.setParent(pl[0]);
                    planetMap.put(pl[1], readSatellite);
                    readParent.setDirect(pl[1], 0);
                }
                planetMap.put(pl[0], readParent);
            }
        }
    }

    private static void removePlanet(String name) {
        Planet planet = planetMap.get(name);
        planet.dissapear();
    }

    private static void findPath() {

        ArrayList<String> youPath = new ArrayList<>();
        ArrayList<String> sanPath = new ArrayList<>();

        boolean end = false;
        String position = "YOU";
        String planet;
        //youPath.add(position);

        while (!end) {
            planet = planetMap.get(position).getParent();
            position = planet;
            if (planet == null) {
                end = true;
            }
            else {
                youPath.add(position);
            }
        }

        end = false;
        position = "SAN";
        //sanPath.add(position);

        while (!end) {
            planet = planetMap.get(position).getParent();
            position = planet;
            if (planet == null) {
                end = true;
            }
            else {
                sanPath.add(position);
            }
        }

        System.out.println("you " + youPath.toString());
        System.out.println("san " + sanPath.toString());

        int stepsYou = -1;
        String intersection = "";

        for (String s : youPath) {
            stepsYou += 1;
            System.out.print(stepsYou + "-" + s + ", ");
            if (sanPath.contains(s)) {
                intersection = s;
                break;
            }
        }

        int stepsSan = -1;
        System.out.println();

        for (String s : sanPath) {
            stepsSan += 1;
            System.out.print(stepsSan + "-" + s + ", ");
            if (s.equals(intersection)) {
                break;
            }
        }

        System.out.println(
                "\nYOU -> " + stepsYou + " -> " +
                        intersection + " <- " +
                        stepsSan + " SAN - Total: " +
                        (stepsSan + stepsYou));

    }

    private static class Planet {

        private String name = "";
        private String parent = null;
        private Integer directOrbit = 0;
        private Integer indirectOrbit = 0;
        private ArrayList<String> satelites = new ArrayList<>();

        public Planet(String name, String parent) {
            this.name = name;
            if (parent != null) this.parent = parent;
        }

        public Integer getChildren() {
               return directOrbit + indirectOrbit;
        }

        public void setDirect(String satelite, Integer children) {
            satelites.add(satelite);
            directOrbit += 1;
            indirectOrbit += children;
            setIndirect(children + 1);
        }

        public void setIndirect(Integer indirect) {
            if (parent != null) {
                Planet origin = planetMap.get(parent);
                origin.increaseIndirect(indirect);
                origin.setIndirect(indirect);
            }
        }

        public void increaseIndirect(Integer indirect) {
            indirectOrbit += indirect;
        }

        public void changeParent(String name) {
            Planet oldParent = planetMap.get(parent);
            oldParent.removeSatelite(this.name, directOrbit + indirectOrbit);
            if (name != null) {
                Planet newParent = planetMap.get(name);
                newParent.setDirect(this.name, directOrbit + indirectOrbit);
                parent = name;
            }
            else parent = null;
        }

        public void removeSatelite(String satelite, Integer children) {
            directOrbit -= 1;
            indirectOrbit -= children;
            boolean success = satelites.remove(satelite);
            if (!success) System.out.println("Problem with removing satellite from list");
            removeChildren(children + 1);
        }

        public void removeChildren(Integer children) {
            if (parent != null) {
                Planet origin = planetMap.get(parent);
                origin.removeChildren(children);
            }
        }

        public void dissapear() {
            changeParent(null);
            for (String planet : satelites) removePlanet(planet);
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public String getParent() {
            return parent;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            String par = "null";
            if (parent != null) par = parent;
            return "Planet: " + name
                    + ", Parent: " + parent
                    + ", Indirect: " + indirectOrbit
                    + ", Direct: " + directOrbit
                    + " - " + satelites.toString();
        }

        public int getTotal() {
            return directOrbit + indirectOrbit;
        }

        public Integer getDirectOrbit() {
            return directOrbit;
        }

        public Integer getIndirectOrbit() {
            return indirectOrbit;
        }
    }
}
