package backtracking;

import coinstraints.FutoshikiConstraints;
import graph.IndexDomainVertex;
import graph.RelationshipEdge;
import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FutoshikiBacktracking extends BacktrackingBase {
    public Graph<IndexDomainVertex, RelationshipEdge> constraintGraph;

    public FutoshikiBacktracking(List<Integer> grid, Graph<IndexDomainVertex, RelationshipEdge> constraintGraph, int index, int size) {
        this.grid = grid;
        this.domain = IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList());
        ;
        this.index = index;
        this.size = size;
        this.constraintGraph = constraintGraph;
    }

    public FutoshikiBacktracking(FutoshikiBacktracking toCopy) {
        this.grid = new ArrayList<>(toCopy.grid);
        this.domain = new ArrayList<>(toCopy.domain);
        this.index = toCopy.index;
        this.size = toCopy.size;
        this.constraintGraph = toCopy.constraintGraph;
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
        columnValues = new ArrayList<Integer>();

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
    public BacktrackingBase createCopyAndIncreaseIndex() {
        FutoshikiBacktracking copy = new FutoshikiBacktracking(this);
        copy.increaseIndex();
        return copy;
    }
}