package util;

import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.Shareable;

import java.util.Collections;

public class ImmutableJsonObject extends JsonObject {
    private Object object;

    private ImmutableJsonObject(Shareable shareable) {
        super(Collections.emptyMap());
        this.object = shareable.copy();
    }

    public static ImmutableJsonObject of(Shareable shareable) {
        return new ImmutableJsonObject(shareable);
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject() {
        return (T) object;
    }

    @Override
    public JsonObject copy() {
        return this;
    }
}