package app;

import factory.CreateUserRequestFactory;
import factory.UserFactory;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import model.request.CreateUserRequest;
import serde.ClusterMessageCodec;
import serde.ClusterMessageFactory;
import service.UserService;
import service.impl.UserServiceClusteredProxy;
import util.Futures;
import verticle.UserVerticle;

import java.util.Arrays;

public class DemoApplication {
    public static void main(String[] args) throws Exception {
        var vertx = Futures.join(Vertx.clusteredVertx(new VertxOptions()));
        registerCodec(vertx);
        var userService = new UserServiceClusteredProxy(vertx, UserVerticle.ADDRESS);
        vertx.deployVerticle(UserVerticle.class, new DeploymentOptions(), ar -> {
           if (ar.succeeded()) {
               sendRequest(userService);
           } else {
               ar.cause().printStackTrace();
           }
        });
    }

    private static void registerCodec(Vertx vertx) {
        var factories = registerFactory(new UserFactory(), new CreateUserRequestFactory());
        vertx.eventBus().registerCodec(new ClusterMessageCodec(factories));
    }

    private static ClusterMessageFactory[] registerFactory(ClusterMessageFactory... factories) {
        int max = Arrays.stream(factories).mapToInt(ClusterMessageFactory::getFactoryId).max().orElse(-1);
        var result = new ClusterMessageFactory[max + 1];
        Arrays.stream(factories).forEach(f -> result[f.getFactoryId()] = f);
        return result;
    }

    private static void sendRequest(UserService userService) {
        var createReq = new CreateUserRequest("username", "password");
        userService.createUser(createReq)
                .onSuccess(System.out::println)
                .onComplete(ar -> getUsers(userService));
    }

    private static void getUsers(UserService userService) {
        userService.getUsers(new JsonObject(), 1, 2)
                .onSuccess(System.out::println)
                .onFailure(Throwable::printStackTrace);
//                .onComplete(r -> System.exit(0));
    }
}
