package structures;

public class Pair<T, Y> {
    public T first;
    public Y second;

    public Pair(T first, Y second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + this.first.toString() + ", " + this.second.toString() + ")";
    }
}
