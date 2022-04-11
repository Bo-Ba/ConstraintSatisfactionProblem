package forwardchecking;

import coinstraints.BinaryConstraints;
import data_loader.DataLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BinaryForwardChecking extends ForwardCheckingBase {
    private Integer inserted;

    public BinaryForwardChecking(List<Integer> grid, int index, int size) {
        this.grid = grid;
        this.domain = Arrays.asList(0, 1);
        this.index = index;
        this.size = size;
        this.filedFieldsInColNum = DataLoader.countRowFilledFields(grid, size);
        this.filedFieldsInRowNum = DataLoader.countColumnFilledFields(grid, size);
    }

    public BinaryForwardChecking(BinaryForwardChecking toCopy) {
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
        inserted = value;
        extractDataToConstraintsCheck();

        filedFieldsInRowNum[index / size]++;
        filedFieldsInColNum[index % size]++;
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
        index++;
    }

    @Override
    public boolean isFilled() {
        return grid.size() == index && BinaryConstraints.isOnlyOnesZeros(grid);
    }

    @Override
    public boolean isFieldEmpty() {
        return grid.get(index) == -1;
    }

    @Override
    public BinaryForwardChecking createCopyAndIncreaseIndex() {
        BinaryForwardChecking copy = new BinaryForwardChecking(this);

        copy.domain = this.adjustChildDomain();
        copy.increaseIndex();

        return copy;
    }

    private List<Integer> adjustChildDomain() {
        List<Integer> newDomain = new ArrayList<>(domain);
        if (index != 0 && inserted != null && Objects.equals(grid.get(index - 1), inserted)) {
            newDomain.remove(inserted);
        } else {
            newDomain = Arrays.asList(0, 1);
        }

        return newDomain;
    }


    @Override
    public void adjustDomain() {
        //just do nothing
    }

    @Override
    public BinaryForwardChecking createCopy() {
        BinaryForwardChecking copy = new BinaryForwardChecking(this);
        copy.increaseIndex();
        copy.filedFieldsInRowNum[index / size]++;
        copy.filedFieldsInColNum[index % size]++;
        return copy;
    }
}
