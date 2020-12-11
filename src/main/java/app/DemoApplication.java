package app;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import model.request.CreateUserRequest;
import service.UserService;
import verticle.UserVerticle;

public class DemoApplication {
    public static void main(String[] args) throws Exception {
        var vertx = Vertx.vertx();
        var userService = UserService.createProxy(vertx);
        vertx.deployVerticle(UserVerticle.class, new DeploymentOptions(), ar -> {
           if (ar.succeeded()) {
               sendRequest(userService);
           } else {
               ar.cause().printStackTrace();
           }
        });
    }

    private static void sendRequest(UserService userService) {
        var createReq = new CreateUserRequest("username", "password");
        userService.createUser(createReq).onSuccess(System.out::println);
    }
}
