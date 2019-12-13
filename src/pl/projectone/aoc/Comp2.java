package pl.projectone.aoc;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class Comp2 implements Callable<Integer> {

    private ArrayList<Long> software;
    private BlockingQueue<Integer> inputs;
    private BlockingQueue<Integer> outputs;
    private Integer result;
    private int baseRef = 0;

    public Comp2(ArrayList<Long> software, BlockingQueue<Integer> inputs, BlockingQueue<Integer> outputs) {

        this.software = software;
        this.inputs = inputs;
        this.outputs = outputs;
        this.result = 0;
    }

    @Override
    public Integer call() {
        int position = 0;
        int input = 0;
        boolean loop = true;
        while (loop) {
            try {
                int[] params = interpreter(software.get(position));
                if (params[4] == 3) {
                    input = inputs.take();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (input == -2000000000) {
                loop = false;
            }
            else {
                int[] params = interpreter(software.get(position));
                position = (int) runInstruction(params, position, input);
                if (position >= software.size()) {
                    loop = false;
                }
            }
        }

        return position;
    }

    private long runInstruction(int[] params, int position, int input) {

        long pos = position;
        long a0 = -1234;
        long a1 = -1234;
        long a2 = -1234;
        long b0 = -1234;
        long b1 = -1234;
        long b2 = -1234;
        long c0 = -1234;
        long c1 = -1234;
        long c2 = -1234;

        if (position < software.size() - 2) {
            a1 = software.get(position+1);
            if (params[2] == 0) a0 = software.get((int) a1);
            if (params[2] == 2) a2 = software.get((int) a1 + baseRef);
        }

        if (params[4] != 3 && params[4] != 4 && params[4] != 9) {
            if (position < software.size() - 3) {
                b1 = software.get(position+2);
                if (params[1] == 0) b0 = software.get((int) b1);
                if (params[1] == 2) b2 = software.get((int) b1 + baseRef);
            }
            if (params[4] != 5 && params[4] != 6) {
                if (position < software.size() - 4) {
                    c1 = software.get(position+3);
                    if (params[0] == 0) c0 = software.get((int) c1);
                    if (params[0] == 2) c2 = software.get((int) c1 + baseRef);
                }
            }
        }


        if (params[0] > 2) {
            System.out.println("Error, param 0 = " + params[0]);
            System.exit(0);
        }
        if (params[1] > 2) {
            System.out.println("Error, param 0 = " + params[1]);
            System.exit(0);
        }
        if (params[2] > 2) {
            System.out.println("Error, param 0 = " + params[2]);
            System.exit(0);
        }

        long param2 = 0;
        long param1 = 0;
        long param0 = 0;


        if (params[4] == 1) {
            pos = position + 4;
            if (params[2] == 0) param2 = a0;
            else if (params[2] == 1) param2 = a1;
            else param2 = a2;

            if (params[1] == 0) param1 = b0;
            else if (params[1] == 1) param1 = b1;
            else param1 = b2;

            if (params[0] == 0) param0 = c1;
            else if (params[0] == 1) param0 = position+3;
            else param0 = c1 + baseRef;

            software.set((int) param0, param2 + param1);
            //System.out.println("Adding: A: " + param2 + " B: " + param1 + " Writing to: " + param0);
        }
        else if (params[4] == 2) {
            pos = position + 4;
            if (params[2] == 0) param2 = a0;
            else if (params[2] == 1) param2 = a1;
            else param2 = a2;

            if (params[1] == 0) param1 = b0;
            else if (params[1] == 1) param1 = b1;
            else param1 = b2;

            if (params[0] == 0) param0 = c1;
            else if (params[0] == 1) param0 = position+3;
            else param0 = c1 + baseRef;

            software.set((int) param0, param2 * param1);
            //System.out.println("Multiplying: A: " + param2 + " B: " + param1 + " Writing to: " + param0);

        }
        else if (params[4] == 3) {
            pos = position + 2;
            if (params[2] == 0) param2 = a1;
            else if (params[2] == 1) param2 = position+1;
            else param2 = a1 + baseRef;

            software.set((int) param2, (long) input);
            //System.out.println("Input: A: " + input + " Writing to: " + param2);

        }
        else if (params[4] == 4) {
            pos = position + 2;
            if (params[2] == 0) param2 = a0;
            else if (params[2] == 1) param2 = a1;
            else param2 = a2;
            try {
                outputs.put((int) param2);
                result = (int) param2;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //System.out.println("Position: " + position + " Output: A: " + param2);
            software.add(param2);
        }
        else if (params[4] == 5) {

            if (params[2] == 0) param2 = a0;
            else if (params[2] == 1) param2 = a1;
            else param2 = a2;

            if (params[1] == 0) param1 = b0;
            else if (params[1] == 1) param1 = b1;
            else param1 = b2;

            if (param2 != 0) pos = param1;
            else pos = position + 3;

            //System.out.println("Position: " + position + " Checking a: " + param2 + " jumping to: " + pos + " baseRef: " + baseRef);
        }
        else if (params[4] == 6) {

            if (params[2] == 0) param2 = a0;
            else if (params[2] == 1) param2 = a1;
            else param2 = a2;

            if (params[1] == 0) param1 = b0;
            else if (params[1] == 1) param1 = b1;
            else param1 = b2;

            if (param2 == 0) pos = param1;
            else pos = position + 3;

            //System.out.println("Position: " + position + " Checking a: " + param2 + " jumping to: " + pos + " baseRef: " + baseRef);

        }
        else if (params[4] == 7) {
            pos = position + 4;

            if (params[2] == 0) param2 = a0;
            else if (params[2] == 1) param2 = a1;
            else param2 = a2;

            if (params[1] == 0) param1 = b0;
            else if (params[1] == 1) param1 = b1;
            else param1 = b2;

            if (params[0] == 0) param0 = c1;
            else if (params[0] == 1) param0 = position+3;
            else param0 = c1 + baseRef;

            long result = 0;
            if (param2 < param1) result = 1;

            software.set((int) param0, result);
            //System.out.println("Comparing: " + param2 + " < " + param1 + " - result = " + result);

        }
        else if (params[4] == 8) {
            pos = position + 4;

            if (params[2] == 0) param2 = a0;
            else if (params[2] == 1) param2 = a1;
            else param2 = a2;

            if (params[1] == 0) param1 = b0;
            else if (params[1] == 1) param1 = b1;
            else param1 = b2;

            if (params[0] == 0) param0 = c1;
            else if (params[0] == 1) param0 = position+3;
            else param0 = c1 + baseRef;

            long result = 0;
            if (param2 == param1) result = 1;

            software.set((int) param0, result);
            //System.out.println("Comparing: " + param2 + " = " + param1 + " - result = " + result);
        }
        else if ((params[4] == 9) && (params[3] == 9)) {
            System.out.println("Code 99 at position: " + pos + " terminating program...");
            pos = software.size();
            try {
                outputs.put(-2000000000);
                System.out.println("From comp - outputs size " + outputs.size());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if (params[4] == 9){
            pos = position + 2;

            if (params[2] == 0) param2 = a0;
            else if (params[2] == 1) param2 = a1;
            else param2 = a2;

            //System.out.println("BaseRef before: " + baseRef + " BaseRef after: " + (baseRef + param2));
            baseRef += param2;
        }
        else {
            System.out.println("Unknown code, exiting program...");
            System.exit(0);
        }
        return pos;
    }

    private int[] interpreter(long codeInt) {
        String code = Long.toString(codeInt);
        int[] codes = {0, 0, 0, 0, 0};
        int offset = 5 - code.length();
        for (int i = 0; i < code.length(); i++) {
            codes[i + offset] = Integer.parseInt(code.substring(i, i+1));
        }
        return codes;
    }

}