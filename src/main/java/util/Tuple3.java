package util;

public class Tuple3<T0, T1, T2> {
    /** Field 0 of the tuple. */
    private T0 f0;
    /** Field 1 of the tuple. */
    private T1 f1;
    /** Field 2 of the tuple. */
    private T2 f2;

    public Tuple3(T0 f0, T1 f1, T2 f2) {
        this.f0 = f0;
        this.f1 = f1;
        this.f2 = f2;
    }

    public T0 getF0() {
        return f0;
    }

    public T1 getF1() {
        return f1;
    }

    public T2 getF2() {
        return f2;
    }
}
