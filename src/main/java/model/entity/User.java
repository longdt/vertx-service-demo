package model.entity;

import io.vertx.core.shareddata.Shareable;

public class User implements Shareable {
    private Long userId;
    private String username;
    private String password;

    public User() {
    }

    public User(User other) {
        this.userId = other.userId;
        this.username = other.username;
        this.password = other.password;
    }

    public Long getUserId() {
        return userId;
    }

    public User setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public User copy() {
        return new User(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
