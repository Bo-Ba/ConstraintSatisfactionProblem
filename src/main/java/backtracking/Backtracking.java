package backtracking;

import data_loader.DataLoader;


public class Backtracking {
    public static int count = 0;
    static int gridsCount = 0;
    public static boolean isHeuristicMostFrequent = false;

    public static void startBacktracking(BacktrackingBase backtracking) {
        backtracking(backtracking);
        System.out.println("Created grids: " + gridsCount);
        System.out.println("Found solutions: " + count);
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
            for (Integer value : backtracking.domain) {
                backtracking.setValue(value);
                gridsCount++;

                if (backtracking.checkConstraints()) {
                    backtracking(backtracking.createCopyAndIncreaseIndex());
                }
            }
        } else {
            backtracking(backtracking.createCopyAndIncreaseIndex());
        }
    }
}
