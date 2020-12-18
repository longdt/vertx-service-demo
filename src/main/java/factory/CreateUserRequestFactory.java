package factory;

import model.request.CreateUserRequest;
import serde.ClusterMessage;
import serde.ClusterMessageFactory;

public class CreateUserRequestFactory implements ClusterMessageFactory {
    public static final int FACTORY_ID = 1;
    public static final int CLUSTER_MESSAGE_TYPE = 1;

    @Override
    public ClusterMessage create(int typeId) {
        if (typeId == CLUSTER_MESSAGE_TYPE) {
            return new CreateUserRequest();
        }
        return null;
    }

    @Override
    public int getFactoryId() {
        return FACTORY_ID;
    }
}
