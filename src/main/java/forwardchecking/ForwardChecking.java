package forwardchecking;

import coinstraints.BinaryConstraints;
import coinstraints.FutoshikiConstraints;
import data_loader.DataLoader;
import graph.IndexDomainVertex;
import graph.RelationshipEdge;
import org.jgrapht.Graph;

import java.util.*;

public class ForwardChecking {

    public static int gridsCount = 0;
    public static int count = 0;
    public static boolean isHeuristicMostFrequent = false;

    public static void startForwardChecking(List<Integer> grid, int index, List<Integer> domain, Graph<IndexDomainVertex, RelationshipEdge> constraintGraph, int size) {
        forwardChecking(grid, index, domain, constraintGraph, size);
        System.out.println("Created grids: " + gridsCount);
    }

    public static void startForwardCheckingBinary(List<Integer> grid, int index, List<Integer> domain, int size) {
        forwardCheckingBinary(grid, index, domain, size);
        System.out.println("Created grids: " + gridsCount);
    }

    public static void forwardCheckingBinary(List<Integer> grid, int index, List<Integer> domain, int size) {
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
                    List<Integer> newDomain = new ArrayList<>(domain);
                    if(index != 0 && Objects.equals(grid.get(index - 1), value)) {
                        newDomain.remove(value);
                    } else {
                        newDomain = Arrays.asList(0, 1);
                    }
                    forwardCheckingBinary(new ArrayList<>(grid), index + 1, newDomain, size);
                }
            }
        } else {
            domain = Arrays.asList(0, 1);
            forwardCheckingBinary(new ArrayList<>(grid), index + 1, domain, size);
        }
    }

    public static void forwardChecking(List<Integer> grid, int index, List<Integer> domain, Graph<IndexDomainVertex, RelationshipEdge> constraintGraph, int size) {
        var defaultDomain = new ArrayList<>(domain);
        if (index == grid.size()) {
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
            try {
                domain = constraintGraph.vertexSet().stream().filter(v -> v.index == index).findFirst().get().domain;
                domain = new ArrayList<>(domain);
            } catch (NoSuchElementException e) {
                //just swallow the exception
            }
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

                var tempGraph = (Graph<IndexDomainVertex, RelationshipEdge>) DataLoader.deepCopyGraph(constraintGraph);
                FutoshikiConstraints.deleteIndexesFromNeighboursDomain(index, value, tempGraph);
                FutoshikiConstraints.deleteIndexesFromRowAndColumnDomain(index, value, tempGraph, size);

                if (isGreatest && !isValueInColumnDuplicated && !isValueInRowDuplicated) {
                    forwardChecking(new ArrayList<>(grid), index + 1, defaultDomain, tempGraph, size);
                }
            }
        } else {
            forwardChecking(new ArrayList<>(grid), index + 1, defaultDomain, constraintGraph, size);
        }
    }
}
