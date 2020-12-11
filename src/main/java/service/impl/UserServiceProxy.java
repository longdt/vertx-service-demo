package service.impl;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;
import io.vertx.serviceproxy.ServiceExceptionMessageCodec;
import model.entity.User;
import model.request.CreateUserRequest;
import service.UserService;
import util.ImmutableJsonObject;

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
        return vertx.eventBus().<JsonObject>request(address, ImmutableJsonObject.of(createRequest.copy()), _deliveryOptions)
                .map(msg -> {
                    var body = msg.body();
                    if (body == null) {
                        return null;
                    }
                    return ((ImmutableJsonObject) body).getObject();
                });
    }
}
