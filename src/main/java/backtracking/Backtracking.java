package backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import coinstraints.BinaryConstraints;
import coinstraints.FutoshikiConstraints;
import data_loader.DataLoader;
import graph.IndexDomainVertex;
import graph.RelationshipEdge;
import org.jgrapht.Graph;


public class Backtracking {
   public static int count = 0;
   static int gridsCount = 0;
    public static boolean isHeuristicMostFrequent = false;

    public static void startBacktracking(List<Integer> grid, int index, List<Integer> domain, Graph<IndexDomainVertex, RelationshipEdge> constraintGraph, int size) {
        backtracking(grid,  index, domain, constraintGraph, size);
        System.out.println("Created grids: " + gridsCount);
    }

    public static void startBacktrackingBinary(List<Integer> grid, int index, List<Integer> domain, int size) {
        backtrackingBinary(grid,  index, domain, size);
        System.out.println("Created grids: " + gridsCount);
    }

    public static void backtracking(List<Integer> grid, int index, List<Integer> domain, Graph<IndexDomainVertex, RelationshipEdge> constraintGraph, int size) {
        if (index == grid.size()) {
            System.out.println("solution");
            int ind = 0;
            count++;
            for (var e : grid) {
                if (ind % size == 0) {
                    System.out.println();
                }
                System.out.print(e + " ");
                ind++;
            }
            System.out.println();
            return;
        }

        if (grid.get(index) == -1) {
            if(isHeuristicMostFrequent) {
                var frequent = DataLoader.getMostFrequent(grid);
                var temp = new ArrayList<Integer>();
                for (var e : domain) {
                    if (!frequent.contains(e)) {
                        frequent.add(e);
                    }
                }
                domain = frequent;
            }
            for (Integer value : domain) {
                gridsCount++;
                grid.set(index, value);
                int row = index / size;
                int column = index % size;
                var rowValues = new ArrayList<>(grid.subList(row * size, (row + 1) * size));
                var columnValues = new ArrayList<Integer>();

                while (column < grid.size()) {
                    columnValues.add(grid.get(column));
                    column = column + size;
                }

                boolean isGreatest = FutoshikiConstraints.checkIfGreatest(grid, index, constraintGraph, domain);
                boolean isValueInRowDuplicated = FutoshikiConstraints.checkDuplicates(rowValues);
                boolean isValueInColumnDuplicated = FutoshikiConstraints.checkDuplicates(columnValues);

                if (isGreatest && !isValueInColumnDuplicated && !isValueInRowDuplicated) {
                    backtracking(new ArrayList<>(grid), index + 1, domain, constraintGraph, size);
                }
            }
        } else {
            backtracking(new ArrayList<>(grid), index + 1, domain, constraintGraph, size);
        }
    }

    public static void backtrackingBinary(List<Integer> grid, int index, List<Integer> domain, int size) {
        if (index == grid.size() && BinaryConstraints.isOnlyOnesZeros(grid)) {
            System.out.println("solution");
            int ind = 0;
            for (var e : grid) {
                if (ind % size == 0) {
                    System.out.println();
                }
                System.out.print(e + " ");
                ind++;
            }
            System.out.println();
            return;
        }

        if (grid.get(index) == -1) {
            if(isHeuristicMostFrequent) {
                var frequent = DataLoader.getMostFrequent(grid);
                var temp = new ArrayList<Integer>();
                for (var e : domain) {
                    if (!frequent.contains(e)) {
                        frequent.add(e);
                    }
                }
                domain = frequent;
            }
            for (Integer value : domain) {
                grid.set(index, value);
                gridsCount++;
                int row = index / size;
                int column = index % size;
                var rowValues = new ArrayList<>(grid.subList(row * size, (row + 1) * size));
                var columnValues = new ArrayList<Integer>();

                while (column < grid.size()) {
                    columnValues.add(grid.get(column));
                    column = column + size;
                }

                List<Integer> transposed = new ArrayList<>(grid);
                DataLoader.transpose(transposed, size);

                boolean areThreeConsecutiveZerosRow = BinaryConstraints.isPatternInList(rowValues, Arrays.asList(0, 0, 0));
                boolean areThreeConsecutiveOnesRow = BinaryConstraints.isPatternInList(rowValues, Arrays.asList(1, 1, 1));
                boolean areThreeConsecutiveZerosColumn = BinaryConstraints.isPatternInList(columnValues, Arrays.asList(0, 0, 0));
                boolean areThreeConsecutiveOnesColumn = BinaryConstraints.isPatternInList(columnValues, Arrays.asList(1, 1, 1));
                boolean areRowsUnique = BinaryConstraints.isUniqueRows(grid, size);
                boolean areColumnsUnique = BinaryConstraints.isUniqueRows(transposed, size);
                boolean zeroOneEqualRow = BinaryConstraints.isOneZero(rowValues);
                boolean zeroOneEqualColumn = BinaryConstraints.isOneZero(columnValues);

                if (!areThreeConsecutiveZerosRow
                        && !areThreeConsecutiveOnesRow
                        && !areThreeConsecutiveZerosColumn
                        && !areThreeConsecutiveOnesColumn
                        && areRowsUnique
                        && areColumnsUnique
                        && zeroOneEqualRow
                        && zeroOneEqualColumn
                ) {
                    backtrackingBinary(new ArrayList<>(grid), index + 1, domain, size);
                }
            }
        } else {
            backtrackingBinary(new ArrayList<>(grid), index + 1, domain, size);
        }
    }
}
