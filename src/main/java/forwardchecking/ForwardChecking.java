package forwardchecking;

import coinstraints.FutoshikiConstraints;
import data_loader.DataLoader;
import graph.IndexDomainVertex;
import graph.RelationshipEdge;
import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ForwardChecking {

    static int gridsCount = 0;

    public static void startForwardChecking(List<Integer> grid, int index, List<Integer> domain, Graph<IndexDomainVertex, RelationshipEdge> constraintGraph, int size) {
        forwardChecking(grid,  index, domain, constraintGraph, size);
        System.out.println("Created grids: " + gridsCount);
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
