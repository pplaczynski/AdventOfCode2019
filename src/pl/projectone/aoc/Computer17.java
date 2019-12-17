package pl.projectone.aoc;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class Computer17 {

    private static ArrayList<Integer> software;
    private static int[][] map2;
    private static int result;
    private static int baseRef = 0;
    private static int row = 1;
    private static int collumn = 1;
    private static BlockingQueue<Integer> c = new ArrayBlockingQueue<Integer>(10);


    public static void run(ArrayList<Integer> soft, int[][] map, BlockingQueue<Integer> codes) {
        software = soft;
        map2 = map;
        c = codes;
        int position = 0;
        int input = 0;
        boolean loop = true;
        while (loop) {
            try {
                int[] params = interpreter(software.get(position));
                if (params[4] == 3) {
                    input = c.take();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int[] params = interpreter(software.get(position));
            position = (int) runInstruction(params, position, input);
            if (position >= software.size()) {
                loop = false;
            }
        }
    }

    private static long runInstruction(int[] params, int position, int input) {

        int pos = position;
        int a0 = -1234;
        int a1 = -1234;
        int a2 = -1234;
        int b0 = -1234;
        int b1 = -1234;
        int b2 = -1234;
        int c0 = -1234;
        int c1 = -1234;
        int c2 = -1234;

        if (position < software.size() - 2) {
            a1 = software.get(position+1);
            if (params[2] == 0) a0 = software.get(a1);
            if (params[2] == 2) a2 = software.get(a1 + baseRef);
        }

        if (params[4] != 3 && params[4] != 4 && params[4] != 9) {
            if (position < software.size() - 3) {
                b1 = software.get(position+2);
                if (params[1] == 0) b0 = software.get(b1);
                if (params[1] == 2) b2 = software.get(b1 + baseRef);
            }
            if (params[4] != 5 && params[4] != 6) {
                if (position < software.size() - 4) {
                    c1 = software.get(position+3);
                    if (params[0] == 0) c0 = software.get(c1);
                    if (params[0] == 2) c2 = software.get(c1 + baseRef);
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

        int param2 = 0;
        int param1 = 0;
        int param0 = 0;


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

            software.set(param2, input);
            //System.out.println("Input: A: " + input + " Writing to: " + param2);

        }
        else if (params[4] == 4) {
            pos = position + 2;
            if (params[2] == 0) param2 = a0;
            else if (params[2] == 1) param2 = a1;
            else param2 = a2;
            result = param2;
            if (param2 < 100000) System.out.print(Character.toString((char) param2));
            else System.out.println("Robot has collected: " + param2 + " dust");
            /**
             * Part 1
            if (param2 > 10) {
                map2[row][collumn] = param2;
                collumn++;
            }
            else {
                collumn = 1;
                row++;
            }
            */
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

            int result = 0;
            if (param2 < param1) result = 1;

            software.set(param0, result);
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

            int result = 0;
            if (param2 == param1) result = 1;

            software.set(param0, result);
            //System.out.println("Comparing: " + param2 + " = " + param1 + " - result = " + result);
        }
        else if ((params[4] == 9) && (params[3] == 9)) {
            System.out.println("Code 99 at position: " + pos + " terminating program...");
            pos = software.size();
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

    private static int[] interpreter(long codeInt) {
        String code = Long.toString(codeInt);
        int[] codes = {0, 0, 0, 0, 0};
        int offset = 5 - code.length();
        for (int i = 0; i < code.length(); i++) {
            codes[i + offset] = Integer.parseInt(code.substring(i, i+1));
        }
        return codes;
    }

}
