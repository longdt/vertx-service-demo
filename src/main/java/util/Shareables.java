package util;

import io.vertx.core.shareddata.Shareable;

import java.util.ArrayList;
import java.util.List;

public class Shareables {
    public static <T extends Shareable> List<T> copy(List<T> shareables) {
        var result = new ArrayList<T>(shareables.size());
        for (var shareable : shareables) {
            result.add((T) shareable.copy());
        }
        return result;
    }
}
