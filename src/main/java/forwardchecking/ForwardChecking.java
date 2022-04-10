package forwardchecking;

import data_loader.DataLoader;

import java.util.ArrayList;

public class ForwardChecking {

    public static int gridsCount = 0;
    public static int count = 0;
    public static boolean isHeuristicMostFrequent = false;

    public static void startForwardChecking(ForwardCheckingBase forwardChecking) {
        forwardChecking(forwardChecking);
        System.out.println("Created grids: " + gridsCount);
        System.out.println("Solutions found: " + count);
    }

    public static void forwardChecking(ForwardCheckingBase forwardChecking) {
        if (forwardChecking.isFilled()) {
            count++;
            System.out.println("solution");
            int ind = 0;
            for (var e : forwardChecking.grid) {
                if (ind % forwardChecking.size == 0) {
                    System.out.println();
                }
                System.out.print(e + " ");
                ind++;
            }
            System.out.println();
            return;

        }

        if (forwardChecking.isFieldEmpty()) {
            forwardChecking.adjustDomain();

            if (isHeuristicMostFrequent) {
                var frequent = DataLoader.getMostFrequent(forwardChecking.grid);
                var temp = new ArrayList<Integer>();
                for (var e : forwardChecking.domain) {
                    if (!frequent.contains(e)) {
                        frequent.add(e);
                    }
                }
                forwardChecking.domain = frequent;
            }

            for (Integer value : forwardChecking.domain) {
                gridsCount++;
                forwardChecking.setValue(value);

                if (forwardChecking.checkConstraints()) {
                    forwardChecking(forwardChecking.createCopyAndIncreaseIndex());
                }
            }
        } else {
            forwardChecking(forwardChecking.createCopyAndIncreaseIndex());
        }
    }
}
