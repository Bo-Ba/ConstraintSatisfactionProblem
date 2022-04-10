package backtracking;

import java.util.ArrayList;
import java.util.List;

public abstract class BacktrackingBase {
    public List<Integer> grid, domain;
    public int index, size;
    ArrayList<Integer> rowValues, columnValues, transposed;

    public abstract void setValue(int value);
    public abstract boolean checkConstraints();
    public abstract void increaseIndex();
    public abstract boolean isFilled();
    public abstract boolean isFieldEmpty();
    public abstract BacktrackingBase createCopyAndIncreaseIndex();
}
