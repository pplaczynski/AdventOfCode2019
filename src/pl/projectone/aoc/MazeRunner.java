package pl.projectone.aoc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class MazeRunner implements Callable<Integer> {

    public int newX;
    public int newY;
    public int currX;
    public int currY;
    private int steps;
    private int state = 0;
    private int direction = 0;
    private BlockingQueue<Integer> inputs;
    private BlockingQueue<Integer> outputs;
    private String[][] paintMap;

    public MazeRunner(BlockingQueue<Integer> inputs, BlockingQueue<Integer> outputs, int x, int y, String[][] paintMap) {

        this.inputs = inputs;
        this.outputs = outputs;
        this.newX = x;
        this.newY = y;
        this.currX = x;
        this.currY = y;
        this.paintMap = paintMap;
        this.steps = 0;
    }

    public Integer call() {

        int move = -1;
        boolean loop = true;
        while (loop) {
            try {

                newX = currX;
                newY = currY;

                //if (move == -1) paintMap[currY][currX] = "X";
                move = ThreadLocalRandom.current().nextInt(1, 5);
                if (move == 1) {
                    newY = currY - 1;
                    //System.out.println(currX + "," + currY + " - moving north to: " + newX + "," + newY);
                }
                if (move == 2) {
                    newY = currY + 1;
                    //System.out.println(currX + "," + currY + " - moving south to: " + newX + "," + newY);
                }
                if (move == 3) {
                    newX = currX - 1;
                    //System.out.println(currX + "," + currY + " - moving west to: " + newX + "," + newY);
                }
                if (move == 4) {
                    newX = currX + 1;
                    //System.out.println(currX + "," + currY + " - moving east to: " + newX + "," + newY);
                }
                outputs.put(move);

                state = inputs.take();
                if (state == -2000000000) { //check if program already finished
                    loop = false;
                    continue;
                }
                if (state == 0) { // wall
                    paintMap[newY][newX] = "#";
                    //System.out.println("Got wall at: " + newX + "," + newY);
                }
                else if (state == 1) { //move
                    if (!paintMap[newY][newX].equals("X")) paintMap[newY][newX] = ".";
                    currX = newX;
                    currY = newY;
                    //System.out.println("Moving to: " + newX + "," + newY);
                }
                else if (state == 2) { // oxygen tank
                    paintMap[newY][newX] = "O";
                    System.out.println("--------- found tank at: " + newX + "," + newY);
                    outputs.put(-2000000000);
                    loop = false;
                }
                else System.out.println("Wrong direction - " + direction);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return steps;
    }
}
