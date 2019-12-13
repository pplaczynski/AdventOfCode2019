package pl.projectone.aoc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class Arkanoid implements Callable<Integer> {


    public int currX;
    public int currY;
    private int steps;
    private int paint = 0;
    private BlockingQueue<Integer> inputs;
    private BlockingQueue<Integer> outputs;
    private String[][] paintMap;
    private int ballX;
    private int paddleX;


    public Arkanoid(BlockingQueue<Integer> inputs, BlockingQueue<Integer> outputs, String[][] paintMap) {

        this.inputs = inputs;
        this.outputs = outputs;
        this.currX = 0;
        this.currY = 0;
        this.steps = 0;
        this.paintMap = paintMap;
        this.ballX = 0;
    }

    public Integer call() {

        boolean loop = true;
        boolean inital = true;
        int blocks = 261;

        while (loop) {
            try {

                currX = inputs.take(); // get x coordinate
                if (currX == -2000000000) { //check if program already finished
                    System.out.println("Got end command - ending robot");
                    loop = false;
                    break;
                }
                currY = inputs.take(); // get y coordinate
                if (currY == -2000000000) { //check if program already finished
                    System.out.println("Got end command - ending robot");
                    loop = false;
                    break;
                }
                paint = inputs.take(); // check how to paint
                if (paint == -2000000000) { //check if program already finished
                    System.out.println("Got end command - ending robot");
                    loop = false;
                    break;
                }
                if ((currX == -1) && (currY == 0)) {
                    System.out.println("Current score: " + paint + " blocks left: " + blocks);
                    blocks--;
                    if (blocks <= 0) {
                        outputs.put(-2000000000);
                        break;
                    }
                }
                else {
                    if (paint == 0) {
                        paintMap[currY][currX] = " ";
                    } else if (paint == 1) {
                        paintMap[currY][currX] = "+";
                    } else if (paint == 2) {
                        paintMap[currY][currX] = "=";
                        steps++;
                    } else if (paint == 3) {
                        paintMap[currY][currX] = "T";
                        paddleX = currX;
                    } else if (paint == 4) {
                        paintMap[currY][currX] = "o";
                        ballX = currX;
                        if (inital) {
                            outputs.put(0);
                            inital = false;
                        } else {
                            if (ballX < paddleX) outputs.put(-1);
                            else if (ballX > paddleX) outputs.put(1);
                            else outputs.put(0);

                        }
                    } else System.out.println("Wrong paint code - " + paint);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return steps;
    }
}
