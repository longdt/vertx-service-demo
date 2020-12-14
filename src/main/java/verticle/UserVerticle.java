package verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import service.impl.UserServiceVertxProxyHandler;
import service.impl.UserServiceImpl;

public class UserVerticle extends AbstractVerticle {
    public static final String ADDRESS = "users";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        var userService = new UserServiceImpl();
        var proxyHandler = new UserServiceVertxProxyHandler(vertx, userService, true, UserServiceVertxProxyHandler.DEFAULT_CONNECTION_TIMEOUT, true);
        vertx.eventBus().localConsumer(ADDRESS, proxyHandler);
        startPromise.complete();
    }
}
