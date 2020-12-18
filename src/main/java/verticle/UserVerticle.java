package verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import service.impl.UserServiceClusteredProxyHandler;
import service.impl.UserServiceImpl;
import service.impl.UserServiceLocalProxyHandler;

public class UserVerticle extends AbstractVerticle {
    public static final String ADDRESS = "users";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        var userService = new UserServiceImpl();
        var proxyHandler = new UserServiceClusteredProxyHandler(vertx, userService, true, UserServiceLocalProxyHandler.DEFAULT_CONNECTION_TIMEOUT, true);
        vertx.eventBus().consumer(ADDRESS, proxyHandler);
        startPromise.complete();
    }
}
