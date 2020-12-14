package service.impl;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;
import io.vertx.serviceproxy.ServiceExceptionMessageCodec;
import model.entity.User;
import model.request.CreateUserRequest;
import service.UserService;
import util.ImmutableJsonObject;

import java.util.List;

public class UserServiceProxy implements UserService {
    private Vertx vertx;
    private String address;

    public UserServiceProxy(Vertx vertx, String address) {
        this.vertx = vertx;
        this.address = address;
        try {
            this.vertx.eventBus().registerDefaultCodec(ServiceException.class,
                    new ServiceExceptionMessageCodec());
        } catch (IllegalStateException ex) {
        }
    }

    @Override
    public Future<User> createUser(CreateUserRequest createRequest) {
        DeliveryOptions _deliveryOptions = new DeliveryOptions();
        _deliveryOptions.addHeader("action", "createUser");
        return vertx.eventBus().<JsonObject>request(address, ImmutableJsonObject.of(createRequest), _deliveryOptions)
                .map(msg -> {
                    var body = msg.body();
                    if (body == null) {
                        return null;
                    }
                    return ((ImmutableJsonObject) body).getObject();
                });
    }

    @Override
    public Future<List<User>> getUsers(JsonObject filter, long offset, int limit) {
        DeliveryOptions _deliveryOptions = new DeliveryOptions();
        _deliveryOptions.addHeader("action", "getUsers");
        return vertx.eventBus().<JsonArray>request(address, new JsonArray(List.of(filter, offset, limit)), _deliveryOptions)
                .map(msg -> {
                    var body = msg.body();
                    if (body == null) {
                        return null;
                    }
                    return body.getList();
                });
    }
}
