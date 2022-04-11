package coinstraints;

import java.util.List;

public class BinaryConstraints {

    public static boolean isPatternInList(List<Integer> grid, List<Integer> pattern) {
        if (pattern.size() > grid.size()) return false;
        for (int i = 0; i < grid.size() - pattern.size() + 1; i++) {
            if (grid.subList(i, i + pattern.size()).equals(pattern)) return true;
        }
        return false;
    }

    public static boolean isOneZero(List<Integer> list) {
        if(list.contains(-1)) return true;
        int ones = 0;
        int zeros = 0;
        for (Integer integer : list) {
            if (integer == 1) ones++;
            else zeros++;
        }
        return ones == zeros;
    }

    public static boolean isUniqueRows(List<Integer> grid, int rows) {
        for (int i = 0; i < rows; i++) {
            for (int j = i + 1; j < rows; j++) {
                List<Integer> firstRow = grid.subList(i * rows, i * rows + rows);
                List<Integer> secondRow = grid.subList(j * rows, j * rows + rows);
                if(firstRow.stream().anyMatch(v -> v == -1) || secondRow.stream().anyMatch(v -> v == -1)) return true;
                if (firstRow.equals(secondRow)) return false;
            }
        }
        return true;
    }

    public static boolean isOnlyOnesZeros(List<Integer> list) {
        for (Integer integer : list) {
            if (integer != 0 && integer != 1) return false;
        }
        return true;
    }
}
