package forwardchecking;

import backtracking.BacktrackingBase;

import java.util.ArrayList;
import java.util.List;

public abstract class ForwardCheckingBase {
    public List<Integer> grid, domain;
    public int index, size;
    ArrayList<Integer> rowValues, columnValues, transposed;

    public abstract void setValue(int value);
    public abstract boolean checkConstraints();
    public abstract void increaseIndex();
    public abstract boolean isFilled();
    public abstract boolean isFieldEmpty();
    public abstract void adjustDomain();
    public abstract ForwardCheckingBase createCopyAndIncreaseIndex();

}
