package serde;

public interface ClusterMessageFactory {
    ClusterMessage create(int typeId);

    int getFactoryId();
}
