package util;

public class Tuple7<T0, T1, T2, T3, T4, T5, T6> {
    /** Field 0 of the tuple. */
    private T0 f0;
    /** Field 1 of the tuple. */
    private T1 f1;
    /** Field 2 of the tuple. */
    private T2 f2;
    /** Field 3 of the tuple. */
    private T3 f3;
    /** Field 4 of the tuple. */
    private T4 f4;
    /** Field 5 of the tuple. */
    private T5 f5;
    /** Field 6 of the tuple. */
    private T6 f6;

    public Tuple7(T0 f0, T1 f1, T2 f2, T3 f3, T4 f4, T5 f5, T6 f6) {
        this.f0 = f0;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.f4 = f4;
        this.f5 = f5;
        this.f6 = f6;
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

    public T4 getF4() {
        return f4;
    }

    public T5 getF5() {
        return f5;
    }

    public T6 getF6() {
        return f6;
    }
}
