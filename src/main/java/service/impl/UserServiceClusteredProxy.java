package service.impl;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;
import io.vertx.serviceproxy.ServiceExceptionMessageCodec;
import model.entity.User;
import model.request.CreateUserRequest;
import serde.ClusterMessage;
import service.UserService;
import util.Arguments;

import java.util.List;

public class UserServiceClusteredProxy implements UserService {
    private Vertx vertx;
    private String address;

    public UserServiceClusteredProxy(Vertx vertx, String address) {
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
        _deliveryOptions.setCodecName("clustermessage");
        return vertx.eventBus().<ClusterMessage>request(address, createRequest, _deliveryOptions)
                .map(msg -> {
                    var body = msg.body();
                    if (body == null) {
                        return null;
                    }
                    return (User) body;
                });
    }

    @Override
    public Future<List<User>> getUsers(JsonObject filter, long offset, int limit) {
        DeliveryOptions _deliveryOptions = new DeliveryOptions();
        _deliveryOptions.addHeader("action", "getUsers");
        _deliveryOptions.setCodecName("clustermessage");
        return vertx.eventBus().<Arguments>request(address, new Arguments(filter, offset, limit), _deliveryOptions)
                .map(msg -> {
                    var body = msg.body();
                    if (body == null) {
                        return null;
                    }
                    return body.getList();
                });
    }
}
