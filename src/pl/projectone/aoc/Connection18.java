package pl.projectone.aoc;

import java.util.ArrayList;

public class Connection18 {

    public String name;
    public int distance;
    public ArrayList<String> doors = new ArrayList<>();

    public Connection18(String name, int distance, ArrayList<String> doors) {
        this.name = name;
        this.distance = distance;
        this.doors.addAll(doors);
    }
}
