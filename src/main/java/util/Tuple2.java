package util;

public class Tuple2<T0, T1> {
    /** Field 0 of the tuple. */
    private T0 f0;
    /** Field 1 of the tuple. */
    private T1 f1;

    public Tuple2(T0 f0, T1 f1) {
        this.f0 = f0;
        this.f1 = f1;
    }

    public T0 getF0() {
        return f0;
    }

    public T1 getF1() {
        return f1;
    }
}
