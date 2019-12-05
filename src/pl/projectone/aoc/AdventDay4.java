package pl.projectone.aoc;

import java.util.ArrayList;

public abstract class AdventDay4 {

    private static ArrayList<Integer> combs = new ArrayList<>();

    public static void calculateAoC4(int min, int max) {

        combs = combinations(min, max);
        System.out.println(combs.size());

    }

    public static ArrayList<Integer> combinations(int min, int max) {

        int minBound = Integer.parseInt(Integer.toString(min).substring(0, 1));
        int maxBound = Integer.parseInt(Integer.toString(max).substring(0, 1));

        ArrayList<Integer> comb = new ArrayList<Integer>();

        for (int i = minBound; i <= maxBound; i++) {
            for (int j = 0; j <= 9; j++) {
                if (j < i) continue;
                for (int k = 0; k <= 9; k++) {
                    if (k < j) continue;
                    for (int l = 0; l <= 9; l++) {
                        if (l < k) continue;
                        for (int m = 0; m <= 9; m++) {
                            if (m < l) continue;
                            for (int n = 0; n <= 9; n++) {
                                if (n < m) continue;

                                String combValue = "";
                                combValue =
                                        Integer.toString(i) +
                                                Integer.toString(j) +
                                                Integer.toString(k) +
                                                Integer.toString(l) +
                                                Integer.toString(m) +
                                                Integer.toString(n);


                                int valueInt = Integer.parseInt(combValue);

                                if ((valueInt > max) || (valueInt < min) ) continue;

                                int repeatNo = 1;
                                int tempRep = 1;
                                int d = 0;
                                for (int z = 0; z < combValue.length()-1; z++) {
                                    if (combValue.substring(z, z+1).equals(combValue.substring(z+1, z+2))) {
                                        tempRep += 1;
                                        if (tempRep > repeatNo) repeatNo = tempRep;
                                        if (z == 4 && tempRep == 2) d += 1;
                                    }
                                    else {
                                        if (tempRep == 2) d += 1;

                                        tempRep = 1;
                                    }
                                }
                                System.out.println(combValue + " repeats: " + repeatNo + " d" + d);

                                if (d > 0) {
                                    comb.add(valueInt);
                                    System.out.println(combValue + " - " + comb.size());
                                }
                            }
                        }
                    }
                }
            }
        }
        return comb;
    }
}
