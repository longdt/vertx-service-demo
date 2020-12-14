package util;

import io.vertx.core.shareddata.Shareable;

public class Tuple1<T0> implements Shareable {
    /** Field 0 of the tuple. */
    private T0 f0;

    public Tuple1(T0 f0) {
        this.f0 = f0;
    }

    public T0 getF0() {
        return f0;
    }
}
