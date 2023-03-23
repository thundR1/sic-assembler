package structures;

import java.util.ArrayList;
import java.util.Arrays;

abstract public class AbstractTable2D<T, Y> {
    private ArrayList<Pair<T, Y>> table;

    abstract public boolean validateEntry(Instruction instruction);

    protected AbstractTable2D() {
        this.table = new ArrayList<Pair<T, Y>>();
        this.table.clear();
    }
    
    public void add(T first, Y second) {
        this.table.add(new Pair<T, Y>(first, second));
    }
    
    public void remove(int index) {
        this.table.remove(index);
    }

    public int size() {
        return this.table.size();
    }

    public ArrayList<Pair<T, Y>> getContent() {
        return this.table;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.table.toArray());
    }
}
