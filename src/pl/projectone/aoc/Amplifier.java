package pl.projectone.aoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class Amplifier implements Callable<Integer> {

    private int phase;
    private int id;
    private boolean looped;
    private String fullKey;
    private ArrayList<Integer> software;
    private BlockingQueue<Integer> inputs;
    private BlockingQueue<Integer> outputs;
    private HashMap<String, Integer> finalOutput;
    private static Integer result;

    public Amplifier(int phase,
                     int id,
                     boolean looped,
                     String fullKey,
                     ArrayList<Integer> software,
                     BlockingQueue<Integer> inputs,
                     BlockingQueue<Integer> outputs,
                     HashMap<String, Integer> finalOutput) {

        this.phase = phase;
        this.id = id;
        this.looped = looped;
        this.fullKey = fullKey;
        this.software = software;
        this.inputs = inputs;
        this.outputs = outputs;
        this.finalOutput = finalOutput;
        this.result = 0;
    }

    @Override
    public Integer call() {
        System.out.println("Thread: " + id + " started");
        Integer input = phase;
        int position = 0;
        boolean loop = true;
        while (loop) {
            if (position > 0) {
                try {
                    int[] params = interpreter(software.get(position));
                    if (params[4] == 3) {
                        input = inputs.take();
                        //System.out.println("Thread " + id + " takes from queue" + input);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (input == -2000000000) {
                loop = false;
            }
            else {
                //System.out.println("Thread " + id + " Position: " + position + " instr: " + software.get(position));
                int[] params = interpreter(software.get(position));
                position = runInstruction(params, position, input);
                if (position >= software.size()) {
                    System.out.println("Thread ID close: " + id);
                    //System.out.println("Pozycja zwrócona: " + position);
                    //System.out.println("software " + software);
                    //System.out.println("zatrzymuję");
                    loop = false;
                }
            }
        }
        System.out.println("zamykam " + id);
        return 0;
    }

    private int runInstruction(int[] params, int position, int input) {

        int pos = position;

        int a = 0;
        int b = 0;

        if (params[4] != 9) {
            if (position < software.size() - 1) {
                if (params[2] == 0) {
                    if (software.get(position + 1) < software.size()) {
                        a = software.get(software.get(position + 1));
                    }
                }
                else if (params[2] == 1) {
                    if (position + 1 < software.size()) {
                        a = software.get(position + 1);
                    }
                }
                else {
                    System.exit(0);
                }
            }
        }

        if ((params[4] == 1) ||
                (params[4] == 2) ||
                (params[4] == 5) ||
                (params[4] == 6) ||
                (params[4] == 7) ||
                (params[4] == 8)) {

            //System.out.println(params[4] + " reading b");

            if (position < software.size() - 2) {
                if (params[1] == 0) {
                    if (software.get(position + 2) < software.size()) {
                        b = software.get(software.get(position + 2));
                    }
                }
                else if (params[1] == 1) {
                    if (position + 2 < software.size()) {
                        b = software.get(position + 2);
                    }
                }
                else {
                    System.out.println("Error: Param 2 = " + params[1]);
                    System.exit(0);
                }
            }
        }

        //System.out.println("Sparwdzam params: " + params[0]+params[1]+params[2]+params[3]+params[4]);
        if (params[0] != 0) System.out.println("Error: Param 1 = " + params[0]);
        if (params[4] == 1) {
            pos = position + 4;
            software.set(software.get(position + 3), a + b);
            //System.out.println("sprawdzam param 4");
        }
        else if (params[4] == 2) {
            pos = position + 4;
            software.set(software.get(position + 3), a * b);
        }
        else if (params[4] == 3) {
            software.set(software.get(position+1), input);
            if (id == 1) System.out.println("Got input: " + input);
            pos = position + 2;
        }
        else if (params[4] == 4) {
            pos = position + 2;
            System.out.println("Thread " + id + " puts " + a);
            try {
                outputs.put(a);
                result = a;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (id == 5) finalOutput.replace(fullKey, a);

        }
        else if (params[4] == 5) {
            if (a == 0) pos = position + 3;
            else pos = b;
            //System.out.println("Thread " + id + " Position 28: " + a + " instr: " + software.get(position));
        }
        else if (params[4] == 6) {
            if (a == 0) pos = b;
            else pos = position + 3;
            //System.out.println("Thread " + id + " Position 28: " + b);
        }
        else if (params[4] == 7) {
            pos = position + 4;
            int result = 0;
            if (a < b) result = 1;
            software.set(software.get(position + 3), result);
        }
        else if (params[4] == 8) {
            pos = position + 4;

            int result = 0;
            if (a == b) result = 1;
            software.set(software.get(position + 3), result);
        }
        else if ((params[4] == 9) && (params[3] == 9)) {
            //System.out.println("Code 99 at position: " + pos + " terminating program...");
            try {
                outputs.put(-2000000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pos = software.size();
        }
        else {
            System.out.println("Unknown code, exiting program...");
            System.exit(0);
        }
        //System.out.println("zwracam pozycję " + pos);
        if (id == 1) System.out.println("Thread " + id + " - " + software.toString());
        return pos;
    }

    private int[] interpreter(int codeInt) {
        String code = Integer.toString(codeInt);
        int[] codes = {0, 0, 0, 0, 0};
        int offset = 5 - code.length();
        for (int i = 0; i < code.length(); i++) {
            codes[i + offset] = Integer.parseInt(code.substring(i, i+1));
        }
        return codes;
    }
}
