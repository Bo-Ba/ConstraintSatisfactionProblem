package forwardchecking;

import data_loader.DataLoader;

import java.time.Duration;
import java.time.Instant;

public class ForwardChecking {

    public static int gridsCount = 0;
    public static int count = 0;
    public static int insertCount = 0;
    public static boolean isHeuristicMostFrequent = false;
    public static boolean isHeuristicMostFilled = false;

    public static void startForwardChecking(ForwardCheckingBase forwardChecking) {
        Instant start = Instant.now();
        forwardChecking(forwardChecking);
        Instant finish = Instant.now();
        System.out.println("Created grids: " + forwardChecking.gridsCount);
        System.out.println("Solutions found: " + count);
        System.out.println("Insertions made: " + insertCount);
        System.out.println("Time elapsed: " + Duration.between(start, finish).toMillis());
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
                for (var e : forwardChecking.domain) {
                    if (!frequent.contains(e)) {
                        frequent.add(e);
                    }
                }
                forwardChecking.domain = frequent;
            }

            if(isHeuristicMostFilled) {
                forwardChecking.index = DataLoader.getIndexToInsert(forwardChecking.grid, forwardChecking.filedFieldsInRowNum, forwardChecking.filedFieldsInColNum, forwardChecking.size);
            }
            for (Integer value : forwardChecking.domain) {
                forwardChecking.setValue(value);
                insertCount++;

                if (forwardChecking.checkConstraints()) {
                    forwardChecking(forwardChecking.createCopy());
                    forwardChecking.gridsCount++;
                }
            }
        } else {
            forwardChecking(forwardChecking.createCopyAndIncreaseIndex());
        }
    }
}
