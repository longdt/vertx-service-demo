package service;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import model.entity.User;
import model.request.CreateUserRequest;
import service.impl.UserServiceProxy;
import verticle.UserVerticle;

import java.util.List;

public interface UserService {
    static UserService createProxy(Vertx vertx) {
        return new UserServiceProxy(vertx, UserVerticle.ADDRESS);
    }

    Future<User> createUser(CreateUserRequest createRequest);

    Future<List<User>> getUsers(JsonObject filter, long offset, int limit);
}
