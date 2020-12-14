package util;

public class Tuple4<T0, T1, T2, T3> {
    /** Field 0 of the tuple. */
    private T0 f0;
    /** Field 1 of the tuple. */
    private T1 f1;
    /** Field 2 of the tuple. */
    private T2 f2;
    /** Field 3 of the tuple. */
    private T3 f3;

    public Tuple4(T0 f0, T1 f1, T2 f2, T3 f3) {
        this.f0 = f0;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
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

    public T3 getF3() {
        return f3;
    }
}
