package model.request;

import io.vertx.core.shareddata.Shareable;

public class CreateUserRequest implements Shareable {
    private String username;
    private String password;

    public CreateUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
