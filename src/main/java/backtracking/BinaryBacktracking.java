package backtracking;

import coinstraints.BinaryConstraints;
import data_loader.DataLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BinaryBacktracking extends BacktrackingBase {

    public BinaryBacktracking(List<Integer> grid, int index, int size) {
        this.grid = grid;
        this.domain = Arrays.asList(0, 1);
        this.index = index;
        this.size = size;
        this.filedFieldsInColNum = DataLoader.countRowFilledFields(grid, size);
        this.filedFieldsInRowNum = DataLoader.countColumnFilledFields(grid, size);
    }

    public BinaryBacktracking(BinaryBacktracking toCopy) {
        this.grid = new ArrayList<>(toCopy.grid);
        this.domain = new ArrayList<>(toCopy.domain);
        this.index = toCopy.index;
        this.size = toCopy.size;
        this.filedFieldsInColNum = toCopy.filedFieldsInColNum.clone();
        this.filedFieldsInRowNum = toCopy.filedFieldsInRowNum.clone();
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

        transposed = new ArrayList<>(grid);
        DataLoader.transpose(transposed, size);
    }

    @Override
    public boolean checkConstraints() {
        boolean areThreeConsecutiveZerosRow = BinaryConstraints.isPatternInList(rowValues, Arrays.asList(0, 0, 0));
        boolean areThreeConsecutiveOnesRow = BinaryConstraints.isPatternInList(rowValues, Arrays.asList(1, 1, 1));
        boolean areThreeConsecutiveZerosColumn = BinaryConstraints.isPatternInList(columnValues, Arrays.asList(0, 0, 0));
        boolean areThreeConsecutiveOnesColumn = BinaryConstraints.isPatternInList(columnValues, Arrays.asList(1, 1, 1));
        boolean areRowsUnique = BinaryConstraints.isUniqueRows(grid, size);
        boolean areColumnsUnique = BinaryConstraints.isUniqueRows(transposed, size);
        boolean zeroOneEqualRow = BinaryConstraints.isOneZero(rowValues);
        boolean zeroOneEqualColumn = BinaryConstraints.isOneZero(columnValues);

        gridsCount += 8;

        return !areThreeConsecutiveZerosRow
                && !areThreeConsecutiveOnesRow
                && !areThreeConsecutiveZerosColumn
                && !areThreeConsecutiveOnesColumn
                && areRowsUnique
                && areColumnsUnique
                && zeroOneEqualRow
                && zeroOneEqualColumn;
    }

    @Override
    public void increaseIndex() {
        if(index < grid.size() - 1) index++;
    }

    @Override
    public boolean isFilled() {
        return BinaryConstraints.isOnlyOnesZeros(grid);
    }

    @Override
    public boolean isFieldEmpty() {
        return grid.get(index) == -1;
    }

    @Override
    public BacktrackingBase createCopyAndIncreaseIndex() {
        BinaryBacktracking copy = new BinaryBacktracking(this);
        copy.increaseIndex();
        return copy;
    }

    @Override
    public BacktrackingBase createCopy() {
        BinaryBacktracking copy = new BinaryBacktracking(this);
        copy.increaseIndex();
        copy.filedFieldsInRowNum[index / size]++;
        copy.filedFieldsInColNum[index % size]++;
        return copy;
    }
}
