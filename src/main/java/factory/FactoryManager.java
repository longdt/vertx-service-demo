package factory;

import serde.ClusterMessageFactory;

import java.util.Arrays;

public class FactoryManager {
    private ClusterMessageFactory[] factories;

    public static FactoryManager getInstance() {
        return Holder.INSTANCE;
    }

    public FactoryManager addFactory(ClusterMessageFactory factory) {
        var fcts = factories;
        if (fcts.length <= factory.getFactoryId()) {
            fcts = Arrays.copyOf(fcts, Math.max(fcts.length << 1, factory.getFactoryId()) + 1);
        }
        fcts[factory.getFactoryId()] = factory;
        factories = fcts;
        return this;
    }

    public ClusterMessageFactory getFactory(int factoryId) {
        return factories[factoryId];
    }

    private interface Holder {
        FactoryManager INSTANCE = new FactoryManager();
    }
}
