package pl.projectone.aoc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class RobotDay11 implements Callable<Integer> {

    public int vectorX;
    public int vectorY;
    public int currX;
    public int currY;
    private int steps;
    private int paint = 0;
    private int direction = 0;
    private BlockingQueue<Integer> inputs;
    private BlockingQueue<Integer> outputs;
    private String[][] paintMap;
    private int[][] visitMap;
    private final String BLACK = ".";
    private final String WHITE = "#";
    private String paintBelow;

    public RobotDay11(BlockingQueue<Integer> inputs, BlockingQueue<Integer> outputs, int x, int y, String[][] paintMap, int[][] visitMap) {

        this.inputs = inputs;
        this.outputs = outputs;
        this.vectorX = 0;
        this.vectorY = -1;
        this.currX = x;
        this.currY = y;
        this.paintMap = paintMap;
        this.visitMap = visitMap;
        this.steps = 0;
    }

    public Integer call() {

        boolean loop = true;
        while (loop) {
            try {

                int newVecX;
                int newVecY;

                paintBelow = paintMap[currX][currY]; // check what's below
                if (paintBelow.equals(BLACK)) outputs.put(0); //send value below to computer
                else outputs.put(1);
                paint = inputs.take(); // check how to paint
                if (paint == -2000000000) { //check if program already finished
                    loop = false;
                    continue;
                }

                if (paint == 0) paintMap[currX][currY] = BLACK; // paint below
                else if (paint == 1) paintMap[currX][currY] = WHITE;
                else System.out.println("Wrong paint - " + paint);

                direction = inputs.take(); // check where to turn
                if (direction == 0) { // turn left
                    newVecX = vectorY;;
                    newVecY = -1*vectorX;
                    vectorY = newVecY;
                    vectorX = newVecX;
                }
                else if (direction == 1){ //turn right
                    newVecX = -1*vectorY;
                    newVecY = vectorX;
                    vectorY = newVecY;
                    vectorX = newVecX;
                }
                else System.out.println("Wrong direction - " + direction);
                currX += vectorX;
                currY += vectorY;
                //if (direction == 1) System.out.println("New position - (" + currX + "," + currY + ")");
                visitMap[currX][currY] = visitMap[currX][currY]+1;
                steps++;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return steps;
    }
}
