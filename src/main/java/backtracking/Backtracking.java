package backtracking;

import data_loader.DataLoader;

import java.time.Duration;
import java.time.Instant;


public class Backtracking {
    public static int count = 0;
    static int gridsCount = 0;
    static int insertCount = 0;
    public static boolean isHeuristicMostFrequent = false;
    public static boolean isHeuristicMostFilled = false;

    public static void startBacktracking(BacktrackingBase backtracking) {
        Instant start = Instant.now();
        backtracking(backtracking);
        Instant finish = Instant.now();

        System.out.println("Created grids: " + backtracking.gridsCount);
        System.out.println("Found solutions: " + count);
        System.out.println("FInsertions made: " + insertCount);
        System.out.println("Time elapsed " + Duration.between(start, finish).toMillis());
    }

    public static void backtracking(BacktrackingBase backtracking) {
        if (backtracking.isFilled()) {
            count++;
            System.out.println("solution");
            int ind = 0;
            for (var e : backtracking.grid) {
                if (ind % backtracking.size == 0) {
                    System.out.println();
                }
                System.out.print(e + " ");
                ind++;
            }
            System.out.println();
            return;
        }

        if (backtracking.isFieldEmpty()) {
            if (isHeuristicMostFrequent) {
                var frequent = DataLoader.getMostFrequent(backtracking.grid);
                for (var e : backtracking.domain) {
                    if (!frequent.contains(e)) {
                        frequent.add(e);
                    }
                }
                backtracking.domain = frequent;
            }

            if(isHeuristicMostFilled) {
                backtracking.index = DataLoader.getIndexToInsert(backtracking.grid, backtracking.filedFieldsInRowNum, backtracking.filedFieldsInColNum, backtracking.size);
            }
            for (Integer value : backtracking.domain) {
                backtracking.setValue(value);
                insertCount++;

                if (backtracking.checkConstraints()) {
                    backtracking(backtracking.createCopy());
                    backtracking.gridsCount++;
                }

            }
        } else {
            backtracking(backtracking.createCopyAndIncreaseIndex());
        }
    }
}
