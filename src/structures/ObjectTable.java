package structures;

import java.util.ArrayList;

public class ObjectTable {
    private ArrayList<String> table;

    public ObjectTable() {
        this.table = new ArrayList<String>();
        this.table.clear();
    }

    public void add(String objectCode) {
        this.table.add(objectCode);
    }

    public void remove(int index) {
        this.table.remove(index);
    }

    public ArrayList<String> getContent() {
        return this.table;
    }

    @Override
    public String toString() {
        return this.table.toString();
    }
}
