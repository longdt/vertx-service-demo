package service.impl;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import model.entity.User;
import model.request.CreateUserRequest;
import service.UserService;

import java.util.List;

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

    @Override
    public Future<List<User>> getUsers(JsonObject filter, long offset, int limit) {
        var user1 = new User()
                .setUserId(++counter)
                .setUsername("username1")
                .setPassword("password1");
        var user2 = new User()
                .setUserId(++counter)
                .setUsername("username2")
                .setPassword("password2");
        return Future.succeededFuture(List.of(user1, user2));
    }
}
