package helpers;
import java.lang.Exception;

public class Wrappers {
    @FunctionalInterface
    public static interface IException<T> {
        public T useTryCatch() throws Exception;
    }

    private Wrappers() {}

    public static <T> T useTryCatch(IException<T> exp) {
        try {
            T result = exp.useTryCatch();
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
