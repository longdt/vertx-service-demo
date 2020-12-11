package verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.serviceproxy.ServiceBinder;
import service.UserService;
import service.impl.UserServiceImpl;

public class UserVerticle extends AbstractVerticle {
    public static final String ADDRESS = "users";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        var userService = new UserServiceImpl();
        new ServiceBinder(vertx).setAddress(ADDRESS)
                .register(UserService.class, userService);
        startPromise.complete();
    }
}
