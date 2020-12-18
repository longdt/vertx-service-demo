package service.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.HelperUtils;
import io.vertx.serviceproxy.ServiceException;
import io.vertx.serviceproxy.ServiceExceptionMessageCodec;
import service.UserService;
import util.ImmutableJsonObject;

public class UserServiceLocalProxyHandler implements Handler<Message<Object>> {

    public static final long DEFAULT_CONNECTION_TIMEOUT = 5 * 60; // 5 minutes
    private final Vertx vertx;
    private final UserService service;
    private final long timerID;
    private long lastAccessed;
    private final long timeoutSeconds;
    private final boolean includeDebugInfo;

    public UserServiceLocalProxyHandler(Vertx vertx, UserService service) {
        this(vertx, service, DEFAULT_CONNECTION_TIMEOUT);
    }

    public UserServiceLocalProxyHandler(Vertx vertx, UserService service, long timeoutInSecond) {
        this(vertx, service, true, timeoutInSecond);
    }

    public UserServiceLocalProxyHandler(Vertx vertx, UserService service, boolean topLevel, long timeoutInSecond) {
        this(vertx, service, true, timeoutInSecond, false);
    }

    public UserServiceLocalProxyHandler(Vertx vertx, UserService service, boolean topLevel, long timeoutSeconds, boolean includeDebugInfo) {
        this.vertx = vertx;
        this.service = service;
        this.includeDebugInfo = includeDebugInfo;
        this.timeoutSeconds = timeoutSeconds;
        try {
            this.vertx.eventBus().registerDefaultCodec(ServiceException.class,
                    new ServiceExceptionMessageCodec());
        } catch (IllegalStateException ex) {
        }
        if (timeoutSeconds != -1 && !topLevel) {
            long period = timeoutSeconds * 1000 / 2;
            if (period > 10000) {
                period = 10000;
            }
            this.timerID = vertx.setPeriodic(period, this::checkTimedOut);
        } else {
            this.timerID = -1;
        }
        accessed();
    }


    private void checkTimedOut(long id) {
        long now = System.nanoTime();
        if (now - lastAccessed > timeoutSeconds * 1000000000) {
            close();
        }
    }

//    @Override
    public void close() {
        if (timerID != -1) {
            vertx.cancelTimer(timerID);
        }
//        super.close();
    }

    private void accessed() {
        this.lastAccessed = System.nanoTime();
    }

    public void handle(Message<Object> msg) {
        try {
            String action = msg.headers().get("action");
            if (action == null) throw new IllegalStateException("action not specified");
            accessed();
            switch (action) {
                case "createUser": {
                    ImmutableJsonObject json = (ImmutableJsonObject) msg.body();
                    service.createUser(json.getObject())
                            .onComplete(res -> {
                                if (res.failed()) {
                                    HelperUtils.manageFailure(msg, res.cause(), includeDebugInfo);
                                } else {
                                    msg.reply(res.result() != null ? ImmutableJsonObject.of(res.result()) : null);
                                }
                            });
                    break;
                }
                case "getUsers": {
                    JsonArray json = (JsonArray) msg.body();
                    var params = json.getList();
                    service.getUsers((JsonObject) params.get(0), (long) params.get(1), (int) params.get(2))
                            .onComplete(res -> {
                                if (res.failed()) {
                                    HelperUtils.manageFailure(msg, res.cause(), includeDebugInfo);
                                } else {
                                    msg.reply(res.result() != null ? new JsonArray(res.result()) : null);
                                }
                            });
                    break;
                }
                default:
                    throw new IllegalStateException("Invalid action: " + action);
            }
        } catch (Throwable t) {
            if (includeDebugInfo)
                msg.reply(new ServiceException(500, t.getMessage(), HelperUtils.generateDebugInfo(t)));
            else msg.reply(new ServiceException(500, t.getMessage()));
            throw t;
        }
    }
}
