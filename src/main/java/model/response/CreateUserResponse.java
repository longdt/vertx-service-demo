package model.response;

public class CreateUserResponse {
    private long userId;
    private String username;

    public CreateUserResponse(long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
