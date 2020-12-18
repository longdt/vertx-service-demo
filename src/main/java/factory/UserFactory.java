package factory;

import model.entity.User;
import serde.ClusterMessage;
import serde.ClusterMessageFactory;

public class UserFactory implements ClusterMessageFactory {
    public static final int FACTORY_ID = 0;
    public static final int USER_TYPE = 1;

    @Override
    public ClusterMessage create(int typeId) {
        if (typeId == USER_TYPE) {
            return new User();
        }
        return null;
    }

    @Override
    public int getFactoryId() {
        return FACTORY_ID;
    }
}
