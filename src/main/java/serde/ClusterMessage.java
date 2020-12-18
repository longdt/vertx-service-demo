package serde;

import io.vertx.core.shareddata.Shareable;
import io.vertx.core.shareddata.impl.ClusterSerializable;

public interface ClusterMessage extends Shareable, ClusterSerializable {
    int getFactoryId();

    int getClassId();
}
