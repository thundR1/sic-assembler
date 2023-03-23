package memory;

import java.lang.IllegalArgumentException;

public class RegistersProvider {
    private int A = 0;
    private int X = 0;

    private static final RegistersProvider provider = new RegistersProvider();

    private RegistersProvider() {}

    public int A() { return A; }
    public int X() { return X; }

    public void mutateA(int val) {
        if (val < 0) throw new IllegalArgumentException("Register A Can't Be Negative.");
        A = val;
    }

    public void mutateX(int val) {
        if (val < 0) throw new IllegalArgumentException("Register X Can't Be Negative.");
        X = val;
    }

    public static RegistersProvider getProvider() {
        return provider;
    }
}
