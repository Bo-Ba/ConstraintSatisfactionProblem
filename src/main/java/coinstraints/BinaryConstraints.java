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

    //write method to find if number of ones is equal to number of zeros in a list
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

    //with given a 1d array which represents a matrix and rows number, write method  to find if every row is unique
    //ignore if -1 in a row
    public static boolean isUniqueRows(List<Integer> grid, int rows) {
        for (int i = 0; i < rows; i++) {
            for (int j = i + 1; j < rows; j++) {
                if (grid.subList(i * rows, i * rows + rows).equals(grid.subList(j * rows, j * rows + rows))) return false;
            }
        }
        return true;
    }

    //write method to check if list contains only ones and zeros
    public static boolean isOnlyOnesZeros(List<Integer> list) {
        for (Integer integer : list) {
            if (integer != 0 && integer != 1) return false;
        }
        return true;
    }
}
