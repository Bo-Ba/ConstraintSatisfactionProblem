package forwardchecking;

import coinstraints.FutoshikiConstraints;
import data_loader.DataLoader;
import graph.IndexDomainVertex;
import graph.RelationshipEdge;
import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FutoshikiForwardChecking extends ForwardCheckingBase {
    public Graph<IndexDomainVertex, RelationshipEdge> constraintGraph;

    public FutoshikiForwardChecking(List<Integer> grid, Graph<IndexDomainVertex, RelationshipEdge> constraintGraph, int index, int size) {
        this.grid = grid;
        this.domain = IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList());
        this.index = index;
        this.size = size;
        this.constraintGraph = DataLoader.deepCopyGraph(constraintGraph);
    }

    public FutoshikiForwardChecking(FutoshikiForwardChecking toCopy) {
        this.grid = new ArrayList<>(toCopy.grid);
        this.domain = new ArrayList<>(toCopy.domain);
        this.index = toCopy.index;
        this.size = toCopy.size;
        this.constraintGraph = null;
    }

    @Override
    public void setValue(int value) {
        grid.set(index, value);
        extractDataToConstraintsCheck();
    }

    private void extractDataToConstraintsCheck() {
        int row = index / size;
        int column = index % size;
        rowValues = new ArrayList<>(grid.subList(row * size, (row + 1) * size));
        columnValues = new ArrayList<>();

        while (column < grid.size()) {
            columnValues.add(grid.get(column));
            column = column + size;
        }
    }

    @Override
    public boolean checkConstraints() {
        boolean isGreatest = FutoshikiConstraints.checkIfGreatest(grid, index, constraintGraph, domain);
        boolean isValueInRowDuplicated = FutoshikiConstraints.checkDuplicates(rowValues);
        boolean isValueInColumnDuplicated = FutoshikiConstraints.checkDuplicates(columnValues);

        return isGreatest && !isValueInColumnDuplicated && !isValueInRowDuplicated;
    }

    @Override
    public void increaseIndex() {
        index++;
    }

    @Override
    public boolean isFilled() {
        return grid.size() == index;
    }

    @Override
    public boolean isFieldEmpty() {
        return grid.get(index) == -1;
    }

    @Override
    public FutoshikiForwardChecking createCopyAndIncreaseIndex() {
        FutoshikiForwardChecking copy = new FutoshikiForwardChecking(this);

        var tempGraph = (Graph<IndexDomainVertex, RelationshipEdge>) DataLoader.deepCopyGraph(constraintGraph);
        FutoshikiConstraints.deleteIndexesFromNeighboursDomain(index, grid.get(index), tempGraph);
        FutoshikiConstraints.deleteIndexesFromRowAndColumnDomain(index, grid.get(index), tempGraph, size);

        copy.constraintGraph = tempGraph;
        copy.increaseIndex();

        return copy;
    }

    @Override
    public void adjustDomain() {
        try {
            domain = constraintGraph.vertexSet().stream().filter(v -> v.index == index).findFirst().get().domain;
            domain = new ArrayList<>(domain);
        } catch (NoSuchElementException e) {
            //just swallow the exception
        }
    }
}
