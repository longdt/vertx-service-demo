package service.impl;

import io.vertx.core.Future;
import model.entity.User;
import model.request.CreateUserRequest;
import service.UserService;

/***
 * no thread-safe. Need run inside verticle
 */
public class UserServiceImpl implements UserService {
    private long counter;

    @Override
    public Future<User> createUser(CreateUserRequest createRequest) {
        var user = new User()
                .setUserId(++counter)
                .setUsername(createRequest.getUsername())
                .setPassword(createRequest.getPassword());
        return Future.succeededFuture(user);
    }
}
